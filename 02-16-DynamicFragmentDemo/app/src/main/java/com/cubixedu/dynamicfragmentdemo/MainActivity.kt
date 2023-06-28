package com.cubixedu.dynamicfragmentdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.dynamicfragmentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        if (savedInstanceState == null) {
            showHomeFragment()
        }
    }

    private fun showHomeFragment() {
        val homeFragment = HomeFragment()
        val ft = supportFragmentManager.beginTransaction()

        ft.add(R.id.fragmentContainer,homeFragment,HomeFragment.TAG)
        ft.commit()
    }

    fun showDetails(parameter: String) {
        val detailsFragment = DetailsFragment.newInstance(parameter)
        val ft = supportFragmentManager.beginTransaction()

        ft.add(R.id.fragmentContainer,detailsFragment,DetailsFragment.TAG)
        ft.addToBackStack(DetailsFragment.TAG)
        ft.commit()
    }
}