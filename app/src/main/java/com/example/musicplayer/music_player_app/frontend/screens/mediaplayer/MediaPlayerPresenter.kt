package com.example.musicplayer.music_player_app.frontend.screens.mediaplayer

import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.example.musicplayer.music_player_app.backend.data.Playlist
import com.example.musicplayer.music_player_app.backend.service.MusicService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MediaPlayerPresenter(
    private var view: MediaPlayerContract.View?,
    private val musicService: MusicService,
    private val model: MediaPlayerContract.Model
) : MediaPlayerContract.Presenter {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            val current = musicService.getCurrentPosition()
            val duration = musicService.getDuration()
            view?.updateProgress(current, duration)
            view?.setPlayPauseIcon(musicService.isPlaying())
            handler.postDelayed(this, 1000)
        }
    }

    init {
        handler.post(updateProgressRunnable)
    }

    override fun loadAndPlay(uri: Uri, title: String, artist: String) {
        musicService.playSong(uri.toString())
        view?.updateSongInfo(title, artist)
        view?.setPlayPauseIcon(true)
    }

    override fun onPlayPauseClick() {
        if (musicService.isPlaying()) {
            musicService.pauseMusic()
        } else {
            musicService.resumeMusic()
        }
        view?.setPlayPauseIcon(musicService.isPlaying())
    }

    override fun onPreviousClick() {
        musicService.playPrevious()
        updateSongInfoFromService()
    }

    override fun onNextClick() {
        musicService.playNext()
        updateSongInfoFromService()
    }

    override fun onShuffleClick() {
        musicService.cyclePlaybackMode()
        view?.updatePlaybackMode(musicService.getPlaybackMode())
    }

    private fun updateSongInfoFromService() {
        val song = musicService.getCurrentSong()
        if (song != null) {
            view?.updateSongInfo(song.title, song.artist)
            view?.setPlayPauseIcon(musicService.isPlaying())
        }
    }

    override fun seekTo(position: Int) {
        musicService.seekTo(position)
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateProgressRunnable)
        job.cancel()
        view = null
    }

    override fun onAddSongToPlaylistClick() {}
    override fun onPlaylistSelected(playlist: Playlist) {}
}