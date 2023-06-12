package com.peacefulstormcorner.implicitintentdemo

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.peacefulstormcorner.implicitintentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

    }

    fun intentSearch(v: View) {
        val intentSearch = Intent(Intent.ACTION_WEB_SEARCH)
        intentSearch.putExtra(SearchManager.QUERY,"Balaton")
        startActivity(intentSearch)
    }

    fun intentDial(v: View) {
        val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+36302690145"))
        startActivity(intentDial)
    }

    fun intentSend(v: View) {
        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        //intentSend.`package` = "com.facebook.katana"
        intentSend.putExtra(Intent.EXTRA_TEXT, "Jee  Tanfolyam!")
        startActivity(intentSend)
        startActivity(Intent.createChooser(intentSend, "Select share app"))
    }
}