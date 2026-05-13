package com.example.musicplayer.music_player_app.frontend.screens.playlist

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R

class PlaylistActivity : AppCompatActivity(), PlaylistContract.View {

    private lateinit var presenter: PlaylistPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        recyclerView = findViewById(R.id.recyclerViewPlaylist)
        progressBar = findViewById(R.id.progressBarPlaylist)

        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter = PlaylistPresenter(this, PlaylistModel())

        presenter.loadPlaylist()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun displayPlaylist(songs: List<Song>) {
        val adapter = PlaylistAdapter(songs)
        recyclerView.adapter = adapter
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}