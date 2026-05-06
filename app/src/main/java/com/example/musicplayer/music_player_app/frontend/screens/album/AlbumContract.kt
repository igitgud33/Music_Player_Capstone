package com.example.musicplayer.music_player_app.frontend.screens.album

import com.example.musicplayer.music_player_app.backend.data.Album

class AlbumContract {
    interface View {
        fun showAlbums(albums: List<Album>)
        fun showLoading()
        fun hideLoading()
        fun showError(throwable: String)
        fun toAlbumDetails(album: Album)
    }

    interface Presenter {
        fun loadAlbums()
        fun onAlbumClick(album: Album)
        fun addAlbum(name: String, artist: String)
    }

    interface Model {
        // pass function type (fun as parameter), input list of album or error, Unit = void
        fun fetchAlbums(callback: (List<Album>?, Throwable?) -> Unit)
        fun insertAlbum(album: Album, callback: (List<Album>?, Throwable?) -> Unit)
    }
}