package com.unltm.distance.ui.components.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.unltm.distance.R
import com.unltm.distance.adapter.bottomsheet.SettingItem
import com.unltm.distance.base.contracts.showToast
import com.unltm.distance.base.file.Files
import com.unltm.distance.fragment.listbottomsheet.ListBottomSheet
import com.unltm.distance.ui.components.photoview.PhotoView

class BitmapDialog(
    context: Context,
    private val fragmentManager: FragmentManager,
    vararg bitmap: Bitmap?,
) : Dialog(context, R.style.QRCodeDialog) {
    private val src: Array<out Bitmap?> = bitmap

    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_x_photo)
        viewPager = findViewById(R.id.vp2)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        viewPager.registerOnPageChangeCallback(CALLBACK)
        viewPager.adapter = object : RecyclerView.Adapter<PagerViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
                val inflate =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
                return PagerViewHolder(inflate).apply {
                    photo.setOnPhotoTapListener { _, _, _ -> dismiss() }
                }
            }

            override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
                holder.photo.load(src[position])
                holder.photo.setOnLongClickListener {
                    ListBottomSheet(
                        "图片预览",
                        listOf(
                            SettingItem(
                                R.drawable.ic_baseline_save_24,
                                R.string.cd_save
                            ) {
                                val icon: Int
                                src[position]?.let {
                                    val textId = if (Files.saveImage(it)) {
                                        icon = R.drawable.live_state_primary
                                        R.string.success_save
                                    } else {
                                        icon = R.drawable.live_state_warn
                                        R.string.error_save
                                    }
                                    context.showToast(textId, icon)
                                }
                            }
                        )
                    ).show(fragmentManager, "show_picture")
                    false
                }
            }

            override fun getItemCount(): Int = src.size
        }
    }

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: PhotoView = itemView.findViewById(R.id.photo)
    }

    object CALLBACK : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPosition = position
        }
    }

    companion object {
        private var currentPosition = 0
    }

}