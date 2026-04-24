package com.example.musicplayer.music_player_app.frontend.screens.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.app.CustomApp
import com.example.musicplayer.music_player_app.frontend.screens.login.LoginActivity

class DashboardActivity : Activity(), DashboardContract.View {

    private lateinit var dashboardPresenter: DashboardPresenter
    private lateinit var textViewWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val buttonBackToLogin = findViewById<Button>(R.id.buttonBackToLogin)

        textViewWelcome = findViewById<TextView>(R.id.textviewUser)

        dashboardPresenter = DashboardPresenter(this, DashboardModel(application as CustomApp))
        dashboardPresenter.initializeUsername()

        buttonBackToLogin.setOnClickListener{
            val backToLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(backToLoginIntent)
            finish();
        }
    }

    override fun displayUsername(message: String) {
        textViewWelcome.setText(message)
    }
}