package com.example.musicplayer.music_player_app.frontend.screens.register

class RegisterPresenter(private val view: RegisterContract.View, private val model: RegisterModel) : RegisterContract.Presenter {

    override fun register(username: String, password: String, confirmPassword: String) {
        if (username.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
            view.showEmptyField()
            return
        }

        if (password != confirmPassword) {
            view.showPasswordMismatch()
            return
        }

        if (model.isUsernameTaken(username)) {
            view.showUsernameTaken()
            return
        }

        model.register(username, password)
        view.showSuccess()
        view.goToLogin()
    }
}