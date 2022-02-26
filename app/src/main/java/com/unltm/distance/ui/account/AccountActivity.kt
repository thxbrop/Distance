package com.unltm.distance.ui.account

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.imageview.ShapeableImageView
import com.r0adkll.slidr.Slidr
import com.unltm.distance.R
import com.unltm.distance.base.collection.Launchers
import com.unltm.distance.base.contracts.*
import com.unltm.distance.databinding.ActivityAccountBinding
import com.unltm.distance.room.entity.UserRich
import com.unltm.distance.ui.components.dialog.BitmapDialog
import com.unltm.distance.ui.components.dialog.DialogUtils
import com.unltm.distance.ui.components.dialog.ProgressDialog
import com.unltm.distance.ui.edit.EditActivity
import com.unltm.distance.ui.edit.EditActivity.Companion.TYPE
import com.unltm.distance.ui.edit.EditActivity.Companion.USER_RICH
import com.unltm.distance.ui.settings.Fork
import com.unltm.distance.ui.settings.SETTING_FORK
import com.unltm.distance.ui.settings.SettingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var selectMediaLauncher: ActivityResultLauncher<String>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var userRich: UserRich
    private var headPictures = mutableListOf<Bitmap>()

    private var uploadedBitmap: Bitmap? = null

    //WIDGETS
    private lateinit var widgetUsername: TextView
    private lateinit var widgetPhone: TextView
    private lateinit var widgetIntroduce: TextView
    private lateinit var widgetAvatar: ShapeableImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = AccountViewModel.INSTANCE
        Slidr.attach(this)

        progressDialog = ProgressDialog(this)
        binding.init()
        initObserver()
        initLaunchers()
    }

    private fun initLaunchers() {
        selectMediaLauncher = Launchers.selectMediaLauncher(this) {
            widgetAvatar.setImageURI(it)
            widgetAvatar.buildDrawingCache()
            uploadedBitmap = (widgetAvatar.drawable as BitmapDrawable).bitmap
            viewModel.uploadHeadPicture(uploadedBitmap!!)
        }
    }


    private fun ActivityAccountBinding.init() {
        activityAccountName.let {
            it.title.text = getString(R.string.account)
            it.key.text = getString(R.string.information_base_username)
            it.body.setOnClickListener {
                startActivity(Intent(this@AccountActivity, EditActivity::class.java).apply {
                    putExtra(TYPE, EditActivity.Type.NAME)
                    putExtra(USER_RICH, userRich)
                })
            }
        }
        activityAccountPhone.let {
            it.key.text = getString(R.string.information_base_phone)
            it.body.setOnClickListener {
                startActivity(Intent(this@AccountActivity, EditActivity::class.java).apply {
                    putExtra(TYPE, EditActivity.Type.PHONE)
                    putExtra(USER_RICH, userRich)
                })
            }
        }
        activityAccountDescription.key.text = getString(R.string.information_base_description)
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
                    R.string.dialog_callback_position_button to DialogInterface.OnClickListener { _, _ ->
                        //TODO Logout
                    }
                )
            }
        }
        activityUserFloating.setOnClickListener {
            selectMediaLauncher.launch("image/*")
        }
        activityUserImage.setOnClickListener {
            BitmapDialog(this@AccountActivity, supportFragmentManager)
        }

        widgetUsername = activityAccountName.value
        widgetPhone = activityAccountPhone.value
        widgetIntroduce = activityAccountDescription.value
        widgetAvatar = activityUserImage
    }

    private fun initObserver() {
        viewModel.currentUserLive.observe(this) { result ->
            result.success?.let {
                var username = it.first().username
                if (username.isBlank()) username = getString(R.string.empty_username)
                widgetUsername.text = username
                viewModel.getRichInfo(it.first())
            }
            result.error?.let {
                showErrorToast(it.localizedMessage)
            }
        }
        viewModel.richUserResult.observe(this) { result ->
            result.success?.let { updateInformation(it) }
            result.error?.let { showErrorToast(it.localizedMessage) }
        }
//        viewModel.uploadHeadPictureLive.observe(this) { task ->
//            task?.addOnProgressListener {
//                val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
//                progressDialog.show()
//                progressDialog.submit(progress.toInt())
//            }
//            task?.addOnSuccessListener {
//                val result = getString(R.string.success_upload)
//                headPictures.add(uploadedBitmap!!)
//                progressDialog.dismiss()
//                Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).show()
//            }
//            task?.addOnFailureListener {
//                val result = getString(R.string.error_upload)
//                progressDialog.dismiss()
//                Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).show()
//            }
//        }
        viewModel.getCurrentUser()
    }

    private fun updateInformation(userRich: UserRich) {
        this.userRich = userRich
        widgetPhone.text =
            if (userRich.phoneNumber.isNull) getString(R.string.no_phone_number)
            else userRich.phoneNumber.toString()
        widgetIntroduce.text = userRich.introduce
        lifecycleScope.launch(Dispatchers.IO) {
            headPictures = userRich.avatars.map { it.toBitmap() }.toMutableList()
        }
    }
}

