package com.example.musicplayer.music_player_app.frontend.screens.playlist

interface PlaylistContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun displayPlaylist(songs: List<Song>)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadPlaylist()
        fun onDestroy()
    }

    interface Model {
        fun fetchSongs(callback: (List<Song>?, Throwable?) -> Unit)
    }
}