package com.unltm.distance.activity.bubble

import android.app.Notification
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import androidx.annotation.RequiresApi
import com.unltm.distance.R

@RequiresApi(30)
class BubbleCenter {
    fun sendBubble(context: Context) {
        val target = Intent(context, BubbleActivity::class.java)
        val bubbleIntent = PendingIntent.getActivity(context, 0, target, 0)
        val category = ""
        val chatPartner = Person.Builder()
            .setName("name")
            .setImportant(true)
            .build()
        val shortcutId = ""
        val shortcut = ShortcutInfo.Builder(
            context,
            shortcutId
        )
            .setCategories(setOf(category))
            .setIntent(Intent(Intent.ACTION_DEFAULT))
            .setLongLived(true)
            .setShortLabel(chatPartner.name ?: "")
            .build()
        val bubbleData = Notification.BubbleMetadata.Builder(
            bubbleIntent,
            Icon.createWithResource(context, R.drawable.emoji_1)
        )
            .setDesiredHeight(600)
            .build()


    }
}