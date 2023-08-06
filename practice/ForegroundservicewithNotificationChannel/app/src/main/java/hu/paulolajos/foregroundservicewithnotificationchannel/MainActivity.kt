package hu.paulolajos.foregroundservicewithnotificationchannel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.foregroundservicewithnotificationchannel.databinding.ActivityMainBinding
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionsSafely(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200
        )

        binding.startServiceButton.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            intent.action = MyService.ACTION_START_FOREGROUND_SERVICE
                startService(intent)
            }

        binding.stopServiceButton.setOnClickListener{
            val intent = Intent(this, MyService::class.java)
            intent.action = MyService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
        }
    }

    private fun requestPermissionsSafely(
        permissions: Array<String>,
        requestCode: Int
    ) {
        requestPermissions(permissions, requestCode)
    }
}