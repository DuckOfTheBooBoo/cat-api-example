package com.arajdianaltaf.catapiexample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arajdianaltaf.catapiexample.data.CatResponseImageItem
import com.arajdianaltaf.catapiexample.databinding.ItemImagesBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CatPhotoAdapter(val context: Context): RecyclerView.Adapter<CatPhotoAdapter.DogPhotoViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<CatResponseImageItem>() {
        override fun areItemsTheSame(oldItem: CatResponseImageItem, newItem: CatResponseImageItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CatResponseImageItem, newItem: CatResponseImageItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var images: List<CatResponseImageItem>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    inner class DogPhotoViewHolder(val itemBinding: ItemImagesBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(item: CatResponseImageItem) {
            Glide.with(context)
                .load(item.url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
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