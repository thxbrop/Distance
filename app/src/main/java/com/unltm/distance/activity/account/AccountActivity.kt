package com.unltm.distance.activity.account

import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.r0adkll.slidr.Slidr
import com.unltm.distance.R
import com.unltm.distance.activity.edit.EditActivity
import com.unltm.distance.activity.settings.Fork
import com.unltm.distance.activity.settings.SETTING_FORK
import com.unltm.distance.activity.settings.SettingActivity
import com.unltm.distance.activity.vm
import com.unltm.distance.base.Result
import com.unltm.distance.base.collection.Items
import com.unltm.distance.base.collection.Launchers
import com.unltm.distance.base.contracts.*
import com.unltm.distance.databinding.ActivityAccountBinding
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.dialog.DialogUtils
import com.unltm.distance.ui.dialog.ListBottomSheetDialog
import com.unltm.distance.ui.dialog.ProgressDialog
import com.unltm.distance.ui.dialog.image.ImageDialog

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var selectMediaLauncher: ActivityResultLauncher<String>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var progressDialog: ProgressDialog

    //WIDGETS
    private lateinit var widgetUsername: TextView
    private lateinit var widgetPhone: TextView
    private lateinit var widgetIntroduce: TextView
    private lateinit var widgetAvatar: ShapeableImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = AccountViewModel()

        Slidr.attach(this)

        progressDialog = ProgressDialog(this, this)
        binding.init()
        initObserver()
        initLaunchers()
    }

    private fun initLaunchers() {
        selectMediaLauncher = Launchers.selectMediaLauncher(this) {
            widgetAvatar.setImageURI(it)
            vm.currentUser?.id?.let { s -> viewModel.uploadHeadPicture(s, it) }
        }
        requestPermissionLauncher = Launchers.requestPermissionLauncher(this)
    }

    private fun ActivityAccountBinding.init() {
        activityAccountName.let {
            it.title.text = getString(R.string.account)
            it.key.text = getString(R.string.information_base_username)
            it.body.setOnClickListener {
                withNotNull(viewModel.informationResult.value?.success) { user ->
                    val accountList = Items.accountItems(
                        this@AccountActivity,
                        supportFragmentManager,
                        user
                    )
                    ListBottomSheetDialog(
                        user.username,
                        accountList
                    ).show(supportFragmentManager)
                }

            }
        }
        activityAccountPhone.let {
            it.key.text = getString(R.string.information_base_phone)
            it.body.setOnClickListener {
                withNotNull(viewModel.informationResult.value?.success) { user ->
                    val accountList = Items.phoneItems(
                        this@AccountActivity,
                        requestPermissionLauncher,
                        user,
                        true
                    )
                    ListBottomSheetDialog(
                        user.phoneNumber.formatPhoneNumber() ?: getString(R.string.no_phone_number),
                        accountList
                    ).show(supportFragmentManager)
                }
            }
        }
        activityAccountDescription.let {
            it.key.text = getString(R.string.information_base_description)
            it.body.setOnClickListener {
                withNotNull(viewModel.informationResult.value?.success) { user ->
                    EditActivity.start(this@AccountActivity, EditActivity.Type.INTRODUCE, user)
                }
            }
        }
        activityAccountNotify.let {
            it.title.text = getString(R.string.setting)
            it.key.text = getString(R.string.setting_notify)
            it.icon.setImageResource(R.drawable.ic_baseline_music_note_24)
            it.root.setOnClickListener {
                startActivity<SettingActivity>(SETTING_FORK to Fork.Notification)
            }
        }
        activityAccountSafe.let {
            it.key.text = getString(R.string.setting_safe)
            it.icon.setImageResource(R.drawable.ic_baseline_lock_24)
            it.root.setOnClickListener {
                startActivity<SettingActivity>(SETTING_FORK to Fork.PrivacySafe)
            }
        }
        activityAccountTheme.let {
            it.key.text = getString(R.string.setting_theme)
            it.icon.setImageResource(R.drawable.ic_baseline_palette_24)
            it.root.setOnClickListener {
                startActivity<SettingActivity>(SETTING_FORK to Fork.Theme)
            }
        }
        activityAccountCache.let {
            it.key.text = getString(R.string.setting_cache)
            it.icon.setImageResource(R.drawable.ic_baseline_delete_24)
            it.root.setOnClickListener {
                startActivity<SettingActivity>(SETTING_FORK to Fork.Cache)
            }
        }
        activityAccountProxy.let {
            it.key.setTextResource(R.string.setting_proxy)
            it.icon.setImageResource(R.drawable.ic_baseline_dns_24)
            it.body.setOnClickListener {
                startActivity<SettingActivity>(SETTING_FORK to Fork.Proxy)
            }
        }
        activityAccountLogout.let {
            it.key.text = getString(R.string.setting_logout)
            it.icon.setImageResource(R.drawable.ic_baseline_logout_24)
            it.body.setOnClickListener {
                DialogUtils.showCallbackDialog(
                    this@AccountActivity,
                    getString(R.string.setting_logout),
                    "此操作会删除所有本地用户数据",
                    R.string.dialog_callback_position_button to { vm.logoutAllAccount() }
                )
            }
        }
        activityUserFloating.setOnClickListener {
            selectMediaLauncher.launch("image/*")
        }
        activityUserImage.setOnClickListener {
            ImageDialog(this@AccountActivity, supportFragmentManager)
        }

        widgetUsername = activityAccountName.value
        widgetPhone = activityAccountPhone.value
        widgetIntroduce = activityAccountDescription.value
        widgetAvatar = activityUserImage
    }

    private fun initObserver() {
        vm.accountLive.observe(this) { result ->
            result?.let {
                if (it.isNotEmpty()) {
                    var username = it.first().username
                    if (username.isBlank()) username = getString(R.string.empty_username)
                    widgetUsername.text = username
                    viewModel.getInformation(it.first())
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        viewModel.informationResult.observe(this) { result ->
            result.success?.let { updateInformation(it) }
            result.error?.let { errorToast(it.localizedMessage) }
        }
        viewModel.headPicturesLive.observe(this) { result ->
            result.data?.let {
                widgetAvatar.load(it.firstOrNull())
            }
            result.error?.let { errorToast(it.message) }
        }
        viewModel.uploadedHeadPictureLive.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Snackbar.make(
                        this,
                        widgetAvatar,
                        "上传中：${result.data.progress}%",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
                is Result.Error -> {
                    errorToast(result.exception.message)
                }
            }
        }
        vm.getAccounts()
    }

    private fun updateInformation(user: User) {
        widgetPhone.text =
            if (user.phoneNumber.isNull) getString(R.string.no_phone_number)
            else user.phoneNumber.formatPhoneNumber()
        widgetIntroduce.text =
            user.introduce.ifBlank { getString(R.string.no_description) }
        viewModel.getHeadPictures(user.avatars)
    }
}

