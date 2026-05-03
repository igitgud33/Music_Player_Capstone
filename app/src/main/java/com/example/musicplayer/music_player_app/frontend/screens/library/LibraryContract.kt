package com.example.musicplayer.music_player_app.frontend.screens.library

interface LibraryContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun displayLibrary(playlists: List<PlaylistInfo>)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadLibrary()
        fun onDestroy()
    }

    interface Model {
        fun fetchPlaylists(callback: (List<PlaylistInfo>?, Throwable?) -> Unit)
    }
}