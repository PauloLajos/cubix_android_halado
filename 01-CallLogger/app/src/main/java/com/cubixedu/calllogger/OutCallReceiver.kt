package com.cubixedu.calllogger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.cubixedu.calllogger.data.AppDatabase
import com.cubixedu.calllogger.data.OutCallEntity
import java.util.Date
import kotlin.concurrent.thread

class OutCallReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val outNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)

        thread {
            AppDatabase.getInstance(context!!).outCallDAO().addCall(
                OutCallEntity(
                    null,
                    Date(System.currentTimeMillis()).toString(), outNumber!!
                )
            )
        }

        Toast.makeText(context, outNumber, Toast.LENGTH_LONG).show()
    }
}