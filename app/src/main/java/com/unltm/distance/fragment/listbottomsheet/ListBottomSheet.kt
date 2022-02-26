package com.unltm.distance.fragment.listbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unltm.distance.R
import com.unltm.distance.adapter.bottomsheet.BottomSheetAdapter
import com.unltm.distance.adapter.bottomsheet.OnItemClickListener
import com.unltm.distance.adapter.bottomsheet.SettingItem

class ListBottomSheet(
    private val title: String,
    private val list: List<SettingItem>,
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    fun show(manager: FragmentManager) {
        show(manager, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.fragment_bottom_sheet_title).text = title
        view.findViewById<RecyclerView>(R.id.fragment_bottom_sheet_recyclerview).apply {
            layoutManager = LinearLayoutManager(this@ListBottomSheet.requireContext())
            adapter = BottomSheetAdapter().apply {
                setItemOnClickListener(object : OnItemClickListener {
                    override fun onClick() {
                        dismiss()
                    }
                })
                submitList(list)
            }
        }
    }
}