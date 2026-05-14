package com.example.musicplayer.music_player_app.frontend.screens.login

class LoginPresenter(private val view: LoginContract.View, private val model: LoginModel): LoginContract.Presenter {

    override fun login(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            model.validateUser(username, password) { isValid, user ->
                if (isValid) {
                    view.showSuccess()
                    view.showHome()
                } else {
                    view.showInvalidCredentials()
                }
            }
        } else {
            view.showEmptyFields()
        }
    }

    // Temporary helper to allow registration from UI if needed
    fun register(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            model.registerUser(username, password) { success, error ->
                if (success) {
                    view.showSuccess() // or a different message
                } else {
                    // view.showError(error ?: "Registration failed")
                }
            }
        }
    }
}
