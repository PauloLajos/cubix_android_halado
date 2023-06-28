package com.cubixedu.dialogfragmentdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.dialogfragmentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnDialogMessage.setOnClickListener {
            val queryFragment = QueryFragment()
            queryFragment.isCancelable = false

            val bundle = Bundle()
            bundle.putString(KEY_MSG, "Hello Advanced Android")
            queryFragment.arguments = bundle

            queryFragment.show(supportFragmentManager,
                "QueryFragment")
        }
    }

    companion object {
        const val KEY_MSG= "KEY_MSG"
    }
}