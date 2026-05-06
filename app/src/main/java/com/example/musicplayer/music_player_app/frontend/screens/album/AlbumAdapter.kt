package com.example.musicplayer.music_player_app.frontend.screens.album

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.music_player_app.backend.data.Album

// handles RecyclerView by filling item_album components w/ corresponding Name & Artist

class AlbumAdapter(
    private var albums: List<Album>,
    private val onAlbumClick: (Album) -> Unit
): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    // finds views in item_album
    class AlbumViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAlbumName)
        val tvArtist: TextView = view.findViewById(R.id.tvAlbumArtist)
    }

    // create Card that inflates item_album
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    // put data to Card
    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = albums[position]
        holder.tvName.text = album.name
        holder.tvArtist.text = album.artist

        // handle click
        holder.itemView.setOnClickListener {
            onAlbumClick(album)
        }
    }

    override fun getItemCount() = albums.size

    // helps refresh list when data changes
    fun updateData(newAlbums: List<Album>) {
        this.albums = newAlbums
        notifyDataSetChanged()
    }


}
