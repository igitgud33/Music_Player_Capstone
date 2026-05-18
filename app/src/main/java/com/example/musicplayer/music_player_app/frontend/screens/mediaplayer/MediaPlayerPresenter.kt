package com.example.musicplayer.music_player_app.frontend.screens.mediaplayer

import android.net.Uri
import com.example.musicplayer.music_player_app.backend.data.Playlist
import com.example.musicplayer.music_player_app.backend.service.MusicService
import com.example.musicplayer.music_player_app.backend.data.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MediaPlayerPresenter(
    private var view: MediaPlayerContract.View?,
    private val musicService: MusicService,
    private val model: MediaPlayerContract.Model
) : MediaPlayerContract.Presenter {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    init {
        syncWithService()
        startProgressUpdate()
    }

    private fun startProgressUpdate() {
        scope.launch {
            while (isActive) {
                val current = musicService.getCurrentPosition()
                val duration = musicService.getDuration()
                view?.updateProgress(current, duration)
                view?.setPlayPauseIcon(musicService.isPlaying())

                val currentSong = musicService.getCurrentSong()
                if (currentSong != null) {
                    view?.updateSongInfo(currentSong.title, currentSong.artist)
                }
                delay(1000)
            }
        }
    }

    private fun syncWithService() {
        val currentSong = musicService.getCurrentSong()
        if (currentSong != null) {
            view?.updateSongInfo(currentSong.title, currentSong.artist)
            view?.setPlayPauseIcon(musicService.isPlaying())
            view?.updatePlaybackMode(musicService.getPlaybackMode())
        }
    }

    override fun loadAndPlay(uri: Uri, title: String, artist: String) {
        val song = Song(
            title = title,
            artist = artist,
            fileUri = uri.toString(),
            playlistId = -1
        )
        musicService.playSong(song)
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
        if (!musicService.playNext(isManual = true)) {
            view?.showError("User needs to add more songs")
        }
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
        job.cancel()
        view = null
    }

    override fun onPlaylistSelected(playlist: Playlist) {
        scope.launch {
            try {
                val songs = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine { continuation ->
                        model.fetchSongsForPlaylist(playlist.id) { list, throwable ->
                            if (throwable != null) {
                                continuation.resumeWithException(throwable)
                            } else {
                                continuation.resume(list ?: emptyList())
                            }
                        }
                    }
                }

                if (songs.isEmpty()) {
                    view?.showError("Playlist is empty")
                    return@launch
                }

                musicService.setPlaylist(songs, 0)

                val firstSong = songs[0]
                view?.updateSongInfo(firstSong.title, firstSong.artist)
                view?.setPlayPauseIcon(true)

            } catch (e: Exception) {
                view?.showError(e.message ?: "Failed to load playlist")
            }
        }
    }
}