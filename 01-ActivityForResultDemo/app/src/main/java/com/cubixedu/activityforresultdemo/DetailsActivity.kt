package com.cubixedu.activityforresultdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cubixedu.activityforresultdemo.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var detailsBinding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailsBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsBinding.root)

        if (intent.hasExtra(MainActivity.KEY_DATA))
        {
            detailsBinding.tvData.text =
                intent.getStringExtra(MainActivity.KEY_DATA)
        }

        detailsBinding.btnOk.setOnClickListener {
            val intentResult = Intent()
            intentResult.putExtra(MainActivity.KEY_RESULT,
                "Order accepted")
            setResult(RESULT_OK, intentResult)
            finish()
        }

        detailsBinding.btnCancel.setOnClickListener {
            val intentResult = Intent()
            setResult(RESULT_CANCELED, intentResult)
            finish()
        }
    }
}