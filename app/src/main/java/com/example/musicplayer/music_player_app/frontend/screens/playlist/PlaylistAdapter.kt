package com.example.musicplayer.music_player_app.frontend.screens.playlist

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.data.AppDatabase
import com.example.musicplayer.music_player_app.frontend.screens.mediaplayer.MediaPlayerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistAdapter(
    private val songs: List<Song>,
    private val onSongDeleted: () -> Unit // Tells the Activity to refresh the screen
) : RecyclerView.Adapter<PlaylistAdapter.SongViewHolder>() {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textSongTitle: TextView = view.findViewById(R.id.textSongTitle)
        val textSongArtist: TextView = view.findViewById(R.id.textSongArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_placeholder, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.textSongTitle.text = song.title
        holder.textSongArtist.text = song.artist

        // STANDARD TAP: Open the Media Player (Left exactly as you had it!)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MediaPlayerActivity::class.java)
            intent.putExtra("SONG_TITLE", song.title)
            intent.putExtra("SONG_ARTIST", song.artist)
            intent.putExtra("SONG_URI", song.fileUri)
            it.context.startActivity(intent)
        }

        // LONG PRESS: Pop up the Delete Confirmation
        holder.itemView.setOnLongClickListener { contextView ->
            AlertDialog.Builder(contextView.context)
                .setTitle("Delete Song")
                .setMessage("Are you sure you want to remove '${song.title}' from this playlist?")
                .setPositiveButton("Delete") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        // 1. Delete from Room Database
                        AppDatabase.getDatabase(contextView.context).songDao().deleteSong(song)
                        // 2. Tell the UI to refresh
                        withContext(Dispatchers.Main) {
                            onSongDeleted()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
            true // 'true' means we successfully handled the long press
        }
    }

    override fun getItemCount(): Int = songs.size
}