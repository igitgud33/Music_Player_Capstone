package com.example.musicplayer.music_player_app.frontend.screens.dashboard

import com.example.musicplayer.music_player_app.backend.app.CustomApp

class DashboardModel(private val app: CustomApp) {
    fun getUsername(): String {
        return app.loginUser.username
    }
}