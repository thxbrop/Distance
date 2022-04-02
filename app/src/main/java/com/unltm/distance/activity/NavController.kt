package com.unltm.distance.activity

import android.net.Uri
import android.view.View
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import com.unltm.distance.activity.edit.EditActivity
import com.unltm.distance.activity.settings.Fork

interface NavController {
    fun openAccountPage(sharedElement: View? = null)
    fun openUserPage(userId: String)
    fun openLivePlayerPage(roomId: String)
    fun openLiveDiscoveryPage(keyword: String)
    fun openChatPage(conId: String)
    fun openEditPage(settingUseCase: EditActivity.SettingUseCase)
    fun openMusicPage()
    fun openLoginPage(backToFinish: Boolean = true)
    fun requestPermissions(vararg permissions: String, callback: PermissionsCallback)
    fun updateAppBar(
        title: String,
        @MenuRes menu: Int?,
        onIconPressed: () -> Unit = { backPressed() }
    )

    fun enableBaseWidget(isEnableAppBar: Boolean = true)
    fun backPressed()
    fun openSettingPage(fork: Fork)
    fun selectMedia(s: String, callback: MediaSelectCallback)
    fun setFullMode(isEnable: Boolean)
    fun mainPage()
}

typealias PermissionsCallback = (Map<String, Boolean>) -> Unit
typealias MediaSelectCallback = (Uri) -> Unit

fun Fragment.getNavController() = requireActivity() as NavController