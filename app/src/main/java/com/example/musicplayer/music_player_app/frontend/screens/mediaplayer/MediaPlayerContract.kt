package com.example.musicplayer.music_player_app.frontend.screens.mediaplayer

class MediaPlayerContract {

    // Screen-based
    interface View {
        fun updateSongInfo(title: String, artist: String) // updates to display current song title & artist
        fun setPlayPauseIcon(isPlaying: Boolean) // icon changes based on if song is playing
        fun updateProgress(currentMinSec: Int, durationMinSec: Int) // song progress bar
    }

    // User-based
    interface Presenter {
        fun onPlayPauseClick()
        fun onPreviousClick()
        fun onNextClick()
        fun seekTo(position: Int) // if user drags music progress bar
        fun onDestroy() // stop music when app CLOSES COMPLETELY

    }
}