package com.arajdianaltaf.catapiexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.catapiexample.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private fun makeToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, length).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter = CatPhotoAdapter(this)
        binding?.rvImages?.adapter = adapter
        binding?.rvImages?.layoutManager = GridLayoutManager(this, 3)

        // Get breeds list
        val breedSelectAdapter = ArrayAdapter(this, R.layout.dropdown_item, Constants.breedList)

        binding?.actvBreedType?.setAdapter(breedSelectAdapter)


        binding?.actvBreedType?.doAfterTextChanged {

            // Set SubBreed value to empty
            binding?.actvSubBreedType?.text = null

            // Get Breed value for key
            val breedText = binding?.actvBreedType?.text.toString()

            if (Constants.subBreedMap.containsKey(breedText)) {

                Log.i(Constants.TAG_KEY_EXIST, "$breedText in subBreedMap")
                binding?.actvSubBreedType?.isFocusable = true
                val subBreedSelectAdapter = ArrayAdapter(this, R.layout.dropdown_item, Constants.subBreedMap[breedText]!!)
                binding?.actvSubBreedType?.setAdapter(subBreedSelectAdapter)

            } else {

                Log.i(Constants.TAG_KEY_NOT_EXIST, breedText)
                binding?.actvSubBreedType?.text = null
                binding?.actvSubBreedType?.setAdapter(null)
                binding?.actvSubBreedType?.isFocusable = false

            }
        }

        // Button OnClickListener
        binding?.btnRequest?.setOnClickListener {

            val breed: String = binding?.actvBreedType?.text.toString()
            val subBreed: String = binding?.actvSubBreedType?.text.toString()

            when {
                TextUtils.isEmpty(breed) -> {
                    makeToast("Please select breed.")
                }


                TextUtils.isEmpty(subBreed) && Constants.subBreedMap.containsKey(breed) -> {
                    Log.e(Constants.TAG_SUB_BREED_EMPTY, "User has not yet selected the available sub breeds.")
                    makeToast("Please select sub breed.")
                }

                else -> {
                    Log.i("ENRTY VALID", "Success")

                    lifecycleScope.launchWhenCreated {
                        val response = try {
                            if (Constants.subBreedMap.containsKey(breed)) {
                                RetrofitClient.api.getDogImagesBySubBreed(breed = breed, subBreed = subBreed)
                            } else {
                                RetrofitClient.api.getDogImagesByBreed(breed = breed)
                            }

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
                            adapter.images = response.body()!!.message

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