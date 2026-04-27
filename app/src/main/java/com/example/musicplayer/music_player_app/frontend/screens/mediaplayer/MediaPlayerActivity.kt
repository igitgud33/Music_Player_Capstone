package com.example.musicplayer.music_player_app.frontend.screens.mediaplayer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
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

           // presenter = MediaPlayerPresenter(this@MediaPlayerActivity, musicService)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        presenter.onDestroy()
    }

    override fun updateSongInfo(title: String, artist: String) {
        //nun
    }

    override fun setPlayPauseIcon(isPlaying: Boolean) {
        //nun
    }

    override fun updateProgress(currentMinSec: Int, durationMinSec: Int) {
        //nun
    }
}