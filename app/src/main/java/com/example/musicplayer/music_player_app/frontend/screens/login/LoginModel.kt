package com.example.musicplayer.music_player_app.frontend.screens.login

import com.example.musicplayer.music_player_app.backend.app.CustomApp
import com.example.musicplayer.music_player_app.backend.data.User

class LoginModel(private val app: CustomApp) {

    fun validate(username: String, password: String): Boolean {
        return username.equals(app.defaultUsername, false) && password.equals(app.defaultPassword, false)
    }

    fun saveData(username: String, password: String) {
        app.loginUser = User(username, password)
    }
}