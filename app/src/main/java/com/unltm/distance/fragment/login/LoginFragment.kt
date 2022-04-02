package com.unltm.distance.fragment.login

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.unltm.distance.R
import com.unltm.distance.activity.NavController
import com.unltm.distance.activity.getNavController
import com.unltm.distance.activity.login.LoginViewModel
import com.unltm.distance.activity.settings.Fork
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.base.contracts.toast
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.FragmentLoginBinding
import com.unltm.distance.ui.dialog.LoadingDialog

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {
    private val binding by viewBindings(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var loginButton: Button
    private lateinit var signButton: Button
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var proxyButton: ImageButton
    private var license = License()

    private lateinit var navController: NavController

    private class License(var email: String? = null, var password: String = "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(requireContext())
        navController = getNavController()
        navController.requestPermissions(Manifest.permission.INTERNET) { map ->
            if (map.getOrDefault(Manifest.permission.INTERNET, false)) {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        navController.setFullMode(true)
    }

    override fun onPause() {
        super.onPause()
        navController.setFullMode(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeObserverForever()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        with(binding) {
            emailEditText = activityLoginEmail
            passwordEditText = activityLoginPassword
            loginButton = activityLoginLogin
            signButton = activityLoginSign
            proxyButton = proxy
            emailEditText.addTextChangedListener { license.email = it.toString() }
            passwordEditText.addTextChangedListener { license.password = it.toString() }
            loginButton.setOnClickListener(this@LoginFragment)
            signButton.setOnClickListener(this@LoginFragment)
            proxyButton.setOnClickListener(this@LoginFragment)
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
            proxyButton -> navController.openSettingPage(Fork.Proxy)
        }
    }

    private fun initObserver() {
        viewModel.observerForever()
        viewModel.signResultLive.observe(viewLifecycleOwner) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                toast(R.string.success_sign)
            }
            result.error?.let {
                errorToast(it.localizedMessage ?: getString(R.string.error_other))
            }
            signButton.isEnabled = true
        }

        viewModel.loginResultLive.observe(viewLifecycleOwner) { result ->
            loadingDialog.dismiss()
            result.success?.let {
                toast(R.string.success_login)
                navController.mainPage()
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
}