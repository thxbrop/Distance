package com.unltm.distance.ui.live.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.r0adkll.slidr.Slidr
import com.unltm.distance.base.contracts.loadHTTPS
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.databinding.ActivityPlayerBinding
import com.unltm.distance.ui.live.LivePreview
import com.unltm.distance.ui.live.UnsupportedLiveRoomException

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var player: ExoPlayer
    private lateinit var mediaItem: MediaItem

    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var mediaSource: MediaSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBase()
        binding.init()
        initIntent()
        initObserver()
    }

    private fun initBase() {
        BarUtils.transparentStatusBar(this)
        viewModel = PlayerViewModel.INSTANCE
        player = ExoPlayer.Builder(this).build()
        dataSourceFactory = DefaultHttpDataSource.Factory()
        Slidr.attach(this)
    }

    private fun initIntent() {
        val room = intent.getSerializableExtra(INTENT_ROOM) as LivePreview
        try {
            viewModel.getRealUri(room.roomId, room.com)
        } catch (e: UnsupportedLiveRoomException) {
            showErrorToast(e.message)
            onBackPressedDispatcher.onBackPressed()
        }
        binding.apply {
            ownerName.text = room.ownerName
            avatar.loadHTTPS(room.avatar)
        }
    }

    private fun initObserver() {
        viewModel.realUriLive.observe(this) { result ->
            result.data?.let {
                mediaItem = MediaItem.fromUri(it)
                mediaSource =
                    ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
                loadRoom(mediaSource)
            }
            result.error?.let {
                showErrorToast(it.message)
            }
        }
    }

    private fun loadRoom(mediaSource: MediaSource) {
        player.playWhenReady = true
        player.setMediaSource(mediaSource)
        player.prepare()
    }

    private fun ActivityPlayerBinding.init() {
        playerView.player = player
        playerView.useController = false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearAll()
        player.release()
    }

    companion object {
        const val INTENT_ROOM = "INTENT_ROOM"
    }
}

