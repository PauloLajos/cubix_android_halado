package hu.paulolajos.foregroundservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        // less binding :)
        val btnStart = binding.btnStart
        val btnStop = binding.btnStop

        btnStart.setOnClickListener {
            ForegroundService.startService(this, "some string you want to pass into the service")
        }

        btnStop.setOnClickListener {
            ForegroundService.stopService(this)
        }
    }
}