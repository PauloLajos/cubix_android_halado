package hu.paulolajos.locationchecker

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import hu.paulolajos.locationchecker.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

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
        val queryForFirstElement = database.orderByKey().limitToLast(1)
        queryForFirstElement.addValueEventListener(locListener)

        // init mapfragment
        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.googleMap) as? SupportMapFragment

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                googleMap = mapFragment?.awaitMap()
                googleMap?.awaitMapLoad()
            }
        }
    }

    private val locListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                //get the exact longitude and latitude from the database "test"
                //userLocation = snapshot.child("location").value as Location?
                Log.d(TAG, snapshot.toString())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

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
}