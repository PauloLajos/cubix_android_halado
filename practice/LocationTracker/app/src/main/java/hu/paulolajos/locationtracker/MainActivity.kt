package hu.paulolajos.locationtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import hu.paulolajos.locationtracker.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var googleMap: GoogleMap? = null
    private var positionMarker = LatLng(46.645870,21.285489)
    private var userLocation: Location? = null
    private lateinit var fusedLocClient: FusedLocationProviderClient

    // change url: Project/LocationTracker/app/google-services.json:
    // "firebase_url": "https://locationtracker-940b5-default-rtdb.europe-west1.firebasedatabase.app"
    private lateinit var database: DatabaseReference


    companion object {
        private const val REQUEST_LOCATION = 1 //request code to identify specific permission request

        private const val TAG = "MapsActivity" // for debugging
        private const val DefaultCameraZoom = 10F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init firebase database
        database = Firebase.database.reference

        // init mapfragment
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.googleMap) as? SupportMapFragment

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                googleMap = mapFragment?.awaitMap()
                googleMap?.awaitMapLoad()

                setupLocationClient()

                // start listening current location
                getCurrentLocation()
            }
        }
    }

    private fun setupLocationClient() {
        fusedLocClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getCurrentLocation() {

        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            )
        {
            // If the permission has not been granted, then requestLocationPermissions() is called.
            requestLocPermissions()
        } else {
            fusedLocClient.lastLocation.addOnCompleteListener {
                // lastLocation is a task running in the background
                userLocation = it.result //obtain location


                if (userLocation != null) {

                    positionMarker = LatLng(userLocation!!.latitude, userLocation!!.longitude)
                    moveCamera()

                    //Save the location data to the database
                    writeDatabase()

                } else {
                    // if location is null , log an error message
                    Log.e(TAG, "No location found")
                }
            }
        }
    }

    private fun writeDatabase() {

        //Get a reference to the database, so your app can perform read and write operations
        database.child(userLocation!!.time.toString()).child("location").setValue(userLocation)
    }

    private fun moveCamera() {

        googleMap?.addMarker(
            MarkerOptions()
                .position(positionMarker)
                .title("Marker")
        )
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                positionMarker,
                DefaultCameraZoom
            )
        )
    }

    // prompt the user to grant/deny access
    private fun requestLocPermissions() {

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //permission in the manifest
            REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray)
    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //check if the request code matches the REQUEST_LOCATION
        if (requestCode == REQUEST_LOCATION)
        {
            //check if grantResults contains PERMISSION_GRANTED. If it does, call getCurrentLocation()
            if (grantResults.size == 1 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                //if it doesn't log an error message
                Log.e(TAG, "Location permission denied")
            }
        }
    }
}