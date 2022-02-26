package com.unltm.distance.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.textfield.TextInputEditText
import com.unltm.distance.R
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.base.contracts.showToast
import com.unltm.distance.base.contracts.startActivity
import com.unltm.distance.databinding.ActivityLoginBinding
import com.unltm.distance.ui.components.dialog.LoadingDialog
import com.unltm.distance.ui.settings.Fork
import com.unltm.distance.ui.settings.SETTING_FORK
import com.unltm.distance.ui.settings.SettingActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var loadingDialog: LoadingDialog

    //WIDGETS
    private lateinit var loginButton: Button
    private lateinit var signButton: Button
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var proxyButton: ImageButton

    private var email: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = LoginViewModel.INSTANCE
        loadingDialog = LoadingDialog(this)
        BarUtils.setStatusBarVisibility(this, false)
        BarUtils.setNavBarVisibility(this, false)

        binding.init()
        initListener()
        initObserver()
    }

    private fun ActivityLoginBinding.init() {
        emailEditText = activityLoginEmail
        passwordEditText = activityLoginPassword
        loginButton = activityLoginLogin
        signButton = activityLoginSign
        proxyButton = proxy
    }

    private fun initListener() {
        emailEditText.addTextChangedListener { email = it.toString() }
        passwordEditText.addTextChangedListener { password = it.toString() }
        loginButton.setOnClickListener(this)
        signButton.setOnClickListener(this)
        proxyButton.setOnClickListener(this)
    }

    private fun initObserver() {
        viewModel.signResultLive.observe(this) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                showToast(R.string.success_sign)
            }
            result.error?.let {
                showErrorToast(it.localizedMessage ?: getString(R.string.error_other))
            }
            signButton.isEnabled = true
        }

        viewModel.loginResultLive.observe(this) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                showToast(R.string.success_login)
                onBackPressedDispatcher.onBackPressed()
            }
            result.error?.let {
                if (it is ServerException) {
                    when (it) {
                        ServerException.ERROR_SIGN_ILLEGAL_EMAIL -> {
                            emailEditText.error = it.message
                        }
                        ServerException.ERROR_SIGN_ILLEGAL_PASSWORD -> {
                            passwordEditText.error = it.message
                        }
                    }
                }
                showErrorToast(it.message ?: getString(R.string.error_other))
            }
            loginButton.isEnabled = true
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            loginButton -> {
                v.isEnabled = false
                loadingDialog.show()
                viewModel.login(email, password)
            }
            signButton -> {
                v.isEnabled = false
                loadingDialog.show()
                viewModel.sign(email, password)
            }
            proxyButton -> startActivity<SettingActivity>(SETTING_FORK to Fork.Proxy)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}

