package com.cubix.fragmentsdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.cubix.fragmentsdemo.databinding.ActivityMainBinding
import com.cubix.fragmentsdemo.fragments.OneFragment
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    val fragmentMessage = "Hello world!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setSupportActionBar(mainBinding.toolbar)

        mainBinding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.activitymenu_action_settings -> {
                Snackbar.make(mainBinding.root, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()

                true
            }
            R.id.activitymenu_action_done -> {
                Snackbar.make(mainBinding.root, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showFragment(
        containerViewId: Int,
        fragment: Fragment,
        args: Bundle? = null,
        tag: String,
        replace: Boolean = true
    ) {
        mainBinding.toolbar.title = fragment.javaClass.simpleName

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