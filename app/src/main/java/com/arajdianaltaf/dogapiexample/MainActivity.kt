package com.arajdianaltaf.dogapiexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.arajdianaltaf.dogapiexample.databinding.ActivityMainBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter = DogPhotoAdapter(Doggos.doggosList, this)
        binding?.rvImages?.adapter = adapter
        binding?.rvImages?.layoutManager = GridLayoutManager(this, 3)

        // Get breeds list
        val breedList = Constants.breedList
        val breedSelectAdapter = ArrayAdapter(this, R.layout.dropdown_item, breedList)
        binding?.actvBreedType?.setAdapter(breedSelectAdapter)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}