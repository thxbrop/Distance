package com.unltm.distance.ui.conversation.components

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.databinding.DialogAccountBinding
import com.unltm.distance.databinding.ItemMemberBinding
import com.unltm.distance.room.entity.User

class AccountDialog(
    context: Context,
    private val accounts: List<User>,
    private val listener: (Int) -> Unit
) : AlertDialog(context, true, null) {
    private lateinit var binding: DialogAccountBinding
    private lateinit var adapter: AccountListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(null)
        binding.apply {
            accountList.let {
                it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                it.adapter =
                    AccountListAdapter().also { adapter -> this@AccountDialog.adapter = adapter }
            }
        }
        adapter.setOnItemClickListener {
            listener.invoke(it)
            dismiss()
        }
        adapter.submitList(accounts)
    }

    class AccountListAdapter : ListAdapter<User, AccountListAdapter.AccountItem>(DIFF) {
        class AccountItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding = ItemMemberBinding.bind(itemView)
        }

        object DIFF : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }
        }

        private var listener: ((Int) -> Unit)? = null

        fun setOnItemClickListener(listener: (Int) -> Unit) {
            this.listener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountItem {
            return AccountItem(
                LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
            )
        }

        override fun onBindViewHolder(holder: AccountItem, position: Int) {
            val account = getItem(position)
            holder.binding.apply {
                root.setOnClickListener {
                    listener?.invoke(position)
                }
                username.text = account.username
                timestamp.text = ""
            }
        }
    }
}