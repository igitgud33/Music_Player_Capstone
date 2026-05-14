package com.example.musicplayer.music_player_app.frontend.screens.mediaplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.service.MusicService

class MediaPlayerActivity: AppCompatActivity(), MediaPlayerContract.View {
    private lateinit var presenter: MediaPlayerContract.Presenter

    // Service implementation
    private var musicService: MusicService? = null

    // allows Activity to use Music Service
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()

            // (!! - assured to be not null)
            presenter = MediaPlayerPresenter(this@MediaPlayerActivity, musicService!!)

            // seekbar init
            seekBar = findViewById<SeekBar>(R.id.seekBarPlayer)

            // execute when user manually moves slider
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        presenter.seekTo(progress)
                    }
                }


                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

            // play/pause btn
            val playPauseBtn = findViewById<Button>(R.id.btnPlayPause)

            playPauseBtn.setOnClickListener {
                presenter.onPlayPauseClick()
            }


        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }
    }

    // seekbar
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // bind to MusicService
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        presenter.onDestroy()
    }

    override fun updateSongInfo(title: String, artist: String) {
        findViewById<TextView>(R.id.textPlayerTitle).text = title
        findViewById<TextView>(R.id.textPlayerArtist).text = artist
    }

    override fun setPlayPauseIcon(isPlaying: Boolean) {
        val playPauseBtn = findViewById<Button>(R.id.btnPlayPause)
        playPauseBtn.text = if(isPlaying) {
            "Pause"
        } else {
            "Play"
        }
    }

    // helper fun to convert millis to 0:00 format
    private fun formatTime(ms: Int): String {
        val mins = (ms / 1000) / 60
        val secs = (ms / 1000) % 60
        return String.format("%d:%02d", mins, secs)
    }

    override fun updateProgress(currentMinSec: Int, durationMinSec: Int) {
        seekBar.max = durationMinSec
        seekBar.progress = currentMinSec

        // update text timer (rudimentary)
        findViewById<TextView>(R.id.textCurrentTime).text = formatTime(currentMinSec)
        findViewById<TextView>(R.id.textTotalTime).text = formatTime(durationMinSec)

    }
}
