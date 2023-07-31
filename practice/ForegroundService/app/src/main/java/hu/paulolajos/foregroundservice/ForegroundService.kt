package hu.paulolajos.foregroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {

    private lateinit var notificationManager: NotificationManager

    // onStartCommand can be called multiple times, so we keep track of "started" state manually
    private var isStarted = false

    override fun onCreate() {
        super.onCreate()
        // initialize dependencies here (e.g. perform dependency injection)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException() // bound Service is a different story
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // process the command here (e.g. retrieve extras from the Intent and act accordingly)
        val demoString = intent?.getStringExtra(EXTRA_DEMO) ?: ""

        if (!isStarted) {
            makeForeground(demoString)
            // place here any logic that should run just once when the Service is started
            isStarted = true
        }

        return START_STICKY // makes sense for a Foreground Service, or even START_REDELIVER_INTENT
    }

    private fun makeForeground(contentText: String = "Foreground Service demonstration") {

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            ONGOING_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // before calling startForeground, we must create a notification and a corresponding
        // notification channel
        createServiceNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    private fun createServiceNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val ONGOING_NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "1001"
        private const val CHANNEL_NAME = "Foreground Service channel"
        private const val EXTRA_DEMO = "EXTRA_DEMO"

        fun startService(context: Context, demoString: String) {
            val intent = Intent(context, ForegroundService::class.java)
            intent.putExtra(EXTRA_DEMO, demoString)
            context.startForegroundService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            context.stopService(intent)
        }
    }
}