package com.unltm.distance.ui.dialog.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R

abstract class PhotoPagerAdapter(
    private val itemCount: Int,
    private val photoTapListener: () -> Unit
) : RecyclerView.Adapter<PhotoPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPagerViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoPagerViewHolder(inflate).apply {
            photo.setOnPhotoTapListener { _, _, _ -> photoTapListener.invoke() }
        }
    }
    override fun getItemCount(): Int = itemCount
}