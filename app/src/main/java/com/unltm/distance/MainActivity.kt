package com.unltm.distance

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.unltm.distance.base.ProgramSplitter
import com.unltm.distance.base.contracts.dataStoreSetting
import com.unltm.distance.base.contracts.dp
import com.unltm.distance.base.contracts.getValueFlow
import com.unltm.distance.datasource.config.BaseConfig
import com.unltm.distance.ui.settings.SETTING_PROXY_ADDRESS
import com.unltm.distance.ui.settings.SETTING_PROXY_PORT
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var icon: ShapeableImageView
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            dataStoreSetting.getValueFlow(
                stringPreferencesKey(SETTING_PROXY_ADDRESS),
                BaseConfig.address
            ).collectLatest { BaseConfig.address = it }
        }
        lifecycleScope.launch {
            dataStoreSetting.getValueFlow(
                stringPreferencesKey(SETTING_PROXY_PORT),
                BaseConfig.port
            ).collectLatest { BaseConfig.port = it }
        }

        icon = ShapeableImageView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                verticalBias = 0.3f
            }
            setBackgroundResource(R.mipmap.ic_launcher)
            shapeAppearanceModel = ShapeAppearanceModel.builder().setAllCornerSizes(18f.dp).build()
        }
        progressBar = ProgressBar(this).apply {
            isVisible = false
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                verticalBias = 0.8f
            }
            indeterminateTintList = ColorStateList.valueOf(Color.WHITE)
        }
        setContentView(
            ConstraintLayout(this).apply {
                setBackgroundResource(R.color.background)
                addView(icon)
                addView(progressBar)
            }
        )
        BarUtils.setStatusBarVisibility(this, false)
        BarUtils.setNavBarVisibility(this, false)
    }

    override fun onResume() {
        super.onResume()
        MainScope().launch {
            delay(1_000)
            progressBar.isVisible = true
            ProgramSplitter(this@MainActivity)
        }
    }
}