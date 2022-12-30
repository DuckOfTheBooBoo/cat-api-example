package com.arajdianaltaf.dogapiexample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arajdianaltaf.dogapiexample.databinding.ItemImagesBinding
import com.bumptech.glide.Glide

class DogPhotoAdapter(private val dog: List<DogDummy>, val context: Context): RecyclerView.Adapter<DogPhotoAdapter.DogPhotoViewHolder>() {


    inner class DogPhotoViewHolder(val itemBinding: ItemImagesBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item: DogDummy) {
            itemBinding.ivDogImage.contentDescription = item.breed
            Glide.with(context)
                .load(R.drawable.dog_api_logo)
                .centerCrop()
                .override(300, 300)
                .into(itemBinding.ivDogImage)

            itemBinding.ivDogImage.setOnClickListener {
                Toast.makeText(context, itemBinding.ivDogImage.contentDescription, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogPhotoViewHolder {
        return DogPhotoViewHolder(ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DogPhotoViewHolder, position: Int) {
        val dogs = dog[position]
        holder.bindItem(dogs)
    }

    override fun getItemCount(): Int {
        return dog.size
    }

}