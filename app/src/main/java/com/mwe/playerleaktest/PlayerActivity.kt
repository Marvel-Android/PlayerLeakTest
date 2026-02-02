package com.mwe.playerleaktest

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class PlayerActivity : AppCompatActivity() {
    lateinit var file: File
    var player: ExoPlayer? = null
    lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        file = intent.getSerializableExtra("file", File::class.java)!!

        playerView = findViewById(R.id.playerView)
    }

    override fun onStart() {
        super.onStart()
        startPlayer(Uri.fromFile(file))
    }

    override fun onResume() {
        super.onResume()
        player?.play()

        lifecycleScope.launch {
            delay(10000)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun startPlayer(uri: Uri) {
        player = ExoPlayer.Builder(this)
            .setTrackSelector(DefaultTrackSelector(this))
            .build()

        player?.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        player?.addMediaItem(MediaItem.fromUri(uri))
        player?.prepare()
        player?.playWhenReady = true

        playerView.setPlayer(player)
        playerView.onResume()
    }

    private fun releasePlayer() {
        playerView.onPause()
        player?.stop()
        player?.release()
        player = null
    }
}