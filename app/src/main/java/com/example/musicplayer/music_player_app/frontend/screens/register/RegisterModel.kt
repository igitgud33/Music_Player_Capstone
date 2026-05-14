package com.example.musicplayer.music_player_app.frontend.screens.register

import android.content.Context
import com.example.musicplayer.music_player_app.backend.data.AppDatabase
import com.example.musicplayer.music_player_app.backend.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterModel(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()

    fun isUsernameTaken(username: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserByUsername(username)
            withContext(Dispatchers.Main) {
                callback(user != null)
            }
        }
    }

    fun register(username: String, password: String, callback: (Boolean, String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newUser = User(username = username, password = password)
                userDao.insertUser(newUser)
                withContext(Dispatchers.Main) {
                    callback(true, null)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(false, e.message)
                }
            }
        }
    }
}
