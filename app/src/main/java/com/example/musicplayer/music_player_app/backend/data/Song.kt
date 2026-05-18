package com.example.musicplayer.music_player_app.backend.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    foreignKeys = [
        ForeignKey(
            entity = Playlist::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("playlistId"),
            onDelete = ForeignKey.Companion.CASCADE
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