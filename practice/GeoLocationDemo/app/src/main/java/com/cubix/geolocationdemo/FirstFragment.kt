package com.cubix.geolocationdemo

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.cubix.geolocationdemo.databinding.FragmentFirstBinding
import com.cubix.geolocationdemo.location.GeoLocationManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import android.Manifest
import androidx.core.app.ActivityCompat

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationManager: GeoLocationManager
    private var locationTrackingRequested = false

    companion object {
        const val LOCATION_PERMISSION_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Create GeoLocationManager
        locationManager = GeoLocationManager(activity as Context)

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations){
                // Update UI
                binding.textviewLatitude.text = location.latitude.toString()
                binding.textviewLongitude.text = location.longitude.toString()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStartLocationScan.setOnClickListener {
            val permissionGranted = requestLocationPermission();
            if (permissionGranted) {
                locationManager.startLocationTracking(locationCallback)
                locationTrackingRequested = true
                binding.textviewStatus.text = "Started"
            }
        }
        binding.buttonStopLocationScan.setOnClickListener {
            locationManager.stopLocationTracking()
            locationTrackingRequested = false
            binding.textviewStatus.text = "Stopped"
        }
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
                    binding.textviewStatus.text = "Started"
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
        binding.textviewStatus.text = "Stopped"
    }

    override fun onResume() {
        super.onResume()

        if  (locationTrackingRequested) {
            locationManager.startLocationTracking(locationCallback)
            binding.textviewStatus.text = "Started"
        }
    }
}