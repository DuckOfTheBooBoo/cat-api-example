package com.arajdianaltaf.catapiexample

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arajdianaltaf.catapiexample.data.CatResponseImageItem
import com.arajdianaltaf.catapiexample.databinding.ImagePopUpBinding
import com.arajdianaltaf.catapiexample.databinding.ItemImagesBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

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

    private fun popUpPhotoWindow(photoUrl: String) {
        val popUpWindow = Dialog(context)
        val binding: ImagePopUpBinding = ImagePopUpBinding.inflate(LayoutInflater.from(context))
        popUpWindow.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Glide.with(context)
            .load(photoUrl)
            .fitCenter()
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    Log.e("GlidePopUpWindow", "Load Failed on ($target): ", e)

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    Log.i("GlidePopUpWindow", "Load success from ($dataSource)")
                    return false
                }

            })
            .override(700,700)
            .error(R.drawable.ic_image_load_failed)
            .into(binding.ivPhoto)


        binding.ivCloseBtn.setOnClickListener {

            popUpWindow.dismiss()

        }
        popUpWindow.requestWindowFeature(Window.FEATURE_NO_TITLE)
        popUpWindow.setContentView(binding.root)
        popUpWindow.show()
    }

    inner class DogPhotoViewHolder(val itemBinding: ItemImagesBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item: CatResponseImageItem) {


            Glide.with(context)
                .load(item.url)
                .fitCenter()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemBinding.pbLoading.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemBinding.pbLoading.visibility = View.GONE
                        return false
                    }

                })
                .error(R.drawable.ic_image_load_failed)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(300, 300)
                .into(itemBinding.ivDogImage)

            itemBinding.ivDogImage.setOnClickListener {
                Log.i("CatPhotoAdapter", "Image Clicked")
                popUpPhotoWindow(item.url)
            }


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