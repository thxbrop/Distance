package com.unltm.distance.ui.dialog.image

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import com.unltm.distance.R
import com.unltm.distance.adapter.bottomsheet.SettingItem
import com.unltm.distance.base.contracts.loadHTTPS
import com.unltm.distance.base.contracts.toast
import com.unltm.distance.base.file.Files
import com.unltm.distance.ui.dialog.ListBottomSheetDialog

class ImageUrlDialog(
    context: Context,
    private val fragmentManager: FragmentManager,
    vararg url: String?,
) : BaseImageDialog(context) {
    private val src: Array<out String?> = url
    private val bitmaps = mutableListOf<Bitmap>()
    override fun onStart() {
        super.onStart()
        viewPager.adapter = object : PhotoPagerAdapter(src.size, { dismiss() }) {
            override fun onBindViewHolder(holderPhoto: PhotoPagerViewHolder, position: Int) {
                holderPhoto.photo.loadHTTPS(src[position]) {
                    target {
                        holderPhoto.photo.setImageDrawable(it)
                        bitmaps.add(position, it.toBitmap())
                    }
                }
                holderPhoto.photo.setOnLongClickListener {
                    ListBottomSheetDialog(
                        "图片预览",
                        listOf(
                            SettingItem(
                                R.drawable.ic_baseline_save_24,
                                R.string.cd_save
                            ) {
                                var icon: Int
                                src[position]?.let {
                                    val textId: Int = if (Files.saveImage(bitmaps[position])) {
                                        icon = R.drawable.live_state_primary
                                        R.string.success_save
                                    } else {
                                        icon = R.drawable.live_state_warn
                                        R.string.error_save
                                    }
                                    context.toast(textId, icon)
                                }
                            }
                        )
                    ).show(fragmentManager, "show_picture")
                    false
                }
            }
        }
    }
}