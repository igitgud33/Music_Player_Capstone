package com.example.musicplayer.music_player_app.frontend.screens.playlist

class PlaylistPresenter(
    private val view: PlaylistContract.View,
    private val model: PlaylistContract.Model
) : PlaylistContract.Presenter {

    override fun loadPlaylist(playlistId: Int) {
        view.showLoading()
        model.getSongsFromDatabase(playlistId) { songs ->
            view.hideLoading()
            view.displayPlaylist(songs)
        }
    }

    override fun uploadSong(uriString: String, playlistId: Int) {
        model.saveSongToDatabase(uriString, playlistId) { success, error ->
            if (success) {
                view.showUploadSuccess()
            } else {
                view.showUploadError(error ?: "Unknown error occurred")
            }
        }
    }

    override fun onDestroy() {
        // Handle any cleanup here if necessary
    }
}