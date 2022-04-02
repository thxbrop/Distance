package com.unltm.distance.activity.login

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.textfield.TextInputEditText
import com.unltm.distance.R
import com.unltm.distance.activity.settings.Fork
import com.unltm.distance.activity.settings.SETTING_FORK
import com.unltm.distance.activity.settings.SettingActivity
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.base.contracts.toast
import com.unltm.distance.base.contracts.startActivity
import com.unltm.distance.databinding.ActivityLoginBinding
import com.unltm.distance.ui.dialog.LoadingDialog

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var loginButton: Button
    private lateinit var signButton: Button
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var proxyButton: ImageButton

    private var license = License()

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = LoginViewModel()
        loadingDialog = LoadingDialog(this)
        BarUtils.setStatusBarVisibility(this, false)
        BarUtils.setNavBarVisibility(this, false)
        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            }

        binding.init()
        initListener()
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        requestPermissions.launch(permissions)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeObserverForever()
    }

    private fun ActivityLoginBinding.init() {
        emailEditText = activityLoginEmail
        passwordEditText = activityLoginPassword
        loginButton = activityLoginLogin
        signButton = activityLoginSign
        proxyButton = proxy
    }

    private fun initListener() {
        emailEditText.addTextChangedListener { license.email = it.toString() }
        passwordEditText.addTextChangedListener { license.password = it.toString() }
        loginButton.setOnClickListener(this)
        signButton.setOnClickListener(this)
        proxyButton.setOnClickListener(this)
    }

    private fun initObserver() {
        viewModel.signResultLive.observe(this) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                toast(R.string.success_sign)
            }
            result.error?.let {
                errorToast(it.localizedMessage ?: getString(R.string.error_other))
            }
            signButton.isEnabled = true
        }

        viewModel.loginResultLive.observe(this) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                toast(R.string.success_login)
                onBackPressedDispatcher.onBackPressed()
            }
            result.error?.let {
                if (it is ServerException) {
                    when (it) {
                        ServerException.ILLEGAL_EMAIL -> emailEditText.error = it.message
                        ServerException.ILLEGAL_PASSWORD -> passwordEditText.error = it.message
                        else -> errorToast(it.message)
                    }
                }
                errorToast(it.message)
            }
            loginButton.isEnabled = true
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            loginButton -> {
                v.isEnabled = false
                loadingDialog.show()
                with(license) {
                    viewModel.login(email, password)
                }
            }
            signButton -> {
                v.isEnabled = false
                loadingDialog.show()
                with(license) {
                    viewModel.sign(email, password)
                }
            }
            proxyButton -> startActivity<SettingActivity>(SETTING_FORK to Fork.Proxy)
        }
    }

    private class License(var email: String? = null, var password: String = "")
}