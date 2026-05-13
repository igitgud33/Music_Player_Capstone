package com.example.musicplayer.music_player_app.frontend.screens.dashboard

interface DashboardContract {
    interface View {
        fun displayUsername(message: String)
    }

    interface Presenter {
        fun initializeUsername()
    }

    interface Model {
    }
}