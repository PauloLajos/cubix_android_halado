package com.cubixedu.sendbroadcasttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.sendbroadcasttest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnSend.setOnClickListener {
            //sendBroadcast(Intent("com.cubixedu.broadcastreceivertest.NOTIFY"))
            var myIntent = Intent("com.cubixedu.broadcastreceivertest.NOTIFY")
            myIntent.putExtra("KEY_DATA", "HELLO")
            sendBroadcast(myIntent)
        }
    }
}