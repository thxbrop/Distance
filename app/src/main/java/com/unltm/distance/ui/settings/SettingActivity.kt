package com.unltm.distance.ui.settings

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.lifecycleScope
import coil.util.CoilUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface
import com.unltm.distance.R
import com.unltm.distance.base.contracts.*
import com.unltm.distance.components.dialog.ProgressDialog
import com.unltm.distance.databinding.ActivitySettingsBinding
import com.unltm.distance.ui.components.dialog.LoadingDialog
import com.unltm.distance.base.file.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Cache
import java.text.DecimalFormat

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var fork: Fork
    private lateinit var attach: SlidrInterface
    private var cache: Cache? = null

    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        attach = Slidr.attach(this)
        fork = (intent?.getSerializableExtra(SETTING_FORK) ?: Fork.Unknown) as Fork
        loadingDialog = LoadingDialog(this)
        when (fork) {
            is Fork.Notification -> initNotification()
            is Fork.PrivacySafe -> initPrivacySafe()
            is Fork.Theme -> initTheme()
            is Fork.Cache -> initCache()
            is Fork.Unknown -> onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initNotification() {
        binding.run {
            appBarLayout.initAppBar(R.string.setting_notify, onBackPressedDispatcher)
            lifecycleScope.apply {
                SwitchSetting(linear, R.string.setting_notify_shock) { checkbox ->
                    launch {
                        setBooleanDataSource(SETTING_VIBRATION, !checkbox.isChecked)
                    }
                }.also { checkbox ->
                    launch {
                        dataStoreSetting.getValueFlow(
                            booleanPreferencesKey(SETTING_VIBRATION),
                            false
                        ).collectLatest {
                            checkbox.setCheck(it)
                        }
                    }
                }
                SwitchSetting(
                    linear,
                    R.string.setting_notify_ring
                ) { switch ->
                    launch {
                        setBooleanDataSource(SETTING_RING, !switch.isChecked)
                    }
                }.also { checkbox ->
                    launch {
                        dataStoreSetting.getValueFlow(booleanPreferencesKey(SETTING_RING), true)
                            .collectLatest { checkbox.setCheck(it) }
                    }
                }

                TextSetting(
                    linear,
                    R.string.setting_notify_repeat,
                    arrayOf(
                        R.string.setting_notify_repeat_never,
                        R.string.setting_notify_repeat_5min,
                        R.string.setting_notify_repeat_10min,
                        R.string.setting_notify_repeat_30min,
                        R.string.setting_notify_repeat_1hour,
                        R.string.setting_notify_repeat_2hour,
                        R.string.setting_notify_repeat_4hour,
                    ),
                    true,
                ) {
                    showToast("Clicked Position is $it")
                }
            }

        }

    }

    private fun initPrivacySafe() {
        loadingDialog.show()
        lifecycleScope.apply {
//            ServerUtils.getUserById(LCUser.getCurrentUser().objectId) { user ->
//                loadingDialog.dismiss()
//                binding.apply {
//                    appBarLayout.initAppBar(R.string.setting_safe, onBackPressedDispatcher)
//                    TextSetting(
//                        linear,
//                        R.string.phone_number_visibility,
//                        arrayOf(
//                            R.string.everyone,
//                            R.string.only_contract,
//                            R.string.no_one,
//                        )
//                    ) {
//                        launch {
//                            ServerUtils.setPhoneVisibility(it)
//                        }
//                    }.run {
//                        user?.let {
//                            val phoneVisibility = ServerUtils.getPhoneVisibility(it)
//                            selectItem(phoneVisibility)
//                        }
//                    }
//                    TextSetting(
//                        linear,
//                        R.string.invite_permission,
//                        arrayOf(
//                            R.string.everyone,
//                            R.string.only_contract,
//                            R.string.no_one,
//                        )
//                    ) {
//                        launch {
//                            ServerUtils.setInvitePermission(it)
//                        }
//                    }.run {
//                        user?.let {
//                            val invitePermission = ServerUtils.getInvitePermission(it)
//                            selectItem(invitePermission)
//                        }
//                    }
//                }
//            }
        }
    }

    private fun initTheme() {
        binding.apply {
            appBarLayout.initAppBar(R.string.setting_theme, onBackPressedDispatcher)
            ThemeSpanSetting(
                this@SettingActivity,
                linear,
                arrayOf(
                    R.color.colorPrimary,
                    R.color.red,
                    R.color.blue_400,
                    R.color.yellow,
                )
            ) {
                showUnCompletedToast()
            }
            ProgressBarSetting(
                linear,
                R.string.list_max_speed,
                100,
                60
            ) {
                lifecycleScope.launch {
                    dataStoreSetting.setValue(intPreferencesKey(SETTING_THEME_LIST_MAX_SPEED), it)
                }
            }.also {
                lifecycleScope.launch {
                    dataStoreSetting.getValueFlow(
                        intPreferencesKey(SETTING_THEME_LIST_MAX_SPEED),
                        60
                    ).collectLatest { current ->
                        it.setProgress(current)
                    }
                }
            }

        }
    }

    private fun initCache() {
        cache = CoilUtils.createDefaultCache(this@SettingActivity)
        binding.apply {
            appBarLayout.initAppBar(R.string.setting_cache, onBackPressedDispatcher)
            //val conversation = CheckboxSetting(linear, R.string.cache_conversation, false)
            //val message = CheckboxSetting(linear, R.string.cache_message, false)
            val image = CheckboxSetting(linear, R.string.cache_image)
            val record = CheckboxSetting(linear, R.string.cache_voice)

            ProgressDialog(this@SettingActivity).apply {
                FloatingActionButton(this@SettingActivity).apply {
                    setBackgroundResource(R.color.background)
                    foregroundTintList =
                        ColorStateList.valueOf(context.colorRes(R.color.floating_background))
                    compatElevation = 2f.dp
                    setImageResource(R.drawable.ic_baseline_delete_24)
                    imageTintList = ColorStateList.valueOf(Color.WHITE)
                    layoutParams = ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        setMargins(16.dp)
                    }
                    setOnClickListener {
                        submit(0)
                        show()
                        if (image.isChecked()) {
                            cache?.evictAll()
                            image.setTitle(getString(R.string.cache_image))
                            image.setCheck(false)
                        }
                        submit(50)
                        if (record.isChecked()) {
                            com.blankj.utilcode.util.FileUtils.deleteAllInDir(
                                FileUtils.RecordPath + "/"
                            )
                            record.setTitle(getString(R.string.cache_voice))
                            record.setCheck(false)
                        }
                        submit(100)
                    }
                    root.addView(this)
                }
                attach.lock()
                show()
                lifecycleScope.launch(Dispatchers.IO) {
                    val bitmapSize = cache?.size() ?: 0
                    image.setTitle("图片（${DecimalFormat("0.00").format(bitmapSize / 1024 / 1024f)}MB）")
                    submit(50)
                    val listFilesInDir = com.blankj.utilcode.util.FileUtils.listFilesInDir(
                        FileUtils.RecordPath + "/"
                    )
                    var total = 0L
                    listFilesInDir.forEach {
                        total += it.length()
                    }
                    record.setTitle("语音消息（${DecimalFormat("0.00").format(total / 1024f)}KB）")
                    submit(100)
                }
            }
        }
    }

    private suspend fun setBooleanDataSource(key: String, value: Boolean) {
        dataStoreSetting.setValue(booleanPreferencesKey(key), value)
    }

    override fun onDestroy() {
        super.onDestroy()
        cache?.close()
    }

}