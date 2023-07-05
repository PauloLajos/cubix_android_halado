package com.cubixedu.sundirectiononmap

import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.cubixedu.sundirectiononmap.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.OnMapReadyCallback
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Calendar
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : FragmentActivity(), OnMapReadyCallback {

    private val cameraZoom: Float = 18f

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.tvHello.text = "Search GPS..."
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this@MainActivity)
        fetchLocation()

    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(applicationContext, currentLocation.latitude.toString() + "" +
                        currentLocation.longitude, Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.fragmentMap) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this@MainActivity)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        //val latLng = LatLng(49.099288,19.029719)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom))
        mMap.addMarker(markerOptions)

        mainBinding.tvHello.text = "Place find"

        drawLine(latLng)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    private fun drawLine(latLng: LatLng) {

        val azimuthAngle = calculateSunDirection()

        mainBinding.tvHello.text = "azimut = %.2f".format(azimuthAngle)

        val lineEndX = 0.0005 * sin(Math.toRadians(azimuthAngle))
        val lineEndY = 0.0005 * cos(Math.toRadians(azimuthAngle))

        val polyLineOpts = PolylineOptions().add(
            LatLng(latLng.latitude, latLng.longitude),
            LatLng(latLng.latitude + lineEndX, latLng.longitude + lineEndY)
        )
        val polyline = mMap.addPolyline(polyLineOpts)
        polyline.color = Color.GREEN
    }

    private fun calculateSunDirection(): Double {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val latitude = currentLocation.latitude
        val longitude = currentLocation.longitude
        // Time zone offset in hours
        val timeZoneOffset = calendar.timeZone.rawOffset / (1000 * 60 * 60)
        val declination = 23.45 * sin(Math.toRadians((360.toDouble() / 365) * (dayOfYear - 81)))

        val hourAngle = (15 * (12 - (12 + timeZoneOffset) - (longitude / 15)))
        val cosHA = cos(Math.toRadians(hourAngle))
        // val sinHA = sin(Math.toRadians(hourAngle))
        val cosDec = cos(Math.toRadians(declination))
        val sinDec = sin(Math.toRadians(declination))

        val zenithAngle = Math.toDegrees(
            acos(
                sinDec * sin(Math.toRadians(latitude)) +
                        cosDec * cos(Math.toRadians(latitude)) * cosHA
            )
        )
        val azimuthAngle = Math.toDegrees(
            acos(
                (sinDec * cos(Math.toRadians(latitude)) - cosDec * sin(Math.toRadians(latitude)) * cosHA) /
                        sin(Math.toRadians(zenithAngle))
            )
        )

        return if (hourAngle > 0) azimuthAngle else 360 - azimuthAngle
    }
}