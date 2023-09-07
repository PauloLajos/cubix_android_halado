package hu.paulolajos.trackerlocation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.trackerlocation.databinding.ActivityMainBinding
import hu.paulolajos.trackerlocation.service.LocationService
import hu.paulolajos.trackerlocation.ui.login.LoginFragment
import hu.paulolajos.trackerlocation.util.Util
import android.Manifest
import android.widget.Toast
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var locationService: LocationService = LocationService()
    private lateinit var serviceIntent: Intent
    private lateinit var activity: Activity

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

        binding.btnStart.setOnClickListener {
            locationService = LocationService()
            serviceIntent = Intent(this, locationService.javaClass)
            if (!Util.isMyServiceRunning(locationService.javaClass, activity)) {
                startService(serviceIntent)
                Toast.makeText(
                    activity,
                    getString(R.string.service_start_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnStart.text = StringBuilder().append("Stop")
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.service_already_running),
                    Toast.LENGTH_SHORT
                ).show()
                stopService(serviceIntent)
                binding.btnStart.text = StringBuilder().append("Start")
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, LoginFragment.newInstance())
                .commitNow()
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