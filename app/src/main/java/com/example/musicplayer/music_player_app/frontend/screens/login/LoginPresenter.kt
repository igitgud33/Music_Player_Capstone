package com.example.musicplayer.music_player_app.frontend.screens.login

class LoginPresenter(private val view: LoginContract.View, private val model: LoginModel): LoginContract.Presenter {

    override fun login(username: String, password: String) {
        if(!(username.isNullOrEmpty()) && !(password.isNullOrEmpty())){
            if(model.validate(username, password)){
                model.saveData(username, password)
                view.showSuccess()
                view.showHome()
            }
            else{
                view.showInvalidCredentials()
            }
        }
        else{
            view.showEmptyFields()
        }
    }
}