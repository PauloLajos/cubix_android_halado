package com.cubixedu.viewpagertwodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.viewpagertwodemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val fragmentStatePagerAdapter = MyFragmentStatePageAdapter(this, 2)
        mainBinding.mainViewPager.adapter = fragmentStatePagerAdapter
    }
}