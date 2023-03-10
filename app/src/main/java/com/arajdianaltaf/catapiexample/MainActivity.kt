package com.arajdianaltaf.catapiexample

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.catapiexample.data.CatResponseItem
import com.arajdianaltaf.catapiexample.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private fun makeToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, length).show()
    }

    private suspend fun setBreedList() = withContext(Dispatchers.IO) {
        val gson = Gson()

        val inputStream: InputStream = resources.openRawResource(R.raw.breeds)
        val reader = BufferedReader(InputStreamReader(inputStream))


        val jsonRead = gson.fromJson(reader, Array<CatResponseItem>::class.java)
        val breedList = mutableListOf<String>()

        for (cat in jsonRead) {
            breedList.add(cat.name)
            Constants.idMap[cat.name] = cat.id
        }



        withContext(Dispatchers.Main) {
            val breedSelectAdapter =
                ArrayAdapter(this@MainActivity, R.layout.dropdown_item, breedList)
            binding?.actvBreedType?.setAdapter(breedSelectAdapter)
            binding?.actvBreedType?.isClickable = true
            binding?.actvBreedType?.isFocusable = true
        }

    }

    private fun bottomSheetContentsAction(show: Boolean) {

        val contents = listOf(
            binding?.tvHeaderDesc,
            binding?.tvContentDesc,
            binding?.tvSubHeaderFullInfo,
            binding?.tableInfo,
            binding?.llWikipediaBtn
        )

        if (show) {
            for (view in contents) {
                view?.visibility = View.VISIBLE
            }
        } else if (!show) {
            for (view in contents) {
                view?.visibility = View.GONE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter = CatPhotoAdapter(this)

        binding?.rvImages?.adapter = adapter
        binding?.rvImages?.layoutManager = GridLayoutManager(this, 3)

        // Get breeds list
        lifecycleScope.launch {
            setBreedList()
        }

        // Init BottomSheet
        val behavior = BottomSheetBehavior.from(binding?.flStandardbottomSheet!!)
        behavior.apply {
            this.peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
            this.isHideable = false
            this.isFitToContents = true
        }

        // Make bottomSheet contents gone
        bottomSheetContentsAction(false)

        // Button OnClickListener
        binding?.btnRequest?.setOnClickListener {

            val selectedBreed: String = binding?.actvBreedType?.text.toString()
            val breed = Constants.idMap[selectedBreed]

            when {
                TextUtils.isEmpty(breed) -> {
                    makeToast("Please select breed.")
                }

                else -> {
                    Log.i(Constants.TAG_MAIN_ACTIVITY, "All entry are valid.")

                    lifecycleScope.launchWhenCreated {
                        val response = try {
                            RetrofitClient.api.getCatImagesByBreed(
                                api_key = Constants.API_KEY,
                                breedId = breed!!,
                            )


                        } catch (e: IOException) {
                            Log.e("MainActivity", "$e, you might not have internet connection")
                            makeToast("$e, you might not have internet connection")
                            return@launchWhenCreated

                        } catch (e: HttpException) {
                            Log.e("MainActivity", "$e, unexpected response")
                            makeToast("$e, unexpected response")
                            return@launchWhenCreated

                        }

                        if (response.isSuccessful && response.body() != null) {
                            adapter.images = response.body()!!

                            // Show all bottomSheet Contents
                            bottomSheetContentsAction(true)

                            binding?.tvNoBreedSelected?.visibility = View.GONE

                            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                            binding?.tvNoBreedSelected?.visibility = View.GONE

                            val breedInfo = response.body()!![0].breeds[0]

                            binding?.tvContentDesc?.text = breedInfo.description
                            binding?.tvTrNameValue?.text = breedInfo.name
                            binding?.tvTrOriginValue?.text = breedInfo.origin
                            binding?.tvTrLifespanValue?.text = breedInfo.life_span

                            binding?.llWikipediaBtn?.setOnTouchListener { v, event ->
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        v.background.setColorFilter(Color.parseColor("#FF676767"), PorterDuff.Mode.SRC_ATOP)
                                        v.invalidate()
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        v.background.clearColorFilter()
                                        v.invalidate()
                                    }
                                }
                                false
                            }

                            binding?.llWikipediaBtn?.setOnClickListener {
                                val wikipediaUrl = breedInfo.wikipedia_url
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(wikipediaUrl)
                                startActivity(intent)
                            }



                        } else {
                            Log.e("MainActivity", "Response not successful")
                            makeToast("Response not successful [${response.code()}]")
                        }

                    }

                }
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}