package com.tutorialwing.viewpagertwo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.tutorialwing.viewpagertwo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Code that you need to execute on back press i.e. finish()
                val viewPager = mainBinding.viewPager
                if (viewPager.currentItem == 0) {
                    // If the user is currently looking at the first step, allow the system to handle the
                    // Back button. This calls finish() on this activity and pops the back stack.
                    //super.onBackPressed()
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    // Otherwise, select the previous step.
                    viewPager.currentItem = viewPager.currentItem - 1
                }

            }
        })

        setupViewPager2()
    }

    private fun setupViewPager2() {
        val list: MutableList<String> = ArrayList()
        list.add("This is your First Screen")
        list.add("This is your Second Screen")
        list.add("This is your Third Screen")
        list.add("This is your Fourth Screen")

        val colorList: MutableList<String> = ArrayList()
        colorList.add("#00ff00")
        colorList.add("#ff0000")
        colorList.add("#0000ff")
        colorList.add("#f0f0f0")

        // Set adapter to viewPager.
        mainBinding.viewPager.adapter = ViewPagerAdapter(this, list, colorList)
        mainBinding.viewPager.setPageTransformer(ZoomOutPageTransformer())

        setupPageChangeCallback()
    }

    private fun setupPageChangeCallback() {
        mainBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            // Triggered when you select a new page
            override fun onPageSelected(position: Int) {
                Toast.makeText(this@MainActivity, "Selected position: $position",
                    Toast.LENGTH_SHORT).show()
                super.onPageSelected(position)
            }

            /**
             * This method will be invoked when the current page is scrolled,
             * either as part of a programmatically initiated
             * smooth scroll or a user initiated touch scroll.
             */
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            /**
             * Called when the scroll state changes.
             * Useful for discovering when the user begins dragging,
             * when a fake drag is started, when the pager is automatically settling
             * to the current page, or when it is fully stopped/idle.
             * state can be one of SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING or SCROLL_STATE_SETTLING.
             */
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

        })
    }

/*    override fun onBackPressed() {
        val viewPager = mainBinding.viewPager
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            //super.onBackPressed()
            onBackPressedDispatcher.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
 */
}