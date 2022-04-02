package com.unltm.distance.activity.live.discovery

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.GridLayoutManager
import com.r0adkll.slidr.Slidr
import com.unltm.distance.R
import com.unltm.distance.base.contracts.setTextColorResource
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.databinding.ActivityRecoBinding
import com.unltm.distance.datasource.LiveDataSource
import com.unltm.distance.activity.live.discovery.editor.TextEditor
import com.unltm.distance.activity.live.discovery.editor.TextEditorCallback
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DiscoveryActivity : AppCompatActivity(), TextEditorCallback {
    private lateinit var binding: ActivityRecoBinding
    private lateinit var viewModel: DiscoveryViewModel
    private lateinit var adapter: DiscoveryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Slidr.attach(this)
        binding.init()
        initObserver()
    }

    private fun initObserver() {
        viewModel = DiscoveryViewModel.INSTANCE
        viewModel.searchResult.observe(this) { result ->
            result.data?.let { initList(it) }
            result.error?.let { errorToast(it.message) }
        }
    }

    private fun ActivityRecoBinding.init() {
        val title = getString(R.string.live)
        toolbar.textSwitcher.setFactory {
            TextView(this@DiscoveryActivity).also {
                it.typeface = Typeface.DEFAULT_BOLD
                it.setTextColorResource(R.color.toolbar_text)
                it.textSize = 18f
            }
        }
        toolbar.textSwitcher.setText(title)
        activityRecoRecyclerview.let {
            it.layoutManager =
                GridLayoutManager(this@DiscoveryActivity, 2, GridLayoutManager.VERTICAL, false)
            it.adapter = DiscoveryAdapter().also { adapter ->
                this@DiscoveryActivity.adapter = adapter
            }
        }
        floatingActionButton.setOnClickListener {
            val secondaryTitle = getString(R.string.type_key_word)
            toolbar.textSwitcher.setText(secondaryTitle)
            TextEditor(this@DiscoveryActivity, activityRecoRecyclerview.height).apply {
                setOnIssueCallback(this@DiscoveryActivity)
                show()
            }
        }
    }

    private fun initList(liveDataSource: LiveDataSource) {
        lifecycleScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = true,
                    initialLoadSize = 20
                ),
                pagingSourceFactory = { liveDataSource }
            ).flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onSubmit(keyword: String) {
        viewModel.search(keyword)
    }

    override fun onCancel() {
        val title = getString(R.string.live)
        binding.toolbar.textSwitcher.setText(title)
    }
}

