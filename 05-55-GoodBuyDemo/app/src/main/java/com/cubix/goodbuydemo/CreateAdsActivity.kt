package com.cubix.goodbuydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CreateAdsActivity : AppCompatActivity() {

    companion object {
        final const val COLLECTION_ADS = "ads"
        final const val PERMISSION_REQUEST_CODE = 1001
        final const val CAMERA_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ads)
    }
}