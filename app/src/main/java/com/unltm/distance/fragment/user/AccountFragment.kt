package com.unltm.distance.fragment.user

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import coil.load
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.PhoneUtils
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.unltm.distance.R
import com.unltm.distance.activity.NavController
import com.unltm.distance.activity.account.AccountViewModel
import com.unltm.distance.activity.edit.EditActivity
import com.unltm.distance.activity.getNavController
import com.unltm.distance.activity.settings.Fork
import com.unltm.distance.activity.vm
import com.unltm.distance.adapter.bottomsheet.SettingItem
import com.unltm.distance.base.Result
import com.unltm.distance.base.contracts.*
import com.unltm.distance.base.qrcode.Converter
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.FragmentAccountBinding
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.dialog.DialogUtils
import com.unltm.distance.ui.dialog.ListBottomSheetDialog
import com.unltm.distance.ui.dialog.image.ImageDialog

class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var widgetUsername: TextView
    private lateinit var widgetPhone: TextView
    private lateinit var widgetIntroduce: TextView
    private lateinit var widgetAvatar: ShapeableImageView

    private lateinit var supportFragmentManager: FragmentManager

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var navController: NavController

    private val viewModel: AccountViewModel by viewModels()
    private val binding by viewBindings(FragmentAccountBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager = requireActivity().supportFragmentManager
        enterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.slide_start)
        navController = getNavController()
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            activityAccountName.let {
                it.title.text = getString(R.string.account)
                it.key.text = getString(R.string.information_base_username)
                it.body.setOnClickListener {
                    withNotNull(viewModel.informationResult.value?.success) { user ->
                        val accountList = mutableListOf(
                            SettingItem(R.drawable.ic_baseline_edit_24, R.string.cd_edit) {
                                navController.openEditPage(
                                    EditActivity.SettingUseCase(
                                        type = EditActivity.Type.NAME,
                                        user = user,
                                        title = getString(R.string.edit_name)
                                    )
                                )
                            },
                            SettingItem(
                                R.drawable.ic_round_content_copy_24,
                                R.string.cd_copy_link,
                                R.string.badge_alpha
                            ),
                            SettingItem(R.drawable.ic_round_qr_code_24, R.string.qr_code) {
                                val bitmap = Converter.encryptUser(user)
                                ImageDialog(
                                    requireContext(),
                                    supportFragmentManager,
                                    bitmap
                                ).show()
                            })
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
                        val list = mutableListOf(
                            SettingItem(R.drawable.ic_baseline_edit_24, R.string.cd_edit)
                            {
                                navController.openEditPage(
                                    EditActivity.SettingUseCase(
                                        type = EditActivity.Type.PHONE,
                                        user = user,
                                        title = getString(R.string.edit_phone)
                                    )
                                )
                            },
                            SettingItem(R.drawable.ic_baseline_call_24, R.string.cd_call) {
                                val permission = Manifest.permission.CALL_PHONE
                                navController.requestPermissions(permission) { map ->
                                    if (map.getOrDefault(permission, false)) {
                                        PhoneUtils.call((user.phoneNumber ?: 0).toString())
                                    }
                                }
                            },
                            SettingItem(R.drawable.ic_round_content_copy_24, R.string.cd_copy) {
                                ClipboardUtils.copyText(user.phoneNumber.toString())
                                toast(R.string.success_copy)
                            },
                            SettingItem(
                                R.drawable.ic_baseline_share_24,
                                R.string.cd_share,
                                R.string.badge_alpha
                            ),
                        )

                        ListBottomSheetDialog(
                            user.phoneNumber.formatPhoneNumber()
                                ?: getString(R.string.no_phone_number),
                            list
                        ).show(supportFragmentManager)
                    }
                }
            }
            activityAccountDescription.let {
                it.key.text = getString(R.string.information_base_description)
                it.body.setOnClickListener {
                    withNotNull(viewModel.informationResult.value?.success) { user ->
                        navController.openEditPage(
                            EditActivity.SettingUseCase(
                                type = EditActivity.Type.INTRODUCE,
                                user = user,
                                title = getString(R.string.edit_introduce)
                            )
                        )
                    }
                }
            }
            activityAccountNotify.let {
                it.title.text = getString(R.string.setting)
                it.key.text = getString(R.string.setting_notify)
                it.icon.setImageResource(R.drawable.ic_baseline_music_note_24)
                it.root.setOnClickListener {
                    navController.openSettingPage(Fork.Notification)
                }
            }
            activityAccountSafe.let {
                it.key.text = getString(R.string.setting_safe)
                it.icon.setImageResource(R.drawable.ic_baseline_lock_24)
                it.root.setOnClickListener {
                    navController.openSettingPage(Fork.PrivacySafe)
                }
            }
            activityAccountTheme.let {
                it.key.text = getString(R.string.setting_theme)
                it.icon.setImageResource(R.drawable.ic_baseline_palette_24)
                it.root.setOnClickListener {
                    navController.openSettingPage(Fork.Theme)
                }
            }
            activityAccountCache.let {
                it.key.text = getString(R.string.setting_cache)
                it.icon.setImageResource(R.drawable.ic_baseline_delete_24)
                it.root.setOnClickListener {
                    navController.openSettingPage(Fork.Cache)
                }
            }
            activityAccountProxy.let {
                it.key.setTextResource(R.string.setting_proxy)
                it.icon.setImageResource(R.drawable.ic_baseline_dns_24)
                it.body.setOnClickListener {
                    navController.openSettingPage(Fork.Proxy)
                }
            }
            activityAccountLogout.let {
                it.key.text = getString(R.string.setting_logout)
                it.icon.setImageResource(R.drawable.ic_baseline_logout_24)
                it.body.setOnClickListener {
                    DialogUtils.showCallbackDialog(
                        requireContext(),
                        getString(R.string.setting_logout),
                        "此操作会删除所有本地用户数据",
                        R.string.dialog_callback_position_button to { vm.logoutAllAccount() }
                    )
                }
            }
            activityUserFloating.setOnClickListener {
                navController.selectMedia("image/*") {

                }
            }
            activityUserImage.setOnClickListener {
                ImageDialog(requireContext(), supportFragmentManager)
            }
            widgetUsername = activityAccountName.value
            widgetPhone = activityAccountPhone.value
            widgetIntroduce = activityAccountDescription.value
            widgetAvatar = activityUserImage
        }
        initObserver()
    }

    private fun initObserver() {
        vm.accountLive.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it.isNotEmpty()) {
                    var username = it.first().username
                    if (username.isBlank()) username = getString(R.string.empty_username)
                    widgetUsername.text = username
                    viewModel.getInformation(it.first().id)
                } else {
                    navController.backPressed()
                }
            }
        }

        viewModel.informationResult.observe(viewLifecycleOwner) { result ->
            snackBar("${result.success == null}")
            result.success?.let { updateInformation(it) }
            result.error?.let { snackBar(it.localizedMessage) }
        }
        viewModel.headPicturesLive.observe(viewLifecycleOwner) { result ->
            result.data?.let {
                widgetAvatar.load(it.firstOrNull())
            }
            result.error?.let { snackBar(it.message) }
        }
        viewModel.uploadedHeadPictureLive.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> snackBar(
                    "上传中：${result.data.progress}%",
                    Snackbar.LENGTH_INDEFINITE
                )
                is Result.Error -> snackBar(result.exception.message)
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