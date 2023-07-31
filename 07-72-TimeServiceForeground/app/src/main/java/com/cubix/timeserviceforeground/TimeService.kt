package com.cubix.timeserviceforeground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeService : Service() {

    private val NOTIFICATION_CHANNEL_ID = "time_service_notifications"
    private val NOTIFICATION_CHANNEL_NAME = "Time Service notifications"
    private val NOTIF_FOREGROUND_ID = 101

    var serviceEnabled = false

    inner class TimeThread : Thread() {
        override fun run() {

            // access to MainActivity from service
            val handlerMain = Handler(Looper.getMainLooper())

            while (serviceEnabled) {
                // Toast send MainActivity

                val dateTime = SimpleDateFormat(
                    "yyyy.MM.dd. HH:mm:ss",
                    Locale.ROOT
                ).format(
                    Date(System.currentTimeMillis())
                )

                handlerMain.post {
                    Toast.makeText(
                        this@TimeService,
                        dateTime,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                updateNotification(dateTime)

                sleep(5000)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(NOTIF_FOREGROUND_ID, getMyNotification("Hello foreground"))
        serviceEnabled = true
        TimeThread().start()

        return START_STICKY
    }

    private fun updateNotification(text: String) {
        val notification = getMyNotification(text)
        val notifMan = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notifMan?.notify(NOTIF_FOREGROUND_ID, notification)
    }

    private fun getMyNotification(text: String): Notification {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this,
            NOTIF_FOREGROUND_ID,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(
            this, NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle("This the MyTimeService")
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVibrate(longArrayOf(1000, 2000, 1000))
            .setContentIntent(contentIntent).build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()

        // stop thread
        serviceEnabled = false
        stopForeground(STOP_FOREGROUND_DETACH)
    }
}