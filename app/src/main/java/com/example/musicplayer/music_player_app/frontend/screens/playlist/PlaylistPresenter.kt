package com.example.musicplayer.music_player_app.frontend.screens.playlist

class PlaylistPresenter(
    private var view: PlaylistContract.View?,
    private val model: PlaylistContract.Model
) : PlaylistContract.Presenter {

    override fun loadPlaylist() {
        view?.showLoading()

        model.fetchSongs { songs, error ->
            view?.hideLoading()

            if (error != null) {
                view?.showError(error.message ?: "An unknown error occurred while loading the playlist.")
            } else if (songs != null && songs.isNotEmpty()) {
                view?.displayPlaylist(songs)
            } else {
                view?.showError("Your playlist is empty.")
            }
        }
    }

    override fun onDestroy() {
        view = null
    }
}