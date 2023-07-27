package com.cubix.timeservicebackground

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeService : Service() {

    var serviceEnabled = false

    inner class TimeThread : Thread() {
        override fun run() {

            // access to MainActivity from service
            val handlerMain = Handler(Looper.getMainLooper())

            while (serviceEnabled) {
                // Toast send MainActivity
                handlerMain.post {
                    Toast.makeText(
                        this@TimeService,
                        SimpleDateFormat(
                            "yyyy.MM.dd. HH:mm:ss",
                            Locale.ROOT
                        ).format(Date(System.currentTimeMillis())),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                sleep(5000)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceEnabled = true
        TimeThread().start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // stop thread
        serviceEnabled = false

    }
}