package com.unltm.distance.ui.components.dialog.image

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentManager
import coil.load
import com.unltm.distance.R
import com.unltm.distance.adapter.bottomsheet.SettingItem
import com.unltm.distance.base.contracts.showToast
import com.unltm.distance.base.file.Files
import com.unltm.distance.fragment.ListBottomSheetDialog
import com.unltm.distance.ui.components.FastBlurUtil
import java.util.*

class ImageDialog(
    context: Context,
    private val fragmentManager: FragmentManager,
    vararg bitmap: Bitmap?,
) : BaseImageDialog(context) {
    private val src: Array<out Bitmap?> = bitmap
    private var isBlur = BooleanArray(src.size).also {
        Arrays.fill(it, false)
    }

    override fun onStart() {
        super.onStart()
        viewPager.adapter = object : PhotoPagerAdapter(src.size, { dismiss() }) {
            override fun onBindViewHolder(holderPhoto: PhotoPagerViewHolder, position: Int) {
                holderPhoto.photo.load(src[position])
                holderPhoto.photo.setOnLongClickListener {
                    ListBottomSheetDialog(
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
                            },
                            SettingItem(
                                if (!isBlur[position]) R.drawable.ic_baseline_blur_on_24
                                else R.drawable.ic_baseline_blur_off_24,
                                if (!isBlur[position]) R.string.cd_blur
                                else R.string.cd_unBlur,
                                badge = R.string.badge_beta
                            ) {
                                if (!isBlur[position]) {
                                    holderPhoto.photo.load(
                                        FastBlurUtil.doBlur(
                                            src[position],
                                            16,
                                            false
                                        )
                                    )
                                } else {
                                    holderPhoto.photo.load(src[position])
                                }
                                isBlur[position] = !isBlur[position]
                            }
                        )
                    ).show(fragmentManager, "show_picture")
                    false
                }
            }
        }
    }
}