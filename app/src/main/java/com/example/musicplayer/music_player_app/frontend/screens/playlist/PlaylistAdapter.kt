package com.example.musicplayer.music_player_app.frontend.screens.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R

class PlaylistAdapter(private val songs: List<Song>) : RecyclerView.Adapter<PlaylistAdapter.SongViewHolder>() {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textSongTitle: TextView = view.findViewById(R.id.textSongTitle)
        val textArtistName: TextView = view.findViewById(R.id.textArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_placeholder, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.textSongTitle.text = song.title
        holder.textArtistName.text = song.artist
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}