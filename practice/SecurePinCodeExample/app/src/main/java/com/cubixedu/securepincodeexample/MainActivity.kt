package com.cubixedu.securepincodeexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.securepincodeexample.databinding.ActivityMainBinding
import com.cubixedu.securepincodeexample.ui.pin.PinCodeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PinCodeFragment.newInstance())
                .commitNow()
        }
    }
}