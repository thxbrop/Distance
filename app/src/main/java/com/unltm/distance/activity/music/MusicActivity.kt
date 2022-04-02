package com.unltm.distance.activity.music

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unltm.distance.R
import com.unltm.distance.base.contracts.initAppBar
import com.unltm.distance.base.contracts.errorToast
import com.unltm.distance.databinding.ActivityMusicBinding

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private lateinit var viewModel: MusicViewModel
    private lateinit var adapter: MusicAdapter

    //Widgets
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.init()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getRandomMusic()
    }

    private fun ActivityMusicBinding.init() {
        appBarLayout.initAppBar(R.string.music, onBackPressedDispatcher)
        activityMusicRecyclerview.also {
            recyclerView = it
            it.layoutManager = LinearLayoutManager(
                this@MusicActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            it.adapter = MusicAdapter().also { adapter -> this@MusicActivity.adapter = adapter }
        }
    }

    private fun initViewModel() {
        viewModel = MusicViewModel.INSTANCE
        viewModel.musicLiveData.observe(this) { result ->
            result.data?.let {

            }
            result.error?.let {
                errorToast(it.message)
            }
        }
        viewModel.musicsLiveData.observe(this) { result ->
            result.data?.let { adapter.submitList(it) }
            result.error?.let { errorToast(it.message) }
        }
    }
}

