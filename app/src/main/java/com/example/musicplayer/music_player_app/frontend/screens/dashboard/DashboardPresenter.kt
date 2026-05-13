package com.example.musicplayer.music_player_app.frontend.screens.dashboard


class DashboardPresenter(private val view: DashboardContract.View, private val model: DashboardModel): DashboardContract.Presenter {
    override fun initializeUsername() {
        val username = model.getUsername()

        if(!username.isNullOrEmpty()){
            view.displayUsername("Good day, $username")
        }
        else{
            view.displayUsername("Good day, user")
        }
    }
}