package com.example.musicplayer.music_player_app.frontend.screens.playlist

interface PlaylistContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun displayPlaylist(songs: List<Song>)
        fun showError(message: String)
        fun showUploadSuccess()
        fun showUploadError(message: String)
    }

    interface Presenter {
        fun loadPlaylist(playlistId: Int)
        fun uploadSong(uriString: String, playlistId: Int)
        fun onDestroy()
    }

    interface Model {
        fun saveSongToDatabase(uriString: String, playlistId: Int, callback: (Boolean, String?) -> Unit)
        fun getSongsFromDatabase(playlistId: Int, callback: (List<Song>) -> Unit)
    }
}