package com.example.musicplayer.music_player_app.frontend.screens.login

class LoginContract {
    interface View{
        fun showSuccess()
        fun showInvalidCredentials()
        fun showEmptyFields()
        fun showHome()
    }

    interface Presenter{
        fun login(username: String, password: String)
    }


}