package com.cubixedu.locationandmapdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.cubixedu.locationandmapdemo.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val gyula = LatLng(46.646135,21.271710)

        mMap.let {
            it.isTrafficEnabled = true
            it.uiSettings.isZoomControlsEnabled = true
            it.uiSettings.isCompassEnabled = true
            it.uiSettings.isMapToolbarEnabled = true

            it.setOnMapClickListener {
                mMap.setOnMapClickListener { latLng ->
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title("Marker demo")
                            .snippet("Marker details text")
                    )
                    marker!!.isDraggable = true
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
            }

            it.addMarker(MarkerOptions().position(gyula).title("Marker in Gyula"))
            it.moveCamera(CameraUpdateFactory.newLatLng(gyula))
        }
    }
}