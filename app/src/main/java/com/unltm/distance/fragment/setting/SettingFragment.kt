package com.unltm.distance.fragment.setting

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.util.CoilUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unltm.distance.R
import com.unltm.distance.activity.NavController
import com.unltm.distance.activity.getNavController
import com.unltm.distance.activity.settings.Fork
import com.unltm.distance.activity.settings.components.*
import com.unltm.distance.application
import com.unltm.distance.base.contracts.*
import com.unltm.distance.base.file.Files
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.FragmentSettingsBinding
import com.unltm.distance.datasource.config.BaseConfig
import com.unltm.distance.ui.dialog.LoadingDialog
import com.unltm.distance.ui.dialog.ProgressDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import java.text.DecimalFormat

class SettingFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var fork: Fork
    private lateinit var loadingDialog: LoadingDialog
    private var cache: Cache? = null
    private val binding by viewBindings(FragmentSettingsBinding::bind)
    private lateinit var navController: NavController

    companion object {

        private const val SETTING_RING = "ring"
        private const val SETTING_VIBRATION = "vibration"
        private const val SETTING_THEME_LIST_MAX_SPEED = "list_max_speed"

        const val SETTING_PROXY_ADDRESS = "setting_proxy_address"
        const val SETTING_PROXY_PORT = "setting_proxy_port"
        private const val SETTING_FORK = "setting_fork"
        fun newInstance(fork: Fork) = SettingFragment().apply {
            arguments = Bundle().apply { putSerializable(SETTING_FORK, fork) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = getNavController()
        fork = arguments?.getSerializable(SETTING_FORK) as Fork
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(application)
        when (fork) {
            is Fork.Notification -> initNotification()
            is Fork.PrivacySafe -> initPrivacySafe()
            is Fork.Theme -> initTheme()
            is Fork.Cache -> initCache()
            is Fork.Proxy -> initProxy()
        }
    }

    private fun initProxy() {
        binding.run {
            navController.updateAppBar(getString(R.string.setting_proxy), null)
            val addressSetting =
                EditTextSetting(linear, R.string.setting_proxy_address) { address ->
                    BaseConfig.address = address
                    viewLifecycleOwner.lifecycleScope.launch {
                        requireActivity().dataStoreSetting.setValue(
                            stringPreferencesKey(SETTING_PROXY_ADDRESS),
                            address
                        )
                    }
                }
            val portSetting =
                EditTextSetting(linear, R.string.setting_proxy_port) { port ->
                    BaseConfig.port = port

                    viewLifecycleOwner.lifecycleScope.launch {
                        requireActivity().dataStoreSetting.setValue(
                            stringPreferencesKey(SETTING_PROXY_PORT),
                            port
                        )
                    }
                }
            addressSetting.setText(BaseConfig.address)
            portSetting.setText(BaseConfig.port)

            viewLifecycleOwner.lifecycleScope.launch {
                application.dataStoreSetting.getValueFlow(
                    stringPreferencesKey(SETTING_PROXY_PORT),
                    BaseConfig.port
                ).collectLatest {
                    withContext(Dispatchers.Main) {
                        portSetting.setText(it)
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                application.dataStoreSetting.getValueFlow(
                    stringPreferencesKey(SETTING_PROXY_ADDRESS),
                    BaseConfig.address
                ).collectLatest {
                    withContext(Dispatchers.Main) {
                        addressSetting.setText(it)
                    }
                }
            }
        }
    }

    private fun initNotification() {
        binding.run {
            navController.updateAppBar(getString(R.string.setting_notify), null)
            viewLifecycleOwner.lifecycleScope.apply {
                SwitchSetting(linear, R.string.setting_notify_shock) { checkbox ->
                    launch {
                        setBooleanDataSource(
                            SETTING_VIBRATION,
                            !checkbox.isChecked
                        )
                    }
                }.also { checkbox ->
                    launch {
                        application.dataStoreSetting.getValueFlow(
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
                        setBooleanDataSource(
                            SETTING_RING,
                            !switch.isChecked
                        )
                    }
                }.also { checkbox ->
                    launch {
                        application.dataStoreSetting.getValueFlow(
                            booleanPreferencesKey(SETTING_RING),
                            true
                        )
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
                    toast("Clicked Position is $it")
                }
            }

        }

    }

    private fun initPrivacySafe() {
        loadingDialog.show()
        viewLifecycleOwner.lifecycleScope.apply {
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
            navController.updateAppBar(getString(R.string.setting_theme), null)
            ThemeSpanSetting(
                viewLifecycleOwner,
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
                viewLifecycleOwner.lifecycleScope.launch {
                    application.dataStoreSetting.setValue(
                        intPreferencesKey(SETTING_THEME_LIST_MAX_SPEED),
                        it
                    )
                }
            }.also {
                viewLifecycleOwner.lifecycleScope.launch {
                    application.dataStoreSetting.getValueFlow(
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
        cache = CoilUtils.createDefaultCache(application)
        binding.apply {
            navController.updateAppBar(getString(R.string.setting_cache), null)
            //val conversation = CheckboxSetting(linear, R.string.cache_conversation, false)
            //val message = CheckboxSetting(linear, R.string.cache_message, false)
            val image = CheckboxSetting(linear, R.string.cache_image)
            val record = CheckboxSetting(linear, R.string.cache_voice)

            ProgressDialog(viewLifecycleOwner, application).apply {
                FloatingActionButton(application).apply {
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
                                Files.RecordPath + "/"
                            )
                            record.setTitle(getString(R.string.cache_voice))
                            record.setCheck(false)
                        }
                        submit(100)
                    }
                    root.addView(this)
                }
                show()
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val bitmapSize = cache?.size() ?: 0
                    image.setTitle("图片（${DecimalFormat("0.00").format(bitmapSize / 1024 / 1024f)}MB）")
                    submit(50)
                    val listFilesInDir = com.blankj.utilcode.util.FileUtils.listFilesInDir(
                        Files.RecordPath + "/"
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
        requireActivity().dataStoreSetting.setValue(booleanPreferencesKey(key), value)
    }

    override fun onDestroy() {
        super.onDestroy()
        cache?.close()
    }
}