package com.example.musicplayer.music_player_app.frontend.screens.mediaplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.R
import java.util.concurrent.TimeUnit

class MediaPlayerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var textCurrentTime: TextView
    private lateinit var textTotalTime: TextView
    private lateinit var btnPlayPause: Button
    private lateinit var textTitle: TextView
    private lateinit var textArtist: TextView

    // The handler runs a background timer to update the progress bar every second
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // 1. Bind Views
        seekBar = findViewById(R.id.seekBarPlayer)
        textCurrentTime = findViewById(R.id.textCurrentTime)
        textTotalTime = findViewById(R.id.textTotalTime)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        textTitle = findViewById(R.id.textPlayerTitle)
        textArtist = findViewById(R.id.textPlayerArtist)

        // 2. Catch the data thrown by the PlaylistAdapter
        val title = intent.getStringExtra("SONG_TITLE") ?: "Unknown Title"
        val artist = intent.getStringExtra("SONG_ARTIST") ?: "Unknown Artist"
        val uriString = intent.getStringExtra("SONG_URI")

        textTitle.text = title
        textArtist.text = artist

        // 3. Boot up the Audio Engine
        if (uriString != null) {
            initializePlayer(Uri.parse(uriString))
        } else {
            Toast.makeText(this, "Error: Audio file missing.", Toast.LENGTH_SHORT).show()
        }

        // 4. Play/Pause Click Listener
        btnPlayPause.setOnClickListener {
            togglePlayPause()
        }

        // 5. Let the user drag the slider to scrub through the song
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initializePlayer(uri: Uri) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, uri)
                prepareAsync() // Prepares in the background so it doesn't freeze the screen

                setOnPreparedListener {
                    // When the song is ready, set the timers and hit play
                    seekBar.max = duration
                    textTotalTime.text = formatTime(duration)
                    start()
                    btnPlayPause.text = "Pause"
                    updateSeekBar()
                }

                setOnCompletionListener {
                    // When the song ends, reset the UI
                    btnPlayPause.text = "Play"
                    seekBar.progress = 0
                    textCurrentTime.text = formatTime(0)
                    if (::runnable.isInitialized) handler.removeCallbacks(runnable)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to load audio file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                btnPlayPause.text = "Play"
            } else {
                it.start()
                btnPlayPause.text = "Pause"
                updateSeekBar()
            }
        }
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                seekBar.progress = it.currentPosition
                textCurrentTime.text = formatTime(it.currentPosition)

                // Loop this function every 1000ms (1 second) to move the slider
                runnable = Runnable { updateSeekBar() }
                handler.postDelayed(runnable, 1000)
            }
        }
    }

    // Helper math function to turn raw milliseconds into clean "3:45" text
    private fun formatTime(millis: Int): String {
        return String.format(
            "%d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis.toLong()))
        )
    }

    // Critical: When the user presses the back button, kill the audio engine so it doesn't play forever!
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }
}