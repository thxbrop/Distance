package com.unltm.distance.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.unltm.distance.R
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.base.contracts.showToast
import com.unltm.distance.databinding.ActivityLoginBinding
import com.unltm.distance.ui.components.dialog.LoadingDialog

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel
    private lateinit var loadingDialog: LoadingDialog

    private var email: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        viewModel = LoginViewModel.getInstance()
        loadingDialog = LoadingDialog(this)

        binding.init()
        binding.initOnClickListener()
        initObserver()
    }


    private fun ActivityLoginBinding.init() {
        activityLoginEmail.addTextChangedListener { email = it.toString() }
        activityLoginPassword.addTextChangedListener { password = it.toString() }
    }

    private fun ActivityLoginBinding.initOnClickListener() {
        activityLoginLogin.setOnClickListener(this@LoginActivity)
        activityLoginSign.setOnClickListener(this@LoginActivity)
    }

    private fun initObserver() {
        viewModel.signResultLive.observe(this) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                showToast(R.string.sign_success)
            }
            result.error?.let {
                showErrorToast(it.localizedMessage ?: getString(R.string.error_other))
            }
            binding.activityLoginSign.isEnabled = true
        }

        viewModel.loginResultLive.observe(this) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                showToast(R.string.login_success)
                onBackPressedDispatcher.onBackPressed()
            }
            result.error?.let {
                showErrorToast(it.localizedMessage ?: getString(R.string.error_other))
            }
            binding.activityLoginLogin.isEnabled = true
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                activityLoginLogin -> {
                    if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                        v.isEnabled = false
                        loadingDialog.show()
                        viewModel.login(email!!, password!!)
                    }
                }
                activityLoginSign -> {
                    if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
                        v.isEnabled = false
                        loadingDialog.show()
                        viewModel.sign(email!!, password!!)
                    }

                }
            }
        }
    }

}

