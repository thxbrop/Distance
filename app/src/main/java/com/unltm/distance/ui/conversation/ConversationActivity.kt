package com.unltm.distance.ui.conversation

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.unltm.distance.R
import com.unltm.distance.base.collection.Launchers
import com.unltm.distance.base.contracts.setTextColorResource
import com.unltm.distance.base.contracts.sp
import com.unltm.distance.base.contracts.startActivity
import com.unltm.distance.databinding.ActivityConversationBinding
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.account.AccountActivity
import com.unltm.distance.ui.login.LoginActivity

class ConversationActivity : AppCompatActivity() {

    private lateinit var viewModel: ConversationViewModel

    private lateinit var binding: ActivityConversationBinding
    private lateinit var headerView: View
    private lateinit var headImageView: ShapeableImageView
    private lateinit var usernameTextView: TextView
    private lateinit var textSwitcher: TextSwitcher

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
    )

    private var currentUser = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ConversationViewModel.getInstance()
        requestPermissions = Launchers.requestPermissionsLauncher(this)
        binding.init()
        initObserver()
        initOnClickListener()
    }

    private fun initObserver() {
        viewModel.currentUserLive.observe(this) { result ->
            result.success?.let {
                currentUser = it.toMutableList()
                usernameTextView.text = it.first().username
                headImageView.setOnClickListener {
                    startActivity<AccountActivity>()
                }
            }
            result.error?.let {
                currentUser = mutableListOf()
                headImageView.setOnClickListener {
                    startActivity<LoginActivity>()
                }
            }
        }
        viewModel.getCurrentUser()
    }

    private fun ActivityConversationBinding.init() {
        activityMainNavigationView.let {
            headerView = it.getHeaderView(0)
            headImageView = headerView.findViewById(R.id.imageView)
            usernameTextView = headerView.findViewById(R.id.textView)
        }
        activityMainToolbar.setNavigationOnClickListener { root.open() }
        textSwitcher = activityMainTextSwitcher.apply {
            setFactory {
                TextView(context).also {
                    it.typeface = Typeface.DEFAULT_BOLD
                    it.setTextColorResource(R.color.toolbar_text)
                    it.textSize = 18f
                }
            }
            setCurrentText(getString(R.string.app_name))
        }

        ratio.setOnProgressChangeListener {

        }
    }

    private fun initOnClickListener() {

    }

    override fun onStart() {
        super.onStart()
        requestPermissions.launch(permissions)
    }
}
