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
import com.unltm.distance.base.contracts.showToast
import com.unltm.distance.databinding.ActivityEditBinding
import com.unltm.distance.room.entity.UserRich
import com.unltm.distance.ui.account.AccountViewModel
import com.unltm.distance.ui.components.dialog.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable


class EditActivity : AppCompatActivity() {
    enum class Type : Serializable {
        NAME,

        /**ID,*/
        PHONE, DESCRIPTION
    }

    private lateinit var type: Type
    private lateinit var userRich: UserRich
    private var title: String? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: EditViewModel

    private lateinit var binding: ActivityEditBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = intent.getSerializableExtra(TYPE) as Type
        userRich = intent.getSerializableExtra(USER_RICH) as UserRich

        loadingDialog = LoadingDialog(this)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Slidr.attach(this)

        viewModel = EditViewModel.INSTANCE

        binding.apply {

            when (type) {
                Type.NAME -> {
                    title = getString(R.string.edit_name)
                    edittext.setText(userRich.username)
                }
                Type.PHONE -> {
                    title = getString(R.string.edit_phone)
                    edittext.setText("${userRich.phoneNumber}")
                    binding.edittext.inputType = InputType.TYPE_CLASS_PHONE
                }
                Type.DESCRIPTION -> {
                    edittext.setText(userRich.introduce)
                    title = getString(R.string.edit_description)
                }
            }
            activityEditToolbar.setNavigationOnClickListener { supportFinishAfterTransition() }
            activityEditToolbar.title = title
            edittext.hint = title
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

        viewModel.updateResult.observe(this) { result ->
            result.data?.let {
                loadingDialog.dismiss()
                KeyboardUtils.hideSoftInput(this@EditActivity)
                AccountViewModel.INSTANCE.updateRichInfo(it)
                finish()
            }
            result.error?.let {
                showErrorToast(it.localizedMessage)
            }
        }
    }

    private fun save(string: String?) {
        binding.edittext.isEnabled = false
        loadingDialog.show()
        fun saveName(name: String?) = lifecycleScope.launch {
            viewModel.edit(
                id = userRich.id,
                username = name
            )
        }

        fun savePhone(phone: String?) = lifecycleScope.launch(Dispatchers.Main) {
            val isPhoneNumber = phone.isPhoneNumber()
            if (isPhoneNumber) {
                TODO()
            } else {
                showToast(R.string.wrong_phone_number)
                loadingDialog.dismiss()
                binding.edittext.isEnabled = true
            }
        }

        fun saveDescription(description: String?) = lifecycleScope.launch(Dispatchers.IO) {
            TODO()
        }
        when (type) {
            Type.NAME -> saveName(string)
            Type.PHONE -> savePhone(string)
            Type.DESCRIPTION -> saveDescription(string)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

    companion object {
        const val TYPE = "type"
        const val USER_RICH = "user_rich"
        fun start(activity: AppCompatActivity, type: Type, value: String?) {
            activity.startActivity(Intent(activity, EditActivity::class.java).apply {
                putExtra(TYPE, type)
                putExtra(USER_RICH, value)
            })
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.popup_out)
        }
    }
}