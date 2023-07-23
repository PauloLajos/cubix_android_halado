package com.cubix.fragmentsdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
            val bundle = bundleOf("some_int" to 1)

            showFragment(
                R.id.fragment_container_view_program,
                OneFragment(),
                bundle,
                OneFragment.TAG,
                false
            )
        }
    }

    fun showFragment(
        containerViewId: Int,
        fragment: Fragment,
        args: Bundle? = null,
        tag: String,
        replace: Boolean = true
    ) {
        if (replace)
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                .replace(containerViewId, fragment::class.java, args, tag)
                .commit()
        else
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(containerViewId, fragment::class.java, args, tag)
                .commit()
    }
}