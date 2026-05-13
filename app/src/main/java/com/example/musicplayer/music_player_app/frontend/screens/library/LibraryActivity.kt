package com.example.musicplayer.music_player_app.frontend.screens.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.data.AppDatabase
import com.example.musicplayer.music_player_app.backend.data.Playlist
import com.example.musicplayer.music_player_app.frontend.screens.playlist.PlaylistActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        recyclerView = findViewById(R.id.recyclerViewLibrary)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = LibraryAdapter(
            playlists = emptyList(),
            onPlaylistClick = { selectedPlaylist ->
                val intent = Intent(this, PlaylistActivity::class.java)
                intent.putExtra("PLAYLIST_ID", selectedPlaylist.id)
                startActivity(intent)
            },
            onPlaylistDeleted = {
                loadPlaylists()
            }
        )
        recyclerView.adapter = adapter

        val btnCreatePlaylist = findViewById<Button>(R.id.btnCreatePlaylist)
        btnCreatePlaylist.setOnClickListener {
            showCreatePlaylistDialog()
        }

        loadPlaylists()
    }

    private fun loadPlaylists() {
        val db = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            // Assuming your DAO has a query to get all playlists. If it's named differently, update this!
            val playlists = db.playlistDao().getAllPlaylists()
            withContext(Dispatchers.Main) {
                adapter.updateData(playlists)
            }
        }
    }

    private fun showCreatePlaylistDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_playlist, null)
        val editTextPlaylistName = dialogView.findViewById<EditText>(R.id.editTextPlaylistName)
        val btnConfirmCreate = dialogView.findViewById<Button>(R.id.btnConfirmCreate)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnConfirmCreate.setOnClickListener {
            val playlistName = editTextPlaylistName.text.toString().trim()
            if (playlistName.isNotEmpty()) {
                savePlaylistToDatabase(playlistName)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun savePlaylistToDatabase(name: String) {
        val db = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newPlaylist = Playlist(name = name, coverUri = "")
                db.playlistDao().insertPlaylist(newPlaylist)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LibraryActivity, "Playlist created!", Toast.LENGTH_SHORT).show()
                    loadPlaylists() // Instantly refresh the screen so the user sees their new creation!
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LibraryActivity, "Error saving playlist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}