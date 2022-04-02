package com.unltm.distance.fragment.conversation

import android.Manifest
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unltm.distance.R
import com.unltm.distance.activity.NavController
import com.unltm.distance.activity.conversation.ConversationAdapter
import com.unltm.distance.activity.conversation.ConversationViewModel
import com.unltm.distance.activity.conversation.components.CreateConversationDialog
import com.unltm.distance.activity.conversation.useCase.CreateConversationUseCase
import com.unltm.distance.activity.getNavController
import com.unltm.distance.activity.vm
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.FragmentConversationBinding
import com.unltm.distance.datasource.LiveDataSource

class ConversationFragment private constructor() : Fragment(R.layout.fragment_conversation) {
    private val binding by viewBindings(FragmentConversationBinding::bind)
    private val viewModel: ConversationViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var createFloatingActionButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ConversationAdapter

    companion object {
        val Instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ConversationFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = getNavController()
        enterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.slide_bottom)
        navController.requestPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
        ) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            activityMainNavigationView.let {
                it.setNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.menu_main_navigation_live ->
                            navController.openLiveDiscoveryPage(LiveDataSource.DEFAULT_KEY_WORD)
                        R.id.menu_main_navigation_music -> navController.openMusicPage()
                        R.id.menu_main_navigation_add -> navController.openLoginPage(false)
                    }
                    false
                }
            }
            activityMainRecyclerview.let {
                recyclerView = it
                it.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
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
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        navController.enableBaseWidget(true)
        navController.updateAppBar(getString(R.string.app_name), null) {
            if (binding.root.isOpen) binding.root.close()
            else binding.root.open()
        }
    }

    private fun initObserver() {
        with(vm) {
            accountLive.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    val menu = binding.activityMainNavigationView.menu
                    val ids = arrayOf(
                        R.id.account_1,
                        R.id.account_2,
                        R.id.account_3,
                        R.id.account_4,
                        R.id.account_5,
                    )
                    it.forEachIndexed { index, user ->
                        menu.addSubMenu(R.id.accounts, ids[index], index, user.username)
                    }
                }
            }
            conversationsLive.observe(viewLifecycleOwner) { result ->
                result.data?.let { adapter.submitList(it) }
                result.error?.let { errorToast(it.message) }
            }

            messagesLive.forEach { (_, livedata) ->
                livedata.observe(viewLifecycleOwner) { result ->
                    result.data?.let {
                        Log.e(javaClass.simpleName, "messagesLive:$it")
                    }
                }
            }
        }

        viewModel.createLive.observe(viewLifecycleOwner) { result ->
            result.data?.let {

            }
            result.error?.let { errorToast(it.message) }
        }
    }

}