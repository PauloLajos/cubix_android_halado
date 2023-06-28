package com.cubixedu.viewpagertwodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.cubixedu.viewpagertwodemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private var pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Toast.makeText(this@MainActivity, "Selected position: $position",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val fragmentStatePagerAdapter = MyFragmentStatePageAdapter(this, 2)
        mainBinding.mainViewPager.adapter = fragmentStatePagerAdapter

        //binding.mainViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        mainBinding.mainViewPager.registerOnPageChangeCallback(pageChangeCallback)

    }

    override fun onDestroy() {
        mainBinding.mainViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }
}