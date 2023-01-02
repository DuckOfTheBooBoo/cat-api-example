package com.arajdianaltaf.catapiexample

import android.content.res.AssetFileDescriptor
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.catapiexample.data.CatResponseItem
import com.arajdianaltaf.catapiexample.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
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
                    Log.i("ENRTY VALID", "Success")

//                    lifecycleScope.launchWhenCreated {
//                        val response = try {
//                            if (Constants.subBreedMap.containsKey(breed)) {
//                                RetrofitClient.api.getDogImagesBySubBreed(breed = breed, subBreed = subBreed)
//                            } else {
//                                RetrofitClient.api.getDogImagesByBreed(breed = breed)
//                            }
//
//                        } catch (e: IOException) {
//                            Log.e("MainActivity", "$e, you might not have internet connection")
//                            makeToast("$e, you might not have internet connection")
//                            return@launchWhenCreated
//
//                        } catch (e: HttpException) {
//                            Log.e("MainActivity", "$e, unexpected response")
//                            makeToast("$e, unexpected response")
//                            return@launchWhenCreated
//
//                        }
//
//                        if (response.isSuccessful && response.body() != null) {
//                            adapter.images = response.body()!!.message
//
//                        } else {
//                            Log.e("MainActivity", "Response not successful")
//                            makeToast("Response not successful [${response.code()}]")
//                        }
//
//                    }

                }
            }

        }



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}