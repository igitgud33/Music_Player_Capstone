package com.example.musicplayer.music_player_app.frontend.screens.library

class LibraryPresenter(
    private var view: LibraryContract.View?,
    private val model: LibraryContract.Model
) : LibraryContract.Presenter {

    override fun loadLibrary() {
        view?.showLoading()

        model.fetchPlaylists { playlists, error ->
            view?.hideLoading()

            if (error != null) {
                view?.showError(error.message ?: "Failed to load library.")
            } else if (!playlists.isNullOrEmpty()) {
                view?.displayLibrary(playlists)
            } else {
                view?.showError("Your library is empty.")
            }
        }
    }

    override fun onDestroy() {
        view = null
    }
}