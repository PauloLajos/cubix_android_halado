package hu.paulolajos.foregroundservicewithnotificationchannel

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MyService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) when (action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    startForegroundService()
                    Toast.makeText(
                        applicationContext,
                        "Foreground service is started.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopForegroundService()
                    Toast.makeText(
                        applicationContext,
                        "Foreground service is stopped.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                ACTION_PLAY -> Toast.makeText(
                    applicationContext,
                    "You click Play button.",
                    Toast.LENGTH_LONG
                ).show()

                ACTION_PAUSE -> Toast.makeText(
                    applicationContext,
                    "You click Pause button.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.")
        createNotificationChannel("my_service", "My Background Service")
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {

        val resultIntent = Intent(this, MainActivity::class.java)

        // Create the TaskStackBuilder and add the intent, which inflates the back stack

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(resultIntent)

        val resultPendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val chan =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)

        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(resultPendingIntent) //intent
            .build()

        val notificationManager = NotificationManagerCompat.from(this)



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(1, notificationBuilder.build())

        startForeground(1, notification)
    }


    private fun stopForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.")

        // Stop foreground service and remove the notification.
        stopForeground(STOP_FOREGROUND_REMOVE)

        // Stop the foreground service.
        stopSelf()
    }

    companion object {
        const val TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE"
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_PLAY = "ACTION_PLAY"
    }
}