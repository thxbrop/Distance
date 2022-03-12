package com.unltm.distance.ui.live.player

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.blankj.utilcode.util.BarUtils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.r0adkll.slidr.Slidr
import com.unltm.distance.R
import com.unltm.distance.base.ServerException
import com.unltm.distance.base.contracts.loadHTTPS
import com.unltm.distance.base.contracts.showErrorToast
import com.unltm.distance.databinding.ActivityPlayerBinding
import com.unltm.distance.ui.live.LivePreview

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var player: ExoPlayer
    private lateinit var mediaItem: MediaItem
    private lateinit var errorPage: ConstraintLayout
    private lateinit var errorPageMessage: TextView
    private lateinit var errorPageRetry: Button

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
        } catch (e: ServerException) {
            showErrorToast(e.message)
            onBackPressedDispatcher.onBackPressed()
        }
        binding.apply {
            ownerName.text = room.ownerName
            avatar.loadHTTPS(room.avatar)
        }
    }

    private fun initObserver() {
        viewModel.realUrlLive.observe(this) { result ->
            result.data?.let {
                mediaItem = MediaItem.fromUri(it)
                mediaSource =
                    ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
                loadRoom(mediaSource)
            }
            result.error?.let {
                errorPage.isVisible = true
                errorPageMessage.text = it.message
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
        this@PlayerActivity.errorPage = errorPage
        this@PlayerActivity.errorPageMessage = errorPage.findViewById(R.id.message)
        this@PlayerActivity.errorPageRetry = errorPage.findViewById<Button?>(R.id.retry).apply {
            setOnClickListener {
                this@PlayerActivity.errorPage.isVisible = false
                initIntent()
            }
        }
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

