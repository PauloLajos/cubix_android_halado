package com.cubix.timeservicebackground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubix.timeservicebackground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intentTimeService = Intent(this, TimeService::class.java)
        binding.btnStart.setOnClickListener {
            startService(intentTimeService)
            binding.btnStart.isEnabled = false
            binding.btnStop.isEnabled = true
        }
        binding.btnStop.setOnClickListener {
            stopService(intentTimeService)
            binding.btnStart.isEnabled = true
            binding.btnStop.isEnabled = false
        }
    }
}