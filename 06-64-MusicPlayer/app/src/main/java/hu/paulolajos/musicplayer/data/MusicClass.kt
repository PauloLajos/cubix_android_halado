package hu.paulolajos.musicplayer.data

import android.app.Service.STOP_FOREGROUND_DETACH
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.util.Log
import hu.paulolajos.musicplayer.activities.MusicInterface
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.exitProcess

data class MusicClass(
    val id: String,
    val title: String,
    val album: String,
    val length: Long = 0,
    val artist: String,
    val path: String,
    val artUri: String
)

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(
        duration, TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

fun getImageArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

fun exitApplication() {
    if (MusicInterface.musicService != null) {
        MusicInterface.musicService!!.audioManager.abandonAudioFocus(MusicInterface.musicService)
        MusicInterface.musicService!!.stopForeground(STOP_FOREGROUND_DETACH)
        MusicInterface.musicService!!.mediaPlayer!!.release()
        MusicInterface.musicService = null
    }
    exitProcess(1)
}

fun exitApplicationNotification() {
    if (MusicInterface.isPlaying) {
        val musicInterface = MusicInterface()
        musicInterface.pauseMusic()
    }
    MusicInterface.musicService!!.stopForeground(STOP_FOREGROUND_DETACH)
}

fun setSongPosition(increment: Boolean) {
    if (!MusicInterface.isRepeating) {
        if (increment) {
            if (MusicInterface.musicList.size - 1 == MusicInterface.songPosition) {
                MusicInterface.songPosition = 0
            } else ++MusicInterface.songPosition
        } else {
            if (0 == MusicInterface.songPosition) MusicInterface.songPosition =
                MusicInterface.musicList.size - 1
            else --MusicInterface.songPosition
        }
    }
}

fun getMainColor(img: Bitmap): Int {
    val newImg = Bitmap.createScaledBitmap(img, 1, 1, true)
    val color = newImg.getPixel(0, 0)
    newImg.recycle()
    return manipulateColor(color, 0.4.toFloat())
}

fun manipulateColor(color: Int, factor: Float): Int {
    val a: Int = Color.alpha(color)
    val r = (Color.red(color) * factor).roundToInt()
    val g = (Color.green(color) * factor).roundToInt()
    val b = (Color.blue(color) * factor).roundToInt()
    return Color.argb(
        a,
        r.coerceAtMost(255),
        g.coerceAtMost(255),
        b.coerceAtMost(255)
    )
}