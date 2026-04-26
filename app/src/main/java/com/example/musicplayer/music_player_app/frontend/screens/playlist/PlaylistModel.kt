package com.example.musicplayer.music_player_app.frontend.screens.playlist

class PlaylistModel : PlaylistContract.Model {
    override fun fetchSongs(callback: (List<Song>?, Throwable?) -> Unit) {
        val mockSongs = listOf(
            Song("Bohemian Rhapsody", "Queen", ""),
            Song("Hotel California", "Eagles", ""),
            Song("Stairway to Heaven", "Led Zeppelin", "")
        )

        callback(mockSongs, null)
    }
}