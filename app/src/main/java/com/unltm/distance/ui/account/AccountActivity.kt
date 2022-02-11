package com.unltm.distance.ui.account

import android.content.DialogInterface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.r0adkll.slidr.Slidr
import com.unltm.distance.R
import com.unltm.distance.base.collection.Launchers
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.base.contracts.startActivity
import com.unltm.distance.components.dialog.ProgressDialog
import com.unltm.distance.databinding.ActivityAccountBinding
import com.unltm.distance.ui.settings.Fork
import com.unltm.distance.ui.settings.SETTING_FORK
import com.unltm.distance.ui.settings.SettingActivity
import com.unltm.distance.ui.components.dialog.DialogUtils

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var selectMediaLauncher: ActivityResultLauncher<String>
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = AccountViewModel.getInstance()
        Slidr.attach(this)

        progressDialog = ProgressDialog(this)
        binding.init()
        initObserver()
        initLaunchers()
    }

    private fun initLaunchers() {
        selectMediaLauncher = Launchers.selectMediaLauncher(this) {
            binding.activityUserImage.setImageURI(it)
            binding.activityUserImage.buildDrawingCache()
            val bitmap = (binding.activityUserImage.drawable as BitmapDrawable).bitmap
            viewModel.uploadHeadPicture(bitmap)
        }
    }


    private fun ActivityAccountBinding.init() {
        activityAccountName.title.text = getString(R.string.account)
        activityAccountName.key.text = getString(R.string.information_base_username)
        activityAccountPhone.key.text = getString(R.string.information_base_phone)
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
    }

    private fun initObserver() {
        viewModel.currentUserLive.observe(this) { result ->
            result.success?.let {
                binding.apply {
                    var username = it.first().username
                    if (username.isNullOrBlank()) username = getString(R.string.empty_username)
                    activityAccountName.value.text = username
                }
            }
            result.error?.let {
                showErrorToast(it.localizedMessage)
            }
        }
        viewModel.uploadHeadPictureLive.observe(this) { task ->
            task.addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                if (!progressDialog.isShowing) progressDialog.show()
                progressDialog.submit(progress.toInt())
            }
            task.addOnSuccessListener {
                val result = getString(R.string.task_success)
                if (progressDialog.isShowing) progressDialog.dismiss()
                Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).show()
            }
            task.addOnFailureListener {
                val result = getString(R.string.error_task)
                if (progressDialog.isShowing) progressDialog.dismiss()
                Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.getCurrentUser()
    }
}

