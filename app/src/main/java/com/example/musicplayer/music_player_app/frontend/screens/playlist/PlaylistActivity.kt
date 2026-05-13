package com.example.musicplayer.music_player_app.frontend.screens.playlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R

class PlaylistActivity : AppCompatActivity(), PlaylistContract.View {

    private lateinit var presenter: PlaylistPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var playlistId: Int = 1

    private val pickAudioLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            presenter.uploadSong(uri.toString(), playlistId)
        } else {
            Toast.makeText(this, "Upload canceled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        recyclerView = findViewById(R.id.recyclerViewPlaylist)
        progressBar = findViewById(R.id.progressBarPlaylist)
        val btnUploadSong = findViewById<Button>(R.id.btnUploadSong)

        recyclerView.layoutManager = LinearLayoutManager(this)

        playlistId = intent.getIntExtra("PLAYLIST_ID", 1)

        presenter = PlaylistPresenter(this, PlaylistModel(this))
        presenter.loadPlaylist(playlistId)

        btnUploadSong.setOnClickListener {
            pickAudioLauncher.launch("audio/*")
        }
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
        val adapter = PlaylistAdapter(songs) {
            presenter.loadPlaylist(playlistId)
        }
        recyclerView.adapter = adapter
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showUploadSuccess() {
        Toast.makeText(this, "Song saved to database!", Toast.LENGTH_SHORT).show()
        presenter.loadPlaylist(playlistId)
    }

    override fun showUploadError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}