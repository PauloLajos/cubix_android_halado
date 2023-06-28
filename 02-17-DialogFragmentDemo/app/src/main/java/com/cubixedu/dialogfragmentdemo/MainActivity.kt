package com.cubixedu.dialogfragmentdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.dialogfragmentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }
}