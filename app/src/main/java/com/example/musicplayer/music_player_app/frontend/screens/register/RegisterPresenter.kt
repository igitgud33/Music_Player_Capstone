package com.example.musicplayer.music_player_app.frontend.screens.register

class RegisterPresenter(private val view: RegisterContract.View, private val model: RegisterModel) : RegisterContract.Presenter {

    override fun register(username: String, password: String, confirmPassword: String) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            view.showEmptyField()
            return
        }

        if (password != confirmPassword) {
            view.showPasswordMismatch()
            return
        }

        model.isUsernameTaken(username) { taken ->
            if (taken) {
                view.showUsernameTaken()
            } else {
                model.register(username, password) { success, error ->
                    if (success) {
                        view.showSuccess()
                        view.goToLogin()
                    } else {
                        // view.showError(error ?: "Registration failed")
                    }
                }
            }
        }
    }
}
