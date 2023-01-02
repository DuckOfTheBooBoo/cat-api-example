package com.arajdianaltaf.catapiexample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arajdianaltaf.catapiexample.databinding.ItemImagesBinding
import com.bumptech.glide.Glide

class CatPhotoAdapter(val context: Context): RecyclerView.Adapter<CatPhotoAdapter.DogPhotoViewHolder>() {

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
            Glide.with(context)
                .load(item)
                .centerCrop()
                .override(300, 300)
                .placeholder(R.drawable.dog_api_logo)
                .into(itemBinding.ivDogImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogPhotoViewHolder {
        return DogPhotoViewHolder(ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DogPhotoViewHolder, position: Int) {
        val cats = images[position]
        holder.bindItem(cats)
    }

    override fun getItemCount(): Int {
        return images.size
    }


}