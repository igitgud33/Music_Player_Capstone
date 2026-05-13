package com.example.musicplayer.music_player_app.frontend.screens.playlist

import android.content.Context
import com.example.musicplayer.music_player_app.backend.data.AppDatabase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistModel(private val context: Context) : PlaylistContract.Model {

    override fun saveSongToDatabase(uriString: String, playlistId: Int, callback: (Boolean, String?) -> Unit) {
        val songDao = AppDatabase.getDatabase(context).songDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newSong = Song(
                    title = "New Uploaded Song",
                    artist = "Unknown Artist",
                    fileUri = uriString,
                    albumId = 1,
                    playlistId = playlistId
                )

                songDao.insertSong(newSong)

                withContext(Dispatchers.Main) {
                    callback(true, null)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(false, e.message)
                }
            }
        }
    }

    override fun getSongsFromDatabase(playlistId: Int, callback: (List<Song>) -> Unit) {
        val songDao = AppDatabase.getDatabase(context).songDao()

        CoroutineScope(Dispatchers.IO).launch {
            val songs = songDao.getSongsForPlaylist(playlistId)
            withContext(Dispatchers.Main) {
                callback(songs)
            }
        }
    }
}