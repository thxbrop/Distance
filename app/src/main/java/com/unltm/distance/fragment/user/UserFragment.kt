package com.unltm.distance.fragment.user

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.unltm.distance.R
import com.unltm.distance.activity.NavController
import com.unltm.distance.activity.account.AccountViewModel
import com.unltm.distance.activity.getNavController
import com.unltm.distance.activity.vm
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.base.contracts.formatPhoneNumber
import com.unltm.distance.base.contracts.isNull
import com.unltm.distance.base.viewBindings
import com.unltm.distance.databinding.FragmentAccountBinding
import com.unltm.distance.room.entity.User

class UserFragment : Fragment(R.layout.fragment_account) {
    private lateinit var widgetUsername: TextView
    private lateinit var widgetPhone: TextView
    private lateinit var widgetIntroduce: TextView
    private lateinit var widgetAvatar: ShapeableImageView

    companion object {
        private const val ARG_USER_ID = "arg_user_id"
        fun newInstance(userId: String) = UserFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_USER_ID, userId)
            }
        }
    }

    private lateinit var navController: NavController

    private val viewModel: AccountViewModel by viewModels()
    private val binding by viewBindings(FragmentAccountBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.slide_bottom)
        navController = getNavController()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            widgetUsername = activityAccountName.value
            widgetPhone = activityAccountPhone.value
            widgetIntroduce = activityAccountDescription.value
            widgetAvatar = activityUserImage
        }

        val userId = arguments?.getString(ARG_USER_ID)
        if (userId == null) {
            parentFragmentManager.popBackStack()
            return
        }
        initObserver()

        viewModel.getInformation(userId)

    }

    private fun initObserver() {
        vm.accountLive.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it.isNotEmpty()) {
                    var username = it.first().username
                    if (username.isBlank()) username = getString(R.string.empty_username)
                    widgetUsername.text = username
                    viewModel.getInformation(it.first().id)
                } else {
                    navController.backPressed()
                }
            }
        }

        viewModel.informationResult.observe(viewLifecycleOwner) { result ->
            result.success?.let { updateInformation(it) }
            result.error?.let { errorToast(it.localizedMessage) }
        }
        viewModel.headPicturesLive.observe(viewLifecycleOwner) { result ->
            result.data?.let {
                widgetAvatar.load(it.firstOrNull())
            }
            result.error?.let { errorToast(it.message) }
        }
        vm.getAccounts()
    }

    private fun updateInformation(user: User) {
        widgetPhone.text =
            if (user.phoneNumber.isNull) getString(R.string.no_phone_number)
            else user.phoneNumber.formatPhoneNumber()
        widgetIntroduce.text =
            user.introduce.ifBlank { getString(R.string.no_description) }
        viewModel.getHeadPictures(user.avatars)
    }
}