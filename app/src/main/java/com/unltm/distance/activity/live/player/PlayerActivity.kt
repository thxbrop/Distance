package com.unltm.distance.activity.live.player

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
import com.unltm.distance.activity.live.LivePreview
import com.unltm.distance.base.contracts.loadHTTPS
import com.unltm.distance.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var player: ExoPlayer

    private lateinit var errorPage: ConstraintLayout
    private lateinit var errorPageMessage: TextView
    private lateinit var errorPageRetry: Button

    private lateinit var dataSourceFactory: DataSource.Factory
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
        viewModel = PlayerViewModel()
        player = ExoPlayer.Builder(this).build()
        dataSourceFactory = DefaultHttpDataSource.Factory()
        Slidr.attach(this)
    }

    private fun initIntent() {
        with(intent.getSerializableExtra(INTENT_ROOM) as LivePreview) {
            viewModel.getRealUri(roomId, com)
            binding.also {
                it.ownerName.text = ownerName
                it.avatar.loadHTTPS(avatar)
            }
        }
    }

    private fun initObserver() {
        viewModel.realUrlLive.observe(this) { result ->
            result.data?.let {
                val mediaItem = MediaItem.fromUri(it)
                val mediaSource =
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
        player.release()
    }

    companion object {
        const val INTENT_ROOM = "INTENT_ROOM"
    }
}

