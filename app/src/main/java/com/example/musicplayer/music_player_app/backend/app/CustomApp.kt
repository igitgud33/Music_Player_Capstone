package com.example.musicplayer.music_player_app.backend.app

import android.app.Application
import com.example.musicplayer.music_player_app.backend.data.User

class CustomApp : Application() {
    var defaultUsername: String = "test"
    var defaultPassword: String = "test"
    var loginUser: User = User()

    override fun onCreate() {
        super.onCreate()
    }
}