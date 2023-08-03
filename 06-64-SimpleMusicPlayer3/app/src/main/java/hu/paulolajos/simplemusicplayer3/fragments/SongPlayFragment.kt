package hu.paulolajos.simplemusicplayer3.fragments

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import hu.paulolajos.simplemusicplayer3.R
import hu.paulolajos.simplemusicplayer3.databinding.FragmentSongPlayBinding
import java.util.concurrent.TimeUnit


class SongPlayFragment : Fragment() {
    /**
     * Provides global access to these variables from anywhere in the app
     * via DetailListFragment.<variable> without needing to create
     * a DetailListFragment instance.
     */
    companion object {
        const val SONG = "song"
        const val RESID = "resId"
    }

    private var _binding: FragmentSongPlayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // song title and resource id
    private lateinit var songTitle: String
    private var resId = 0

    // media player object
    private var mediaPlayer: MediaPlayer? = null
    private var playPause: Boolean = false

    // seekbar update
    val seekbarUpdateHandler = Handler()
    private val updateSeekbar: Runnable = object : Runnable {
        override fun run() {
            binding.seekbar.progress = mediaPlayer!!.currentPosition
            binding.interfaceSeekStart.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            seekbarUpdateHandler.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // transitions
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_left)

        // Retrieve the SONG from the Fragment arguments
        arguments?.let {
            songTitle = it.getString(SONG).toString()
            resId = it.getInt(RESID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentSongPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.interfaceSongTitle.text = songTitle
        binding.interfaceArtistName.text = resId.toString()


        // volume seekbar
        startVolumeSeekBar()

        // start play sound
        playSound(resId)

        // song playing position seekbar
        startPositionSeekBar()
    }

    private fun startPositionSeekBar() {
        binding.interfaceSeekStart.text =
            formatDuration(mediaPlayer!!.currentPosition.toLong())

        binding.interfaceSeekEnd.text =
            formatDuration(mediaPlayer!!.duration.toLong())

        binding.seekbar.progress = mediaPlayer!!.currentPosition

        binding.seekbar.max = mediaPlayer!!.duration

        binding.interfacePlay.setOnClickListener {
            playPause = if (playPause) {
                playSound(resId)
                false
            } else {
                pauseSound()
                true
            }
        }

        // seekbar change by user
        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, isUser: Boolean) {
                if (isUser) {
                    try {
                        mediaPlayer!!.seekTo(progress)

                        binding.interfaceSeekStart.text =
                            formatDuration(mediaPlayer!!.currentPosition.toLong())

                    } catch (e: Exception) {
                        return
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // nothing
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                // nothing
            }
        })
    }

    private fun startVolumeSeekBar() {
        // Get the audio manager
        val audioManager = requireActivity().getSystemService(AUDIO_SERVICE) as AudioManager

        // Set the maximum volume of the SeekBar to the maximum volume of the MediaPlayer:
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.max = maxVolume

        // Set the current volume of the SeekBar to the current volume of the MediaPlayer:
        val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.progress = currVolume

        // Add a SeekBar.OnSeekBarChangeListener to the SeekBar:
        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do Nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do Nothing
            }
        })
    }

    // Plays the sound
    private fun playSound(resId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        } else mediaPlayer!!.start()

        // seekbar update start
        seekbarUpdateHandler.postDelayed(updateSeekbar, 0);

        // play button change to pause
        binding.interfacePlay.setImageResource(R.drawable.pause)
    }

    // Pause playback
    private fun pauseSound() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }

        // seekbar update end
        seekbarUpdateHandler.removeCallbacks(updateSeekbar)

        // pause button change to play
        binding.interfacePlay.setImageResource(R.drawable.play)
    }

    // Stops playback
    private fun stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }

        // seekbar update end
        seekbarUpdateHandler.removeCallbacks(updateSeekbar)
    }

    // Destroys the MediaPlayer instance when the app is closed
    override fun onStop() {
        super.onStop()

        stopSound()
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(
            duration, TimeUnit.MILLISECONDS
        ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }
}
