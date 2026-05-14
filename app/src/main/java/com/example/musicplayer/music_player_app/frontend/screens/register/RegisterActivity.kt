package com.example.musicplayer.music_player_app.frontend.screens.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.app.CustomApp
import com.example.musicplayer.music_player_app.frontend.screens.login.LoginActivity
import com.example.musicplayer.music_player_app.frontend.utils.getEditTextValue
import com.example.musicplayer.music_player_app.frontend.utils.toast

class RegisterActivity : Activity(), RegisterContract.View {
    private lateinit var registerPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerPresenter = RegisterPresenter(this, RegisterModel(this))

        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val textviewLogin = findViewById<TextView>(R.id.textviewLogin)

        buttonRegister.setOnClickListener {
            val username = getEditTextValue(R.id.edittextUsername)
            val password = getEditTextValue(R.id.edittextPassword)
            val confirmPassword = getEditTextValue(R.id.edittextConfirmPassword)

            registerPresenter.register(username, password, confirmPassword)
        }

        textviewLogin.setOnClickListener {
            goToLogin()
        }
    }

    override fun showSuccess() {
        toast("Registration successful!")
    }

    override fun showEmptyField() {
        toast("All fields cannot be empty!")
    }

    override fun showPasswordMismatch() {
        toast("Passwords do not match!")
    }

    override fun showUsernameTaken() {
        toast("Username is already taken!")
    }

    override fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}