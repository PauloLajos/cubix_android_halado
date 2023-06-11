package com.cubixedu.viewbindingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cubixedu.viewbindingdemo.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnTime.setOnClickListener {
            mainBinding.tvHello.text = Date(System.currentTimeMillis()).toString()
        }
    }
}