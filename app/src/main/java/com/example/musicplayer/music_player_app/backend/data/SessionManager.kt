package com.example.musicplayer.music_player_app.backend.data

object SessionManager {
    var currentUserId: Int = -1
    var currentUsername: String? = null

    fun login(user: User) {
        currentUserId = user.id
        currentUsername = user.username
    }

    fun logout() {
        currentUserId = -1
        currentUsername = null
    }

    fun isLoggedIn(): Boolean = currentUserId != -1
}
