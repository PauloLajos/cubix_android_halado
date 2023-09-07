package hu.paulolajos.trackerlocation.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import hu.paulolajos.trackerlocation.databinding.FragmentMapsBinding
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapsViewModel

    private lateinit var database: DatabaseReference

    private var googleMap: GoogleMap? = null
    private var positionMarker = LatLng(46.645870,21.285489)
    private var userLocation: Location? = null
    private lateinit var fusedLocClient: FusedLocationProviderClient

    companion object {
        fun newInstance() = MapsFragment()

        private const val REQUEST_LOCATION = 1 //request code to identify specific permission request

        private const val TAG = "MapsFragment" // for debugging
        private const val DefaultCameraZoom = 10F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        // TODO: Use the ViewModel

        // init firebase database
        database = Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment?
        //mapFragment?.getMapAsync(callback)
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
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun getCurrentLocation() {

        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
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
        database
            //.child(userLocation!!.time.toString())
            .child("location")
            .setValue(userLocation)
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

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //permission in the manifest
            REQUEST_LOCATION
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray)
    {
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