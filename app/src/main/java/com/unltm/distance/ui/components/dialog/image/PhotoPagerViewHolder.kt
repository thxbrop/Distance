package com.unltm.distance.ui.components.dialog.image

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.ui.components.photoview.PhotoView

class PhotoPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val photo: PhotoView = itemView.findViewById(R.id.photo)
}
