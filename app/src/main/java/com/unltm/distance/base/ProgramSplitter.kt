package com.unltm.distance.base

import android.app.Activity
import android.content.Context
import com.unltm.distance.base.contracts.startActivity
import com.unltm.distance.repository.AccountRepository
import com.unltm.distance.activity.conversation.ConversationActivity
import com.unltm.distance.activity.login.LoginActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProgramSplitter(private val context: Context) {
    private var accountRepository: AccountRepository = AccountRepository.INSTANCE

    init {
        MainScope().launch {
            val currentUser = accountRepository.getCurrentUser()
            if (currentUser.isNullOrEmpty()) context.startActivity<LoginActivity>()
            else {
                context.startActivity<ConversationActivity>()
                (context as Activity).finish()
            }
        }

    }
}