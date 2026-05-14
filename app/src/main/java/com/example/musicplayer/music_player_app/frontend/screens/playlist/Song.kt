package com.example.musicplayer.music_player_app.frontend.screens.playlist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.musicplayer.music_player_app.backend.data.Playlist

@Entity(
    tableName = "songs",
    foreignKeys = [
        ForeignKey(
            entity = Playlist::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("playlistId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var artist: String,
    var fileUri: String,
    val playlistId: Int
)