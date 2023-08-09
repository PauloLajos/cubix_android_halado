package com.cubix.tracklocationservice

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.cubix.tracklocationservice.databinding.ActivityMainBinding
import com.cubix.tracklocationservice.service.TrackLocationService
import android.Manifest
import android.content.IntentFilter
import android.net.Uri
import android.provider.Settings
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.ui.setupWithNavController
import com.cubix.tracklocationservice.fragments.LocationViewModel
import com.cubix.tracklocationservice.fragments.MapsViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: TrackLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var trackBroadcastReceiver: TrackBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences

    private val locationViewModel: LocationViewModel by viewModels()
    private val mapsViewModel: MapsViewModel by viewModels()


    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as TrackLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up view elements
        setupView()

        // init BroadcastReceiver
        trackBroadcastReceiver = TrackBroadcastReceiver()

        // create shave data preference file
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    private fun setupView() {
        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        // Instantiate the navController using the NavHostFragment
        val navController = navHostFragment.navController

        // Make sure actions in the ActionBar get propagated to the NavController
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_log, R.id.navigation_map, R.id.navigation_about)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: BottomNavigationView = binding.bottomNavView
        navView.setupWithNavController(navController)
    }

    fun buttonClick() {
        val enabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
        )

        if (enabled) {
            foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
        } else {

            // Review Permissions: Checks and requests if needed.
            if (foregroundPermissionApproved()) {
                foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    ?: Log.d(TAG, "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }
    }

    // Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // Review Permissions: Method requests permissions.
    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.activity_main),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        else
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                            ),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // Review Permissions: Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()

                else -> {
                    // Permission denied.
                    updateButtonState(false)

                    Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                this.packageName,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /**
     * Receiver for location broadcasts from [TrackLocationService].
     */
    private inner class TrackBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    TrackLocationService.EXTRA_LOCATION, Location::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Location>(
                    TrackLocationService.EXTRA_LOCATION
                )
            }

            if (location != null) {
                logResultsToScreen(location)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Updates button states if new while in use location is added to SharedPreferences.
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updateButtonState(
                sharedPreferences.getBoolean(
                    SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

        updateButtonState(
            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(this, TrackLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            trackBroadcastReceiver,
            IntentFilter(
                TrackLocationService.ACTION_TRACK_LOCATION_BROADCAST
            )
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            trackBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    private fun updateButtonState(trackingLocation: Boolean) {
        if (trackingLocation) {
            locationViewModel.setButtonText(getString(R.string.stop_location_updates_button_text))
        } else {
            locationViewModel.setButtonText(getString(R.string.start_location_updates_button_text))
        }
    }

    private fun logResultsToScreen(location: Location) {
        val output = StringBuilder()
            .append(SimpleDateFormat( "yy.MM.dd. HH:mm:ss", Locale.ROOT )
                .format(
                    Date(System.currentTimeMillis())
                )
            )
            .append(" - ${location.toText()}")
            .toString()
        locationViewModel.setLogText(output)
        mapsViewModel.setPosition(LatLng(location.latitude, location.longitude))
    }
}