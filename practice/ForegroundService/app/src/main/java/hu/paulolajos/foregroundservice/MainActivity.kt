package hu.paulolajos.foregroundservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.foregroundservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /**
     * https://www.techyourchance.com/foreground-service-in-android/
     */

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            ForegroundService.startService(this, "some string you want to pass into the service")
        }

        binding.btnStop.setOnClickListener {
            ForegroundService.stopService(this)
        }
    }
}