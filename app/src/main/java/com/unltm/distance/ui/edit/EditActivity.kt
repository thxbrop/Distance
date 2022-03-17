package com.unltm.distance.ui.edit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.r0adkll.slidr.Slidr
import com.unltm.distance.R
import com.unltm.distance.base.contracts.isPhoneNumber
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.databinding.ActivityEditBinding
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.account.AccountViewModel
import com.unltm.distance.ui.components.dialog.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable


class EditActivity : AppCompatActivity() {
    enum class Type : Serializable {
        NAME,

        /**ID,*/
        PHONE, INTRODUCE
    }

    private lateinit var type: Type
    private lateinit var user: User
    private var title: String? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: EditViewModel

    private lateinit var binding: ActivityEditBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBase()
        binding.init()
        initObserver()
    }

    private fun initObserver() {
        viewModel.updateResult.observe(this) { result ->
            loadingDialog.dismiss()
            binding.edittext.isEnabled = true
            result.data?.let {
                KeyboardUtils.hideSoftInput(this@EditActivity)
                AccountViewModel.INSTANCE.updateRichInfo(it)
                finish()
            }
            result.error?.let {
                showErrorToast(it.localizedMessage)
            }
        }
    }

    private fun initBase() {
        type = intent.getSerializableExtra(TYPE) as Type
        user = intent.getSerializableExtra(USER_RICH) as User

        loadingDialog = LoadingDialog(this)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Slidr.attach(this)
        viewModel = EditViewModel.INSTANCE
    }

    private fun ActivityEditBinding.init() {
        when (type) {
            Type.NAME -> {
                this@EditActivity.title = getString(R.string.edit_name)
                edittext.setText(user.username)
            }
            Type.PHONE -> {
                this@EditActivity.title = getString(R.string.edit_phone)
                user.phoneNumber?.let { edittext.setText("$it") }
                binding.edittext.inputType = InputType.TYPE_CLASS_PHONE
            }
            Type.INTRODUCE -> {
                edittext.setText(user.introduce)
                this@EditActivity.title = getString(R.string.edit_introduce)
            }
        }
        activityEditToolbar.setNavigationOnClickListener { supportFinishAfterTransition() }
        activityEditToolbar.title = this@EditActivity.title
        edittext.hint = this@EditActivity.title
        activityEditToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit_toolbar_correct -> {
                    save(edittext.text.toString())
                    true
                }
                else -> false
            }
        }
        textInputLayout.requestFocus()
    }


    private fun save(string: String?) {
        binding.edittext.isEnabled = false
        loadingDialog.show()
        fun saveName(name: String?) = lifecycleScope.launch {
            viewModel.edit(
                id = user.id,
                username = name
            )
        }

        fun savePhone(phone: String?) = lifecycleScope.launch(Dispatchers.Main) {
            val isPhoneNumber = phone.isPhoneNumber()
            if (isPhoneNumber) {
                viewModel.edit(
                    id = user.id,
                    phoneNumber = phone?.toLong()
                )
            } else {
                showErrorToast(R.string.illegal_phone_number)
                loadingDialog.dismiss()
                binding.edittext.isEnabled = true
            }
        }

        fun saveIntroduce(introduce: String?) = lifecycleScope.launch(Dispatchers.IO) {
            if (introduce.isNullOrBlank()) {
                showErrorToast(R.string.illegal_introduce)
            } else {
                viewModel.edit(
                    id = user.id,
                    introduce = introduce
                )
            }
        }
        when (type) {
            Type.NAME -> saveName(string)
            Type.PHONE -> savePhone(string)
            Type.INTRODUCE -> saveIntroduce(string)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

    companion object {
        const val TYPE = "type"
        const val USER_RICH = "user_rich"
        fun start(activity: AppCompatActivity, type: Type, value: User) {
            activity.startActivity(Intent(activity, EditActivity::class.java).apply {
                putExtra(TYPE, type)
                putExtra(USER_RICH, value)
            })
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.popup_out)
        }
    }
}
