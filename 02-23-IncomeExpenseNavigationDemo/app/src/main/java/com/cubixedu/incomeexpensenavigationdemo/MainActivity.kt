package com.cubixedu.incomeexpensenavigationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.incomeexpensenavigationdemo.data.DataManager
import com.cubixedu.incomeexpensenavigationdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    var dataManager = DataManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }
}