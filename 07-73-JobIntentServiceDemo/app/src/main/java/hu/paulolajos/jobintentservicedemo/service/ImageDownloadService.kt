package hu.paulolajos.jobintentservicedemo.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Message
import android.os.Messenger
import androidx.core.app.JobIntentService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ImageDownloadService : JobIntentService() {

    companion object {
        const val KEY_IMAGE_URL = "KEY_IMAGE_URL"
        const val KEY_MESSENGER = "KEY_MESSENGER"

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ImageDownloadService::class.java, 1, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val imageUrl = intent.getStringExtra(KEY_IMAGE_URL)

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?,
                                          target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?,
                                             target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    val messenger = intent.extras!!.get(KEY_MESSENGER) as Messenger

                    val msg = Message.obtain()
                    msg.arg1 = Activity.RESULT_OK
                    msg.obj = resource

                    messenger.send(msg)

                    return true
                }

            }).submit()

        Thread.sleep(10000)
    }
}