# Implement onPlaylistSelected in MediaPlayerPresenter

This plan outlines the steps to implement `onPlaylistSelected` in `MediaPlayerPresenter` to fetch tracks from a selected playlist and start playback using the `MusicService`.

## User Review Required

- **Error Handling**: Currently, `MediaPlayerContract.View` does not have a method to show error messages or empty states. I propose adding `showError(message: String)` to the `View` interface.
- **Model Interface**: The `MediaPlayerContract.Model` uses callbacks. I will use these callbacks within a coroutine-friendly way or directly if they already handle thread switching (though the requirement specifies using a coroutine on `Dispatchers.IO`).

## Proposed Changes

### MediaPlayer Component

#### [MediaPlayerContract.kt](file:///C:/Project_dev/Android%20development/MusicPlayer/app/src/main/java/com/example/musicplayer/music_player_app/frontend/screens/mediaplayer/MediaPlayerContract.kt)

- Add `showError(message: String)` to the `View` interface.

```kotlin
    interface View {
        fun updateSongInfo(title: String, artist: String)
        fun setPlayPauseIcon(isPlaying: Boolean)
        fun updateProgress(currentMinSec: Int, durationMinSec: Int)
        fun updatePlaybackMode(mode: com.example.musicplayer.music_player_app.backend.service.MusicService.PlaybackMode)
        fun showAddSongDialog()
        fun showError(message: String) // NEW
    }
```

#### [MediaPlayerPresenter.kt](file:///C:/Project_dev/Android%20development/MusicPlayer/app/src/main/java/com/example/musicplayer/music_player_app/frontend/screens/mediaplayer/MediaPlayerPresenter.kt)

- Implement `onPlaylistSelected(playlist: Playlist)`.
- Use `scope.launch` to fetch songs via `model.fetchSongsForPlaylist`.
- Since the Model's `fetchSongsForPlaylist` already uses `withContext(Dispatchers.IO)` and a callback, I will wrap it in a `suspendCancellableCoroutine` or simply call it from the Main scope if it handles everything. Wait, the requirement says "fetch the tracks... using a coroutine on Dispatchers.IO".
- Update `MusicService` with the new song list and start playback.
- Update UI on the main thread.

```kotlin
    override fun onPlaylistSelected(playlist: Playlist) {
        scope.launch {
            try {
                // Fetch tracks on IO dispatcher
                val songs = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine<List<Song>> { continuation ->
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

                // Update MusicService and start playback
                musicService.setPlaylist(songs, 0)
                musicService.playCurrent(true)

                // Update UI
                val firstSong = songs[0]
                view?.updateSongInfo(firstSong.title, firstSong.artist)
                view?.setPlayPauseIcon(true)

            } catch (e: Exception) {
                view?.showError(e.message ?: "Failed to load playlist")
            }
        }
    }
```

## Verification Plan

### Automated Tests
- I will check if there are existing tests for `MediaPlayerPresenter`. If so, I'll add a test case for `onPlaylistSelected`.
- Command: `./gradlew :app:testDebugUnitTest`

### Manual Verification
- Deploy the app and select a playlist in the Media Player.
- Verify that playback starts with the first song of the playlist.
- Verify that song info is updated correctly.
- Verify that an error message is shown if the playlist is empty (after implementing `showError` in the View implementation).
