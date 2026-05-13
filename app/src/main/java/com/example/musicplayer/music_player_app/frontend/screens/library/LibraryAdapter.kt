package com.example.musicplayer.music_player_app.frontend.screens.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R

class LibraryAdapter(private val playlists: List<PlaylistInfo>) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textPlaylistName: TextView = view.findViewById(R.id.textPlaylistName)
        val textSongCount: TextView = view.findViewById(R.id.textSongCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_placeholder, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val playlist = playlists[position]

        holder.textPlaylistName.text = playlist.name
        holder.textSongCount.text = "${playlist.songCount} songs"
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}