package com.cubix.geolocationdemo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cubix.geolocationdemo.MainActivity
import com.cubix.geolocationdemo.R
import com.cubix.geolocationdemo.databinding.FragmentHomeBinding
import com.cubix.geolocationdemo.location.GeoLocationManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationManager: GeoLocationManager
    private var locationTrackingRequested = false

    companion object {
        const val LOCATION_PERMISSION_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnStop.isEnabled = false
        binding.tvStatus.text = getString(R.string.status,"Idle")
        binding.tvLatitude.text = getString(R.string.latitude,0.0)
        binding.tvLongitude.text = getString(R.string.longitude,0.0)

        // Create GeoLocationManager
        locationManager = GeoLocationManager(activity as Context)

        return root
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations){
                // Update UI
                binding.tvLatitude.text = getString(R.string.latitude,location.latitude)
                binding.tvLongitude.text = getString(R.string.longitude,location.longitude)

                binding.tvProvider.text = getString(R.string.provider,location.provider)
                binding.tvAccuracy.text = getString(R.string.accuracy,location.accuracy.toString())
                binding.tvAltitude.text = getString(R.string.altitude,location.altitude.toString())
                binding.tvSpeed.text = getString(R.string.speed,location.speed.toString())
                binding.tvTime.text = getString(R.string.time,
                    SimpleDateFormat("hh:mm:ss")
                        .format(
                            Date(location.time)
                        )
                )

                (activity as MainActivity).position = LatLng(location.latitude,location.longitude)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            val permissionGranted = requestLocationPermission();
            if (permissionGranted) {
                locationManager.startLocationTracking(locationCallback)
                locationTrackingRequested = true
                binding.tvStatus.text = getString(R.string.status, "Started")

                buttonStarted()
            }
        }
        binding.btnStop.setOnClickListener {
            locationManager.stopLocationTracking()
            locationTrackingRequested = false
            binding.tvStatus.text = getString(R.string.status,"Stopped")

            buttonStopped()
        }
    }

    private fun buttonStarted() {
        binding.btnStart.isEnabled = false
        binding.btnStop.isEnabled = true
    }
    private fun buttonStopped() {
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false
    }

    private fun requestLocationPermission(): Boolean {
        var permissionGranted = false

        if (ContextCompat.checkSelfPermission(
                context as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {
            // we have the permission
            permissionGranted = true
        }

        return permissionGranted
    }

    // Handle Allow or Deny response from the permission dialog
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (
                    grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.startLocationTracking(locationCallback)
                    binding.tvStatus.text = getString(R.string.status, "Started")
                } else {
                    // Permission was denied
                    showAlert("Location permission was denied. Unable to track location.")
                }
                return
            }
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        locationManager.stopLocationTracking()
        binding.tvStatus.text = getString(R.string.status, "Stopped")
        buttonStopped()
    }

    override fun onResume() {
        super.onResume()
        if  (locationTrackingRequested) {
            locationManager.startLocationTracking(locationCallback)
            binding.tvStatus.text = getString(R.string.status, "Started")
            buttonStarted()
        }
    }
}