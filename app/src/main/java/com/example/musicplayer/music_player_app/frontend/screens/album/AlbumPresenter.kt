package com.example.musicplayer.music_player_app.frontend.screens.album

import com.example.musicplayer.music_player_app.backend.data.Album

class AlbumPresenter(
    private val view: AlbumContract.View,
    private val model: AlbumContract.Model
): AlbumContract.Presenter {
    override fun loadAlbums() {
        // show loader
        view.showLoading()

        // ask Model for data
        model.fetchAlbums { albums, throwable ->
            // hide loader when data arrives
            view.hideLoading()

            // error handling
            if(throwable != null) {
                view.showError(throwable.message?: "Unknown error occurred")
            } else {
                view.showAlbums(albums?: emptyList())
            }

        }
    }

    override fun onAlbumClick(album: Album) {
        // navigate to album details
        view.toAlbumDetails(album)
    }

    override fun addAlbum(name: String, artist: String) {
        // basic validation
        if(name.isBlank() || artist.isBlank()) {
            view.showError("Please complete both fields")
            return
        } else {
            loadAlbums()
        }

        // Album object creation to send to Model to be saved
        val newAlbum = Album(name = name, artist = artist)

        model.insertAlbum(newAlbum) { id, throwable ->
            if(throwable != null) {
                view.showError("Error in saving album")
            } else {
                loadAlbums()
            }

        }

    }


}