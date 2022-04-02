package com.unltm.distance

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.BarUtils
import com.unltm.distance.activity.MediaSelectCallback
import com.unltm.distance.activity.NavController
import com.unltm.distance.activity.PermissionsCallback
import com.unltm.distance.activity.edit.EditActivity
import com.unltm.distance.activity.live.discovery.DiscoveryActivity
import com.unltm.distance.activity.music.MusicActivity
import com.unltm.distance.activity.settings.Fork
import com.unltm.distance.activity.vm
import com.unltm.distance.base.collection.Launchers
import com.unltm.distance.base.contracts.setTextColorResource
import com.unltm.distance.base.contracts.slideFromTop
import com.unltm.distance.base.contracts.startActivity
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.ActivityMainBinding
import com.unltm.distance.datasource.config.BaseConfig
import com.unltm.distance.fragment.conversation.ConversationFragment
import com.unltm.distance.fragment.login.LoginFragment
import com.unltm.distance.fragment.setting.SettingFragment
import com.unltm.distance.fragment.user.AccountFragment
import com.unltm.distance.fragment.user.UserFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main), NavController {
    private val binding by viewBindings(ActivityMainBinding::bind)

    companion object {
        private const val FRAGMENT_CONVERSATION = "fragment_conversation"
        private const val FRAGMENT_CHAT = "fragment_chat"
        private const val FRAGMENT_ACCOUNT = "fragment_account"
        private const val FRAGMENT_USER = "fragment_user"
        private const val FRAGMENT_EDIT = "fragment_edit"
        private const val FRAGMENT_SETTING = "fragment_setting"
        private const val FRAGMENT_LIVE_DISCOVERY = "fragment_live_discovery"
        private const val FRAGMENT_LIVE_PLAYER = "fragment_live_player"
        private const val FRAGMENT_LOGIN = "fragment_login"
    }

    private lateinit var textSwitcher: TextSwitcher
    private var textSwitcherCurrentText: Editable? = null

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectMediaLauncher: ActivityResultLauncher<String>
    private var permissionsCallback: PermissionsCallback = { }
    private var mediaSelectCallback: MediaSelectCallback = { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
                permissionsCallback::invoke
            )
        selectMediaLauncher = Launchers.selectMediaLauncher(this, mediaSelectCallback::invoke)
        with(binding) {
            activityMainToolbar.setNavigationOnClickListener {

            }
            textSwitcher = activityMainTextSwitcher.apply {
                setFactory {
                    TextView(context).also {
                        it.typeface = Typeface.DEFAULT_BOLD
                        it.setTextColorResource(R.color.toolbar_text)
                        it.textSize = 18f
                        it.addTextChangedListener { editable -> textSwitcherCurrentText = editable }
                    }
                }
                setCurrentText(getString(R.string.app_name))
            }
        }
        lifecycleScope.launch {
            BaseConfig.fetchData()
        }
        mainPage()
    }

    override fun openAccountPage(sharedElement: View?) {
        enableBaseWidget(false)
        supportFragmentManager.popBackStack(
            FRAGMENT_ACCOUNT,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        val newInstance = AccountFragment.newInstance()
        supportFragmentManager.commit {
            addToBackStack(FRAGMENT_ACCOUNT)
            replace(R.id.container, newInstance)
        }
    }

    override fun openUserPage(userId: String) {
        enableBaseWidget(false)
        supportFragmentManager.popBackStack(
            FRAGMENT_USER,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        supportFragmentManager.commit {
            addToBackStack(FRAGMENT_USER)
            replace(R.id.container, UserFragment.newInstance(userId))
        }
    }

    override fun openLivePlayerPage(roomId: String) {
        TODO("Not yet implemented")
    }

    override fun openLiveDiscoveryPage(keyword: String) {
        startActivity<DiscoveryActivity>()
    }

    override fun openChatPage(conId: String) {
        TODO("Not yet implemented")
    }

    override fun openEditPage(settingUseCase: EditActivity.SettingUseCase) {
        TODO("Not yet implemented")
    }

    override fun openMusicPage() {
        startActivity<MusicActivity>()
    }

    override fun openLoginPage(backToFinish: Boolean) {
        supportFragmentManager.popBackStack(
            FRAGMENT_LOGIN,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        supportFragmentManager.commit {
            addToBackStack(FRAGMENT_LOGIN)
            replace(R.id.container, LoginFragment())
        }
    }

    override fun requestPermissions(
        vararg permissions: String,
        callback: PermissionsCallback
    ) {
        permissionsCallback = callback
        requestPermissions.launch(permissions.map { it }.toTypedArray())
    }

    override fun updateAppBar(title: String, menu: Int?, onIconPressed: () -> Unit) {
        if (textSwitcherCurrentText == null || textSwitcherCurrentText?.toString() != title)
            textSwitcher.setText(title)
        menu?.also {

        }
        binding.activityMainToolbar.setNavigationOnClickListener {
            onIconPressed.invoke()
        }
        //todo
    }

    @SuppressLint("ResourceAsColor")
    override fun enableBaseWidget(isEnableAppBar: Boolean) {
        with(binding) {
            TransitionManager.beginDelayedTransition(activityMainAppBarLayout, slideFromTop)
            activityMainAppBarLayout.isVisible = isEnableAppBar
        }
    }

    override fun backPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun openSettingPage(fork: Fork) {
        enableBaseWidget(true)
        supportFragmentManager.popBackStack(
            FRAGMENT_SETTING,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        supportFragmentManager.commit {
            addToBackStack(FRAGMENT_SETTING)
            replace(R.id.container, SettingFragment.newInstance(fork))
        }
    }

    override fun selectMedia(s: String, callback: MediaSelectCallback) {
        mediaSelectCallback = callback
        selectMediaLauncher.launch(s)
    }

    override fun setFullMode(isEnable: Boolean) {
        enableBaseWidget(!isEnable)
        BarUtils.setStatusBarVisibility(this, !isEnable)
        BarUtils.setNavBarVisibility(this, !isEnable)
    }

    override fun mainPage() {
        supportFragmentManager.popBackStack()
        enableBaseWidget(true)
        supportFragmentManager.commit {
            addToBackStack(FRAGMENT_CONVERSATION)
            replace(R.id.container, ConversationFragment.Instance)
        }
        vm.getAccounts()
    }
}