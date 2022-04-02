package com.unltm.distance.activity.conversation

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
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
import com.unltm.distance.activity.account.AccountActivity
import com.unltm.distance.activity.conversation.components.AccountDialog
import com.unltm.distance.activity.conversation.components.CreateConversationDialog
import com.unltm.distance.activity.conversation.useCase.CreateConversationUseCase
import com.unltm.distance.activity.live.discovery.DiscoveryActivity
import com.unltm.distance.activity.login.LoginActivity
import com.unltm.distance.activity.music.MusicActivity
import com.unltm.distance.activity.vm
import com.unltm.distance.base.contracts.*
import com.unltm.distance.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {
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
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        with(vm) {
            accountLive.observe(this@ConversationActivity) {
                getConversations()
                if (it.isNotEmpty()) {
                    usernameTextView.text = it.first().username
                    headImageView.setOnClickListener {
                        startActivity<AccountActivity>()
                    }
                    headImageView.setOnLongClickListener { _ ->
                        AccountDialog(this@ConversationActivity, it) { position ->
                            changeCurrentAccount(it[position].id)
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

            conversationsLive.observe(this@ConversationActivity) { result ->
                result.data?.let { adapter.submitList(it) }
                result.error?.let { errorToast(it.message) }
            }

            messagesLive.forEach { (_, livedata) ->
                livedata.observe(this@ConversationActivity) { result ->
                    result.data?.let {
                        Log.e(javaClass.simpleName, "messagesLive:$it")
                    }
                }
            }
        }

        viewModel.createLive.observe(this) { result ->
            result.data?.let {

            }
            result.error?.let { errorToast(it.message) }
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
                        startActivity<DiscoveryActivity>()
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
                CreateConversationDialog(context) {
                    val user = requireNotNull(vm.currentUser)
                    viewModel.createConversation(
                        CreateConversationUseCase(
                            creator = user.id,
                            name = it.toString()
                        )
                    )
                }.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissions.launch(permissions)
        vm.getAccounts()
    }
}
