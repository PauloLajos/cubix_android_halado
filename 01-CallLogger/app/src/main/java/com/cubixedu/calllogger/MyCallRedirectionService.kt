package com.cubixedu.calllogger

import android.content.Intent
import android.net.Uri
import android.telecom.CallRedirectionService
import android.telecom.PhoneAccountHandle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.Date


class MyCallRedirectionService : CallRedirectionService() {

    override fun onPlaceCall(
        handle: Uri,
        initialPhoneAccount: PhoneAccountHandle,
        allowInteractiveResponse: Boolean
    ) {
        val outCallNumber = handle.toString()

        // We can get the outgoing number from the handle parameter:
        Log.i("Phone Number:", outCallNumber)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.RECEIVER_PHONENUMBER, "$outCallNumber")
        intent.putExtra(MainActivity.RECEIVER_DATE, Date(System.currentTimeMillis()).toString())
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}