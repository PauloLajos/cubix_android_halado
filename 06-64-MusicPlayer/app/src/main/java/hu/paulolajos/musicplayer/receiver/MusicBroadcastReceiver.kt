package hu.paulolajos.musicplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hu.paulolajos.musicplayer.R
import hu.paulolajos.musicplayer.activities.MusicInterface
import hu.paulolajos.musicplayer.data.exitApplicationNotification
import hu.paulolajos.musicplayer.data.getImageArt
import hu.paulolajos.musicplayer.data.setSongPosition
import hu.paulolajos.musicplayer.utils.ApplicationClass
import hu.paulolajos.musicplayer.fragments.NowPlayingFragment

class MusicBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ApplicationClass.PREVIOUS -> {
                prevNextMusic(increment = false, context = context!!)
            }

            ApplicationClass.PLAY -> {
                if (MusicInterface.isPlaying) pauseMusic() else playMusic()
            }

            ApplicationClass.NEXT -> {
                prevNextMusic(increment = true, context = context!!)
                MusicInterface.counter--
            }

            ApplicationClass.EXIT -> {
                exitApplicationNotification()
            }
        }
    }

    private fun playMusic() {
        MusicInterface.musicService!!.audioManager.requestAudioFocus(
            MusicInterface.musicService, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
        )
        MusicInterface.isPlaying = true
        MusicInterface.binding.interfacePlay.setImageResource(R.drawable.pause)
        MusicInterface.musicService!!.mediaPlayer!!.start()
        MusicInterface.musicService!!.showNotification(R.drawable.pause_notification)
        NowPlayingFragment.binding.fragmentButton.setImageResource(R.drawable.pause_now)
    }

    fun prevNextMusic(increment: Boolean, context: Context) {
        try {
            setSongPosition(increment = increment)
            MusicInterface.musicService!!.initSong()
            Glide.with(context)
                .load(getImageArt(MusicInterface.musicList[MusicInterface.songPosition].path))
                .apply(
                    RequestOptions().placeholder(R.drawable.image_as_cover).centerCrop()
                )
                .into(MusicInterface.binding.interfaceCover)

            MusicInterface.binding.interfaceSongName.text =
                MusicInterface.musicList[MusicInterface.songPosition].title
            MusicInterface.binding.interfaceArtistName.text =
                MusicInterface.musicList[MusicInterface.songPosition].album
            Glide.with(context)
                .load(getImageArt(MusicInterface.musicList[MusicInterface.songPosition].path))
                .apply(
                    RequestOptions().placeholder(R.drawable.image_as_cover).centerCrop()
                )
                .into(NowPlayingFragment.binding.fragmentImage)
            NowPlayingFragment.binding.fragmentTitle.text =
                MusicInterface.musicList[MusicInterface.songPosition].title
            NowPlayingFragment.binding.fragmentAlbumName.text =
                MusicInterface.musicList[MusicInterface.songPosition].album
            playMusic()

        } catch (e: Exception) {
            Log.e("AdapterView", e.toString())
        }
    }

    private fun pauseMusic() {
        MusicInterface.musicService!!.audioManager.abandonAudioFocus(MusicInterface.musicService)
        MusicInterface.isPlaying = false
        MusicInterface.binding.interfacePlay.setImageResource(R.drawable.play)
        MusicInterface.musicService!!.mediaPlayer!!.pause()
        MusicInterface.musicService!!.showNotification(R.drawable.play_notification)
        NowPlayingFragment.binding.fragmentButton.setImageResource(R.drawable.play_now)
    }
}