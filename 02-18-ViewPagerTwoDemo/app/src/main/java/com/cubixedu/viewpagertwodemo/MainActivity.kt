package com.cubixedu.viewpagertwodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.cubixedu.viewpagertwodemo.databinding.ActivityMainBinding
import com.cubixedu.viewpagertwodemo.pageanim.DepthPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var pageNames: Array<String>

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

        pageNames = resources.getStringArray(R.array.tab_names)
        TabLayoutMediator(mainBinding.tabLayout, mainBinding.mainViewPager) { tab, position ->
            tab.text = pageNames[position]
        }.attach()

        //binding.mainViewPager.setPageTransformer(ZoomOutPageTransformer())
        mainBinding.mainViewPager.setPageTransformer(DepthPageTransformer())
    }

    override fun onDestroy() {
        mainBinding.mainViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }
}