package com.cubixedu.motionlayouttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.motionlayouttest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.keyPositionButton.setOnClickListener {
            val intent = Intent(this, KeyPositionDemoActivity::class.java)
            startActivity(intent)
        }
    }
}