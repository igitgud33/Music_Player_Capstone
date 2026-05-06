package com.example.musicplayer.music_player_app.frontend.screens.album

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.data.Album
import com.example.musicplayer.music_player_app.frontend.screens.playlist.PlaylistActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlbumActivity: AppCompatActivity(), AlbumContract.View {
    private lateinit var presenter: AlbumContract.Presenter
    private lateinit var adapter: AlbumAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        // init Views
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewAlbum)
        progressBar = findViewById(R.id.progressBarAlbum)
        val fab: FloatingActionButton = findViewById(R.id.fabAddAlbum)

        // setup RecyclerView
        adapter = AlbumAdapter(emptyList()) { album ->
            presenter.onAlbumClick(album)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // init Presenter & load data
        presenter = AlbumPresenter(this, AlbumModel(this))
        presenter.loadAlbums()

        // setup FAB
        fab.setOnClickListener {
            showImportDialog()
        }

    }

    private fun showImportDialog() {
        // inflates dialog_import_album
        val dialogView = layoutInflater.inflate(R.layout.dialog_import_album, null)

        val nameInput = dialogView.findViewById<EditText>(R.id.etAlbumName)
        val artistInput = dialogView.findViewById<EditText>(R.id.etAlbumArtist)

        // builder chain (???)
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Import New Album")
            .setView(dialogView)
            .setPositiveButton("Import") { _, _ ->
                val name = nameInput.text.toString()
                val artist = artistInput.text.toString()

                // save album
                presenter.addAlbum(name, artist)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun showAlbums(albums: List<Album>) {
        adapter.updateData(albums)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(throwable: String) {
        Toast.makeText(this, throwable, Toast.LENGTH_SHORT).show()
    }

    override fun toAlbumDetails(album: Album) {
        val intent = Intent(this, PlaylistActivity::class.java)
        intent.putExtra("ALBUM_ID", album.id)
        intent.putExtra("ALBUM_NAME", album.name)
        startActivity(intent)
    }
}