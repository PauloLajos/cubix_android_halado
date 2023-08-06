package com.cubix.fusedlocationproviderdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.cubix.fusedlocationproviderdemo.databinding.FragmentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var client: FusedLocationProviderClient
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var locationCallback: LocationCallback


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocationBinding.inflate(inflater, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(requireContext())

        checkLocationPermission()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Update UI with location data
                    moveToCursor()
                }
            }
        }
        startLocationUpdates()
    }

    private fun moveToCursor() {

        // move camera to current position
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

        //set marker to current location
        val marker = MarkerOptions().position(currentLocation).title("you are here")
        //set custom icon
        //marker.icon(BitmapFromVector(getApplicationContext(), R.drawable.baseline_flag_24))
        //add marker
        googleMap.addMarker(marker)
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PERMISSION_GRANTED -> {
                setupMap()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                showAlertDialog()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Location Permission is Required")
        builder.setMessage("Location Permission is required for better .... Please allow location in settings...")

        // Positive Button
        builder.setPositiveButton("Enable") { dialog: DialogInterface, which: Int ->
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // Negative Button
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setupMap()
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Location Permission is Required")
                builder.setMessage("Location Permission is required for better .... Please allow location in settings...")

                // Positive Button
                builder.setPositiveButton("Enable") { dialog: DialogInterface, which: Int ->
                    openAppSettings()
                }

                // Negative Button
                builder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }

    private val appSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PERMISSION_GRANTED
        ) {
            setupMap()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        appSettingsLauncher.launch(intent)
    }

    private fun setupMap() {
        val callback = OnMapReadyCallback { googleMap ->
            this.googleMap = googleMap
            googleMap.uiSettings.isZoomControlsEnabled = false
        }
        mapFragment?.getMapAsync(callback)
        getLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        client!!.lastLocation.addOnSuccessListener { task ->
            val location: Location? = task
            if (location != null) {
                lastLocation = location
                currentLocation = LatLng(location.latitude, location.longitude)

                // move camera to current position
                //set marker to current location
                moveToCursor()
            } else {
                getCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        client!!.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener {
            val location: Location = it
            lastLocation = location
            currentLocation = LatLng(location.latitude, location.longitude)

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        client.requestLocationUpdates(
            LocationRequest
                .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(10000)
                .build(),

            locationCallback,
            Looper.getMainLooper()
        )

        requestingLocationUpdates = true
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        client.removeLocationUpdates(locationCallback)
        requestingLocationUpdates = false
    }

    companion object {
        lateinit var lastLocation: Location
        lateinit var currentLocation: LatLng
        var requestingLocationUpdates = false
    }
}