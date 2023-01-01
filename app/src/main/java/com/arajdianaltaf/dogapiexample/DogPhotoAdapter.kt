package com.arajdianaltaf.dogapiexample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arajdianaltaf.dogapiexample.databinding.ItemImagesBinding
import com.bumptech.glide.Glide

class DogPhotoAdapter(val context: Context): RecyclerView.Adapter<DogPhotoAdapter.DogPhotoViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var images: List<String>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    inner class DogPhotoViewHolder(val itemBinding: ItemImagesBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item: String) {
//            itemBinding.ivDogImage.contentDescription = item.breed
            Glide.with(context)
                .load(item)
                .centerCrop()
                .override(300, 300)
                .into(itemBinding.ivDogImage)

//            itemBinding.ivDogImage.setOnClickListener {
//                Toast.makeText(context, itemBinding.ivDogImage.contentDescription, Toast.LENGTH_SHORT).show()
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogPhotoViewHolder {
        return DogPhotoViewHolder(ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DogPhotoViewHolder, position: Int) {
        val dogs = images[position]
        holder.bindItem(dogs)
    }

    override fun getItemCount(): Int {
        return images.size
    }


}