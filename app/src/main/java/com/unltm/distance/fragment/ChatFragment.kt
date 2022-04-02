package com.unltm.distance.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.unltm.distance.R
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.ActivityChatBinding

class ChatFragment : Fragment(R.layout.activity_chat) {
    private val binding by viewBindings(ActivityChatBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}