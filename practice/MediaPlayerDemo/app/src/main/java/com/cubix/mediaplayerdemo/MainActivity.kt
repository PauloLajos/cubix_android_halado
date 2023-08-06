package com.cubix.mediaplayerdemo

import android.R
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cubix.mediaplayerdemo.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mediaPlayer: MediaPlayer? = null
    private var duration: Long = 0
    private var minutes: Long = 0
    private var seconds: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivPlay.setOnClickListener {
            // for check the mediaplayer is null
            if (mediaPlayer == null) {
                /**
                 * if media player is null then create a new mediaplayer
                 * */
                mediaPlayer = MediaPlayer.create(this, R.raw.pink)

                /**
                 * for getting the duration of mediaplayer
                 * */
                duration = mediaPlayer!!.duration.toLong()
                minutes = duration / 1000 / 60 //converting into minutes
                seconds = duration / 1000 % 60 //converting into seconds
                binding.tvTotal.text = "$minutes:$seconds"

                /**
                 * start mediaplayer
                 */
                mediaPlayer!!.start()

                /**
                 * setting the max-length of seekbar according to mediaplayre duration
                 */
                binding.seekbar.max = duration.toInt()

                /**
                 * use Timer task for updating the seekbar progress according to mediaplayer
                 * Current position
                 */
                Timer().scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        try {
                            binding.seekbar.progress = mediaPlayer!!.currentPosition
                        } catch (e: Exception) {
                        }
                    }
                }, 0, 1000)

                /**
                 * for reset the mediaplayer when mediaplayer is complete
                 */
                mediaPlayer!!.setOnCompletionListener { mediaPlayer ->
                    var mediaPlayer = mediaPlayer
                    mediaPlayer!!.release()
                }
                Toast.makeText(this, "start mediaplayer", Toast.LENGTH_SHORT).show()
            } else {
                /**
                 * if media player is already created then only play
                 */
                Log.e("play else", "onClick: ")
                mediaPlayer!!.start()
            }
        }

        binding.ivStop.setOnClickListener {
            /* stop media player if is playing */
            Log.e("stop", "onClick: " + mediaPlayer!!.isPlaying());

            if (mediaPlayer!!.isPlaying()) {
                mediaPlayer!!.pause();
                Toast.makeText(this, "Pause mediaplayre", Toast.LENGTH_SHORT).show();
            }
        }

        binding.ivReset.setOnClickListener {
            /**  Reset Media player..*/

            if (mediaPlayer != null) {
                mediaPlayer!!.release();
                mediaPlayer = null
                binding.seekbar.progress = 0

                /**
                 * * set both text to 00:00 when mediaplayer is reset...
                */
                binding.tvStart.text = "00:00"
                binding.tvTotal.text = "00:00"

                Toast.makeText(this, "Reset mediaplayer", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "There is no sound playing play sound first", Toast.LENGTH_SHORT).show();
            }
        }
    }
}