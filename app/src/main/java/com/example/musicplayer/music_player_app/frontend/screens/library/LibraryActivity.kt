package com.example.musicplayer.music_player_app.frontend.screens.library

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R

class LibraryActivity : AppCompatActivity(), LibraryContract.View {

    private lateinit var presenter: LibraryPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        recyclerView = findViewById(R.id.recyclerViewLibrary)
        progressBar = findViewById(R.id.progressBarLibrary)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        presenter = LibraryPresenter(this, LibraryModel())
        presenter.loadLibrary()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun displayLibrary(playlists: List<PlaylistInfo>) {
        val adapter = LibraryAdapter(playlists)
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