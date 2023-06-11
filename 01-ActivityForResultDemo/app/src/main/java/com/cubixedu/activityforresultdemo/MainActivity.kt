package com.cubixedu.activityforresultdemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.cubixedu.activityforresultdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_DATA = "KEY_DATA"
        const val REQ_ANSWER = 1001
        const val KEY_RES = "KEY_RES"
        const val KEY_ORDER = "KEY_ORDER"
        const val KEY_RESULT = "KEY_RESULT"
    }

    private lateinit var mainBinding: ActivityMainBinding

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->

        if (result.resultCode == Activity.RESULT_OK) {
            val resultIntent = result.data

            mainBinding.tvResult.text = resultIntent?.getStringExtra(
                KEY_RESULT)

        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            mainBinding.tvResult.text = "Cancelled"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnDetails.setOnClickListener {
            var intentDetails = Intent()

            intentDetails.setClass(this,
                DetailsActivity::class.java)

            intentDetails.putExtra(KEY_DATA,
                mainBinding.etData.text.toString())

            //startActivityForResult(intentDetails, REQ_ANSWER)
            startForResult.launch(intentDetails)
        }
    }
}