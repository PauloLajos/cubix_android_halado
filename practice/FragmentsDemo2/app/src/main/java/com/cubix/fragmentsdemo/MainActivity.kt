package com.cubix.fragmentsdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
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

        showMenu()
        mainBinding.toolbar.inflateMenu(R.menu.activity_menu)

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

    private fun showMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.activity_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.activitymenu_action_done -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Menu -Done- selected",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }

                    R.id.activitymenu_action_settings -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Menu -Settings- selected",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }

                    else -> false
                }
            }
        })
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
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
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