package com.unltm.distance.base.collection

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.PhoneUtils
import com.unltm.distance.R
import com.unltm.distance.R.color.*
import com.unltm.distance.R.drawable.*
import com.unltm.distance.R.string.*
import com.unltm.distance.adapter.ExpansionAdapter
import com.unltm.distance.adapter.bottomsheet.SettingItem
import com.unltm.distance.base.contracts.requirePermission
import com.unltm.distance.base.contracts.showToast
import com.unltm.distance.base.jvm.QRCodeConverter
import com.unltm.distance.components.dialog.VoiceDialog
import com.unltm.distance.room.entity.UserRich
import com.unltm.distance.ui.components.dialog.BitmapDialog
import com.unltm.distance.ui.edit.EditActivity

object Items {
    fun getExpansionList(
        context: Context,
        permissionLauncher: ActivityResultLauncher<String>,
        launcher: ActivityResultLauncher<String>,
        fragmentManager: FragmentManager,
        recordingCallback: (String) -> Unit
    ) = listOf(
        ExpansionAdapter.Expansion(
            photo,
            ic_round_photo_24,
            R.color.colorPrimary
        ) { launcher.launch("image/*") },
        ExpansionAdapter.Expansion(voice, ic_baseline_mic_24, orange) {
            context.requirePermission(permissionLauncher, Manifest.permission.RECORD_AUDIO) {
                VoiceDialog().show(fragmentManager, "", recordingCallback)
            }
        },
        ExpansionAdapter.Expansion(location, ic_baseline_location_on_24, green_blue),
        ExpansionAdapter.Expansion(
            music,
            ic_baseline_music_note_24,
            red
        ) { launcher.launch("audio/*") },
        ExpansionAdapter.Expansion(
            video,
            ic_round_videocam_24,
            gray
        ) { launcher.launch("video/*") },
        ExpansionAdapter.Expansion(
            document,
            ic_round_insert_drive_file_24,
            blue_400
        ) { launcher.launch("*/*") },
    )

    fun getExpansionList1(
        callback: (ExpansionType) -> Unit
    ) = listOf(
        ExpansionAdapter.Expansion(
            photo,
            ic_round_photo_24,
            R.color.colorPrimary
        ) { callback.invoke(ExpansionType.Image) },
        ExpansionAdapter.Expansion(voice, ic_baseline_mic_24, orange) {
            callback.invoke(
                ExpansionType.Record
            )
        },
        ExpansionAdapter.Expansion(
            location,
            ic_baseline_location_on_24,
            green_blue
        ) { callback.invoke(ExpansionType.Location) },
        ExpansionAdapter.Expansion(music, ic_baseline_music_note_24, red) {
            callback.invoke(
                ExpansionType.Music
            )
        },
        ExpansionAdapter.Expansion(video, ic_round_videocam_24, gray) {
            callback.invoke(
                ExpansionType.Video
            )
        },
        ExpansionAdapter.Expansion(
            document,
            ic_round_insert_drive_file_24,
            blue_400
        ) { callback.invoke(ExpansionType.Document) },
    )


    @SuppressLint("MissingPermission")
    fun getPhoneSubmenu(
        activity: AppCompatActivity,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        userRich: UserRich,
        isAccount: Boolean = false,
    ): List<SettingItem> {
        val mutableListOf = mutableListOf(
            SettingItem(ic_baseline_edit_24, cd_edit)
            { EditActivity.start(activity, EditActivity.Type.PHONE, userRich) },
            SettingItem(ic_baseline_call_24, cd_call) {
                activity.requirePermission(
                    requestPermissionLauncher,
                    Manifest.permission.CALL_PHONE
                ) {
                    PhoneUtils.call((userRich.phoneNumber?:0).toString())
                }
            },
            SettingItem(ic_round_content_copy_24, cd_copy) {
                ClipboardUtils.copyText(userRich.phoneNumber.toString())
                activity.showToast(success_copy)
            },
            SettingItem(ic_baseline_share_24, cd_share),
        )
        if (!isAccount) {
            mutableListOf.removeAt(0)
        }
        return mutableListOf
    }

    fun getAccountSubMenu(
        activity: AppCompatActivity,
        fragmentManager: FragmentManager,
        user: UserRich
    ) = mutableListOf(
        SettingItem(ic_baseline_edit_24, cd_edit) {
            EditActivity.start(activity, EditActivity.Type.NAME, user)
        },
        SettingItem(ic_round_content_copy_24, cd_copy_link),
        SettingItem(ic_round_qr_code_24, qr_code) {
            val bitmap = QRCodeConverter.encryptUser(user)
            BitmapDialog(activity, fragmentManager, bitmap).show()
        }
    ).also {
//        if (user.id != LCUser.getCurrentUser()?.objectId) {
//            it.removeAt(0)
//        }
    }

}