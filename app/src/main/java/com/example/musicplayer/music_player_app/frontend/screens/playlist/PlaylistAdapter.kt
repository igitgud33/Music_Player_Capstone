package com.example.musicplayer.music_player_app.frontend.screens.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R

class PlaylistAdapter(
    private val onItemClicked: (Song) -> Unit, // Opens full player
    private val onPlayAction: (Song, PlaybackCommand) -> Unit, // Handles inline play/pause
    private val onEditClicked: (Song) -> Unit,
    private val onDeleteClicked: (Song) -> Unit
) : ListAdapter<Song, PlaylistAdapter.ViewHolder>(SongDiffCallback()) {

    enum class PlaybackCommand { PLAY_NEW, RESUME, PAUSE }

    private var activeSongId: Int? = null
    private var isPlaying: Boolean = false

    fun setPlaybackState(songId: Int?, playing: Boolean) {
        val previousActiveId = activeSongId
        activeSongId = songId
        isPlaying = playing
        
        // Refresh items to update icons
        val oldPos = currentList.indexOfFirst { it.id == previousActiveId }
        val newPos = currentList.indexOfFirst { it.id == activeSongId }
        
        if (oldPos != -1) notifyItemChanged(oldPos)
        if (newPos != -1) notifyItemChanged(newPos)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textSongTitle)
        val artist: TextView = view.findViewById(R.id.textSongArtist)
        val buttonPlay: ImageButton = view.findViewById(R.id.buttonPlayItem)
        val buttonEdit: ImageButton = view.findViewById(R.id.buttonEditItem)
        val buttonRemove: ImageButton = view.findViewById(R.id.buttonRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_placeholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = getItem(position)
        holder.title.text = song.title
        holder.artist.text = song.artist

        // Check if this specific row is the one currently playing
        val isThisSongActive = song.id == activeSongId

        // Dynamically change the icon based on the state
        if (isThisSongActive && isPlaying) {
            holder.buttonPlay.setImageResource(android.R.drawable.ic_media_pause)
        } else {
            holder.buttonPlay.setImageResource(android.R.drawable.ic_media_play)
        }

        // 1. Click the row to open the full player
        holder.itemView.setOnClickListener {
            onItemClicked(song)
        }

        // 2. Click the Play button to toggle inline audio
        holder.buttonPlay.setOnClickListener {
            if (isThisSongActive) {
                // Toggle play/pause for the currently active song
                isPlaying = !isPlaying
                notifyItemChanged(position)
                val command = if (isPlaying) PlaybackCommand.RESUME else PlaybackCommand.PAUSE
                onPlayAction(song, command)
            } else {
                // Stop the old song, start this new one
                val previousActiveId = activeSongId
                activeSongId = song.id
                isPlaying = true

                val oldPosition = currentList.indexOfFirst { it.id == previousActiveId }
                if (oldPosition != -1) notifyItemChanged(oldPosition) // Reset old icon
                notifyItemChanged(position) // Set new icon

                onPlayAction(song, PlaybackCommand.PLAY_NEW)
            }
        }

        holder.buttonEdit.setOnClickListener { onEditClicked(song) }
        holder.buttonRemove.setOnClickListener { onDeleteClicked(song) }
    }

    class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
}