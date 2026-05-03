package com.example.musicplayer.music_player_app.backend.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.musicplayer.music_player_app.frontend.screens.playlist.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    suspend fun getAllSongs(): List<Song>

    @Query("SELECT * FROM songs WHERE playlistId = :playlistId")
    suspend fun getSongsForPlaylist(playlistId: Int): List<Song>

    @Insert
    suspend fun insertSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)
}