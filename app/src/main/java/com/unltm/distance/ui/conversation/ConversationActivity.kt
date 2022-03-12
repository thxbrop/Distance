package com.unltm.distance.ui.conversation

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.unltm.distance.R
import com.unltm.distance.base.contracts.*
import com.unltm.distance.databinding.ActivityConversationBinding
import com.unltm.distance.room.entity.User
import com.unltm.distance.ui.GlobalViewModel
import com.unltm.distance.ui.account.AccountActivity
import com.unltm.distance.ui.conversation.components.AccountDialog
import com.unltm.distance.ui.conversation.components.CreateConversationDialog
import com.unltm.distance.ui.live.reco.RecoActivity
import com.unltm.distance.ui.login.LoginActivity
import com.unltm.distance.ui.music.MusicActivity

class ConversationActivity : AppCompatActivity() {

    private lateinit var globalViewModel: GlobalViewModel
    private lateinit var viewModel: ConversationViewModel

    private lateinit var binding: ActivityConversationBinding
    private lateinit var headerView: View
    private lateinit var headImageView: ShapeableImageView
    private lateinit var usernameTextView: TextView
    private lateinit var textSwitcher: TextSwitcher
    private lateinit var createFloatingActionButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ConversationAdapter

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
        globalViewModel = GlobalViewModel.INSTANCE
        viewModel = ConversationViewModel.INSTANCE
        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            }
        binding.init()
        initBase()
        initObserver()
    }

    private fun initBase() {
        onBackPressedDispatcher.addCallback {
            if (binding.root.isOpen) binding.root.close()
            else finish()
        }
        adapter = ConversationAdapter().also { adapter ->
            recyclerView.adapter = adapter
            adapter.onInserted { positionStart, itemCount ->
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    positionStart + itemCount - 1, 0
                )
            }
        }
    }

    private fun initObserver() {
        globalViewModel.accountLive.observe(this) {
            currentUser = it.toMutableList()
            globalViewModel.getConversations()
            if (it.isNotEmpty()) {
                usernameTextView.text = it.first().username
                headImageView.setOnClickListener {
                    startActivity<AccountActivity>()
                }
                headImageView.setOnLongClickListener { _ ->
                    AccountDialog(this, it) { position ->
                        globalViewModel.changeCurrentAccount(it[position].id)
                    }.show()
                    true
                }
            } else {
                usernameTextView.setTextResource(R.string.unLog)
                headImageView.setOnClickListener {
                    startActivity<LoginActivity>()
                }
            }
        }

        globalViewModel.conversationsLive.observe(this) { result ->
            result.data?.let { adapter.submitList(it) }
            result.error?.let { showErrorToast(it.message) }
        }

        viewModel.createConversationLive.observe(this) { result ->
            result.success?.let {

            }
            result.error?.let { showErrorToast(it.message) }
        }
    }

    private fun ActivityConversationBinding.init() {
        activityMainRecyclerview.let {
            recyclerView = it
            it.layoutManager = LinearLayoutManager(
                this@ConversationActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        activityMainNavigationView.let {
            headerView = it.getHeaderView(0)
            headImageView = headerView.findViewById(R.id.imageView)
            usernameTextView = headerView.findViewById(R.id.textView)
            it.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_main_navigation_live -> {
                        startActivity<RecoActivity>()
                    }
                    R.id.menu_main_navigation_music -> {
                        startActivity<MusicActivity>()
                    }
                }
                false
            }
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
        createFloatingActionButton = floatingActionButton.apply {
            setOnClickListener {
                CreateConversationDialog(context, true) {
                    viewModel.createConversation(it)
                }.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissions.launch(permissions)
        globalViewModel.getAccounts()
    }
}
