package com.example.musicplayer.music_player_app.frontend.screens.library

class LibraryModel : LibraryContract.Model {
    override fun fetchPlaylists(callback: (List<PlaylistInfo>?, Throwable?) -> Unit) {
        //Sampling music playlist
        val mockPlaylists = listOf(
            PlaylistInfo("Favorites", 24, ""),
            PlaylistInfo("Workout Vibes", 15, ""),
            PlaylistInfo("Late Night Drive", 42, ""),
            PlaylistInfo("Chill Study", 8, "")
        )

        callback(mockPlaylists, null)
    }
}