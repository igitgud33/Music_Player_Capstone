package com.example.musicplayer.music_player_app.frontend.screens.library

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.data.AppDatabase
import com.example.musicplayer.music_player_app.backend.data.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryAdapter(
    private var playlists: List<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit,
    private val onPlaylistDeleted: () -> Unit // Tells the Activity to refresh the screen
) : RecyclerView.Adapter<LibraryAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textPlaylistName: TextView = view.findViewById(R.id.textPlaylistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_library_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.textPlaylistName.text = playlist.name

        // STANDARD TAP: Open Playlist
        holder.itemView.setOnClickListener {
            onPlaylistClick(playlist)
        }

        // LONG PRESS: Pop up the Delete Confirmation
        holder.itemView.setOnLongClickListener { contextView ->
            AlertDialog.Builder(contextView.context)
                .setTitle("Delete Playlist")
                .setMessage("Are you sure you want to completely delete '${playlist.name}'?")
                .setPositiveButton("Delete") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        // 1. Delete from Room Database
                        AppDatabase.getDatabase(contextView.context).playlistDao().deletePlaylist(playlist)
                        // 2. Tell the UI to refresh
                        withContext(Dispatchers.Main) {
                            onPlaylistDeleted()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}