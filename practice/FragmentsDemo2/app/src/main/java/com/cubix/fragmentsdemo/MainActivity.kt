package com.cubix.fragmentsdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cubix.fragmentsdemo.databinding.ActivityMainBinding
import com.cubix.fragmentsdemo.fragments.OneFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    val fragmentMessage = "Hello world!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        if (savedInstanceState == null) {
            showFragment(R.id.fragment_container_view_program, OneFragment(), OneFragment.TAG, false)
        }
    }

    fun showFragment(
        containerViewId: Int,
        fragment: Fragment,
        tag: String,
        replace: Boolean = true
    ) {
        if (replace)
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(containerViewId, fragment, tag)
                .commit()
        else
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(containerViewId, fragment, tag)
                .commit()
    }
}