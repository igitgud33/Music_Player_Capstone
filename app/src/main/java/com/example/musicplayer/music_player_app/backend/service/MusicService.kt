package com.example.musicplayer.music_player_app.backend.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder

class MusicService: Service() {
    private var mediaPlayer: MediaPlayer? = null // the built-in tool itself

    private val binder = MusicBinder() // binder to access music service
    inner class MusicBinder: Binder() {
        fun getService(): MusicService = this@MusicService // allows music service funs to work
    }

    override fun onBind(intent: Intent): IBinder = binder // not '?' since a binder is already initialized

    fun playSong(fileUri: String) { // song playing process
        // stops prev song (if any)
        mediaPlayer?.stop() // stops any song currently playing
        mediaPlayer?.release() // restores  memory media player took from device to play prev song

        // play current song
        mediaPlayer = MediaPlayer.create(this, Uri.parse(fileUri)) // inputs song Uri
        mediaPlayer?.start()

    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying?: false // checks if media player isn't null

    // allows play/pause button to be singular
    fun pauseResume() {
        mediaPlayer?.let { // only plays if media player IS NOT NULL
            if(it.isPlaying) it.pause() else it.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}