package com.example.musicplayer.music_player_app.frontend.screens.register

import com.example.musicplayer.music_player_app.backend.app.CustomApp

class RegisterModel(private val app: CustomApp) {
    fun isUsernameTaken(username: String): Boolean {
        return username.equals(app.defaultUsername, ignoreCase = true)
    }

    fun register(username: String, password: String) {
        app.defaultUsername = username
        app.defaultPassword = password
    }
}