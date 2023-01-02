package com.arajdianaltaf.catapiexample

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.catapiexample.data.CatResponseImageItem
import com.arajdianaltaf.catapiexample.databinding.ActivityMainBinding
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


        val jsonRead = gson.fromJson(reader, Array<CatResponseImageItem>::class.java)
        val breedList = mutableListOf<String>()

        for (cat in jsonRead) {
            breedList.add(cat.id)
        }

        withContext(Dispatchers.Main) {
            val breedSelectAdapter =
                ArrayAdapter(this@MainActivity, R.layout.dropdown_item, breedList)
            binding?.actvBreedType?.setAdapter(breedSelectAdapter)
            binding?.actvBreedType?.isClickable = true
            binding?.actvBreedType?.isFocusable = true
        }

    }

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

        // Button OnClickListener
        binding?.btnRequest?.setOnClickListener {

            val breed: String = binding?.actvBreedType?.text.toString()

            when {
                TextUtils.isEmpty(breed) -> {
                    makeToast("Please select breed.")
                }

                else -> {
                    Log.i(Constants.TAG_MAINACTIVITY, "All entry are valid.")

                    lifecycleScope.launchWhenCreated {
                        val response = try {
                            RetrofitClient.api.getCatImagesByBreed(
                                api_key = Constants.API_KEY,
                                breedId = breed,
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

                            binding?.tvBreedDesc?.text = response.body()!![0].breeds[0].description


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