package com.cubixedu.locationandmapdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.cubixedu.locationandmapdemo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.random.Random

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

        // Add a marker in Gyula and move the camera
        val markerHungary = LatLng(46.646135, 21.271710)

        initMap(markerHungary)
        initMapAndMarkerClick()

        drawPolygonAndLine()
    }

    private fun drawPolygonAndLine() {
        val polyRect: PolygonOptions = PolygonOptions().add(
            LatLng(44.0, 19.0),
            LatLng(44.0, 26.0),
            LatLng(48.0, 26.0),
            LatLng(48.0, 19.0)
        )
        val polygon: Polygon = mMap.addPolygon(polyRect)
        polygon.fillColor = Color.argb(100, 0, 255, 0)

        val polyLineOpts = PolylineOptions().add(
            LatLng(34.0, 19.0),
            LatLng(34.0, 26.0),
            LatLng(38.0, 26.0)
        )
        val polyline = mMap.addPolyline(polyLineOpts)
        polyline.color = Color.GREEN
    }

    private fun initMapAndMarkerClick() {
        mMap.setOnMapClickListener {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Marker demo")
                    .snippet("Marker details text")
            )
            marker!!.isDraggable = true

            val random = Random(System.currentTimeMillis())
            val cameraPosition = CameraPosition.Builder()
                .target(it)
                .zoom(5f + random.nextInt(15))
                .tilt(30f + random.nextInt(15))
                .bearing(-45f + random.nextInt(90))
                .build()

            //mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

        mMap.setOnMarkerClickListener { marker ->
            Toast.makeText(
                this@MapsActivity,
                "${marker!!.position.latitude}, ${marker!!.position.longitude}",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    private fun initMap(markerHungary: LatLng) {

        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle)
        mMap.setMapStyle(mapStyleOptions)

        mMap.isTrafficEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.addMarker(MarkerOptions().position(markerHungary).title("Marker in Hungary"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerHungary))
    }
}