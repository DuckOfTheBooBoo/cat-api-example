package com.arajdianaltaf.dogapiexample

import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.dogapiexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var subBreedSelectable = false

    private fun checkBreed(): Boolean {

        val breed: String = binding?.actvBreedType?.text.toString()
        val subBreed: String = binding?.actvSubBreedType?.text.toString()

        when {
            TextUtils.isEmpty(breed) -> {
                Toast.makeText(this, "Please select breed.", Toast.LENGTH_SHORT).show()
            }


            TextUtils.isEmpty(subBreed) && subBreedSelectable == false -> {
                Toast.makeText(this, "Please select sub breed.", Toast.LENGTH_SHORT).show()
            }

            else -> {

            }
        }

        return true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter = DogPhotoAdapter(Doggos.doggosList, this)
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
            if (checkBreed()) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}