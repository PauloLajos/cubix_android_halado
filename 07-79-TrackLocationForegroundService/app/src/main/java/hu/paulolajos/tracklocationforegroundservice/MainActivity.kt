package hu.paulolajos.tracklocationforegroundservice

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.tracklocationforegroundservice.databinding.ActivityMainBinding
import hu.paulolajos.tracklocationforegroundservice.service.LocationService
import hu.paulolajos.tracklocationforegroundservice.util.Util

class MainActivity : AppCompatActivity() {

    private var locationService: LocationService = LocationService()
    private lateinit var serviceIntent: Intent
    private lateinit var activity: Activity

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@MainActivity

        if (!Util.isLocationEnabledOrNot(activity)) {
            Util.showAlertLocation(
                activity,
                getString(R.string.gps_enable),
                getString(R.string.please_turn_on_gps),
                getString(R.string.ok)
            )
        }

        requestPermissionsSafely(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200
        )

        binding.txtStartService.setOnClickListener {
            locationService = LocationService()
            serviceIntent = Intent(this, locationService.javaClass)
            if (!Util.isMyServiceRunning(locationService.javaClass, activity)) {
                startService(serviceIntent)
                Toast.makeText(
                    activity,
                    getString(R.string.service_start_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.service_already_running),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun requestPermissionsSafely(
        permissions: Array<String>,
        requestCode: Int
    ) {
        requestPermissions(permissions, requestCode)
    }

    override fun onDestroy() {
        if (::serviceIntent.isInitialized) {
            stopService(serviceIntent)
        }

        super.onDestroy()
    }
}