package com.cubix.goodbuydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubix.goodbuydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        if (savedInstanceState == null) {
            showFragmentLogin()
        }
    }

    private fun showFragmentLogin() {
        val fragmentLogin = FragmentLogin()
        val ft = supportFragmentManager.beginTransaction()

        ft.add(R.id.fragmentContainer, fragmentLogin, FragmentLogin.TAG)
        ft.commit()
    }

    fun showFragmentList() {
        val fragmentList = FragmentList()
        val ft = supportFragmentManager.beginTransaction()

        ft.replace(R.id.fragmentContainer,fragmentList,FragmentList.TAG)
        ft.addToBackStack(FragmentList.TAG)
        ft.commit()
    }

    fun showFragmentAdd() {
        val fragmentAdd = FragmentAdd()
        val ft = supportFragmentManager.beginTransaction()

        ft.replace(R.id.fragmentContainer,fragmentAdd,FragmentAdd.TAG)
        ft.addToBackStack(FragmentAdd.TAG)
        ft.commit()
    }
}