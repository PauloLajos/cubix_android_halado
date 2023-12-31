package hu.paulolajos.taxidemo.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.PolyUtil
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.FragmentMapBinding
import hu.paulolajos.taxidemo.models.OrdersInProgress
import hu.paulolajos.taxidemo.ui.DriveActivity
import hu.paulolajos.taxidemo.ui.MapsActivity
import java.math.RoundingMode
import java.text.DecimalFormat

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var driverMaker: Marker? = null

    private lateinit var uid: String
    private var myDriver: String? = null

    private var _binding: FragmentMapBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val TAG = "MapFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Retrieve and inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapview = binding.map

        mapview.onCreate(savedInstanceState)
        mapview.onResume()
        mapview.getMapAsync(this)

        uid = FirebaseAuth.getInstance().uid ?: ""

        getLocation()

        return binding.root
    }

    private fun showMyDriver() {
        Log.d(TAG, "showdriver")
        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("OrdersInProgress")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val orderinprogress = it.getValue(OrdersInProgress::class.java)
                    Log.d(TAG, orderinprogress!!.driver)
                    if (orderinprogress!!.user == uid) {
                        myDriver = orderinprogress.driver
                        getMyDriverLocation()
                    }
                }
            }
        })
    }

    fun getMyDriverLocation() {
        val firstref =
            FirebaseDatabase
                .getInstance()
                .reference
                .child("users").child(myDriver.toString())
                .child("lastLocalization")

        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("users").child(myDriver.toString())

        val geofire = GeoFire(ref)
        firstref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                geofire.getLocation("lastLocalization",
                    object : com.firebase.geofire.LocationCallback {

                        override fun onLocationResult(key: String?, location: GeoLocation?) {
                            Log.d(TAG, key.toString())
                            Log.d(TAG, location!!.latitude.toString())
                            driverMaker?.remove()
                            driverMaker = mMap
                                .addMarker(
                                    MarkerOptions()
                                        .position(LatLng(location!!.longitude, location.latitude))
                                        .title("Driver")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                )
                        }

                        override fun onCancelled(databaseError: DatabaseError?) {
                            //To change body of created functions use File | Settings | File Templates.
                        }
                    })
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled = true
        val distance = arguments?.getInt("distance")
        val routePolylineCoded = arguments?.getString("decodedPoly")
        if (routePolylineCoded != null) {
            val decodedPolyLine = PolyUtil.decode(routePolylineCoded)
            mMap.addPolyline(PolylineOptions().addAll(decodedPolyLine))
            mMap.addMarker(
                MarkerOptions().position(decodedPolyLine.first()).title("Start").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.starticon)
                )
            )
            mMap.addMarker(
                MarkerOptions().position(decodedPolyLine.last()).title("Finish")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.finishicon))
            )

            binding.endRouteButton.visibility = View.VISIBLE
            /*
            val ref= FirebaseDatabase.getInstance().getReference("/users/"+uid+"/lastLocalization")
            var geofire= GeoFire(ref)
            var geoQuery=geofire.queryAtLocation(GeoLocation(decodedPolyLine.last().latitude,decodedPolyLine.last().longitude),0.2)
            geoQuery.addGeoQueryEventListener(object:GeoQueryEventListener{
                override fun onGeoQueryReady() {
                    Log.d(TAG,"ready")
                }

                override fun onKeyEntered(key: String?, location: GeoLocation?) {
                    Log.d(TAG,key)
                }

                override fun onKeyMoved(key: String?, location: GeoLocation?) {

                }

                override fun onKeyExited(key: String?) {

                }

                override fun onGeoQueryError(error: DatabaseError?) {

                }

            })

            Log.d(TAG,"driver")
*/
            binding.endRouteButton.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("confirm")
                builder.setMessage("Are you sure you want to end the ride?")

                builder.setPositiveButton("Yes") { dialog, which ->
                    Log.d(TAG, "once")
                    val ref = FirebaseDatabase
                        .getInstance()
                        .reference
                        .child("OrdersInProgress")

                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            //
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            Log.d(TAG, "two")
                            ref.removeEventListener(this)
                            snapshot.children.forEach {
                                val orderinprogress = it.getValue(OrdersInProgress::class.java)
                                if (orderinprogress!!.driver == uid || orderinprogress.user == uid) {
                                    it.ref.removeValue()

                                    val reffourth = FirebaseDatabase
                                        .getInstance()
                                        .reference
                                        .child("users").child(orderinprogress.driver)
                                        .child("orders")

                                    reffourth.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            //
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            Log.d(TAG, "three")
                                            var rating = 0.0
                                            var i = 0
                                            snapshot.children.forEach {
                                                i++
                                                val orderFromHistory =
                                                    it.getValue(OrdersInProgress::class.java)
                                                rating = orderFromHistory!!.rating + rating
                                            }
                                            rating = rating / i
                                            val refsecond = FirebaseDatabase
                                                .getInstance()
                                                .reference
                                                .child("users").child(orderinprogress.driver)
                                                .child("orders")
                                                .push()
                                            refsecond.setValue(orderinprogress)

                                            val refthird = FirebaseDatabase.getInstance()
                                                .reference
                                                .child("users").child(orderinprogress.driver)
                                                .child("orders")
                                                .push()

                                            if (rating > 4.0) {
                                                val newOrder = OrdersInProgress(
                                                    orderinprogress.driver,
                                                    orderinprogress.user,
                                                    orderinprogress.startlat,
                                                    orderinprogress.startlng,
                                                    orderinprogress.targetlat,
                                                    orderinprogress.targetlng,
                                                    convertDouble(orderinprogress.price * 0.9),
                                                    orderinprogress.distance,
                                                    orderinprogress.rating,
                                                    orderinprogress.timestamp
                                                )
                                                refthird.setValue(newOrder)
                                            }
                                            if (rating > 3.0 && rating <= 4) {
                                                val newOrder = OrdersInProgress(
                                                    orderinprogress.driver,
                                                    orderinprogress.user,
                                                    orderinprogress.startlat,
                                                    orderinprogress.startlng,
                                                    orderinprogress.targetlat,
                                                    orderinprogress.targetlng,
                                                    convertDouble(orderinprogress.price * 0.8),
                                                    orderinprogress.distance,
                                                    orderinprogress.rating,
                                                    orderinprogress.timestamp
                                                )
                                                refthird.setValue(newOrder)
                                            }
                                            if (rating <= 3) {
                                                val newOrder = OrdersInProgress(
                                                    orderinprogress.driver,
                                                    orderinprogress.user,
                                                    orderinprogress.startlat,
                                                    orderinprogress.startlng,
                                                    orderinprogress.targetlat,
                                                    orderinprogress.targetlng,
                                                    convertDouble(orderinprogress.price * 0.7),
                                                    orderinprogress.distance,
                                                    orderinprogress.rating,
                                                    orderinprogress.timestamp
                                                )
                                                refthird.setValue(newOrder)
                                            } else {
                                                val newOrder = OrdersInProgress(
                                                    orderinprogress.driver,
                                                    orderinprogress.user,
                                                    orderinprogress.startlat,
                                                    orderinprogress.startlng,
                                                    orderinprogress.targetlat,
                                                    orderinprogress.targetlng,
                                                    convertDouble(orderinprogress.price * 0.8),
                                                    orderinprogress.distance,
                                                    orderinprogress.rating,
                                                    orderinprogress.timestamp
                                                )
                                                refthird.setValue(newOrder)
                                            }
                                            val reffifth = FirebaseDatabase
                                                .getInstance()
                                                .reference
                                                .child("users").child(orderinprogress.driver)
                                                .child("status")
                                            reffifth.setValue(false)

                                            binding.endRouteButton.visibility = View.INVISIBLE

                                            mMap.clear()

                                            binding.endRouteButton.setOnClickListener(null)
                                            reffourth.removeEventListener(this)

                                            Log.d(TAG, "Four")
                                        }
                                    })
                                }
                            }
                        }
                    })
                }

                builder.setNegativeButton("nie") { dialog, which ->
                    //
                }
                builder.show()
            }

            if (activity is MapsActivity) {
                Log.d(TAG, "driver")
                if (routePolylineCoded != null)
                    showMyDriver()
            }

            val ref = FirebaseDatabase
                .getInstance()
                .reference
                .child("OrdersInProgress")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var isInBase = false
                    snapshot.children.forEach {
                        val orderinprogress = it.getValue(OrdersInProgress::class.java)
                        if (orderinprogress!!.driver == uid) {
                            isInBase = true
                        } else if (orderinprogress!!.user == uid) {
                            isInBase = true
                        }
                    }
                    if (isInBase == false) {
                        mMap.clear()
                        decodedPolyLine.clear()
                        binding.endRouteButton.visibility = View.INVISIBLE
                        if (activity is MapsActivity) {
                            var bundle = Bundle()
                            bundle.putString("myDriver", myDriver!!)
                            val fragmentEnd = EndRouteFragment()
                            fragmentEnd.arguments = bundle
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragmentEnd)
                                .commit()
                        }
                    }
                }
            })
        }
        val home = LatLng(46.648,21.284)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 14.0f))
    }

    fun convertDouble(value: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        Log.d(TAG, df.format(value))
        return df.format(value).replace(",", ".").toDouble()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        var locationGps: Location? = null
        var locationNetwork: Location? = null

        var hasGps = false
        var hasNetwork = false

        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("AvailableDrivers")

        val refsecond = FirebaseDatabase
            .getInstance()
            .reference
            .child("users").child(uid)

        val locationmanager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        hasGps = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps || hasNetwork) {
            if (hasGps) {
                val gpsLocationListener = object : android.location.LocationListener {
                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {
                        Log.d(TAG, "status = $status")
                    }

                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationGps = location
                            if (activity is DriveActivity) {
                                val geofiresecond = GeoFire(refsecond)
                                geofiresecond.setLocation(
                                    "lastLocalization",
                                    GeoLocation(
                                        locationGps!!.longitude,
                                        locationGps!!.latitude
                                    ),
                                    GeoFire.CompletionListener { key, error ->

                                    })
                            }

                            Log.d(
                                "CodeAndroidLocation",
                                "GPS Latitude:" + locationGps!!.latitude
                            )
                            Log.d(
                                "CodeAndroidLocation",
                                "GPS Latitude:" + locationGps!!.longitude
                            )
                        }
                    }
                }

                locationmanager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
            }
            val localGpsLocation =
                locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (localGpsLocation != null) {
                locationGps = localGpsLocation
            }
            if (hasNetwork) {
                val networkLocationListener = object : android.location.LocationListener {
                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {
                        Log.d(TAG, "status = $status")
                    }

                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationGps = location
                            if (activity is DriveActivity) {
                                val geofiresecond = GeoFire(refsecond)
                                geofiresecond.setLocation(
                                    "lastLocalization",
                                    GeoLocation(
                                        locationGps!!.longitude,
                                        locationGps!!.latitude
                                    ),
                                    GeoFire.CompletionListener { key, error ->

                                    })
                            }

                            Log.d(
                                "CodeAndroidLocation",
                                "GPS Latitude:" + locationGps!!.latitude
                            )
                            Log.d(
                                "CodeAndroidLocation",
                                "GPS Latitude:" + locationGps!!.longitude
                            )
                        }
                    }
                }

                locationmanager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
                )
            }
            val localNewtorkLocation =
                locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (localNewtorkLocation != null) {
                locationNetwork = localNewtorkLocation
            }
            if (locationGps != null && locationNetwork != null) {
                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                    Log.d(
                        "CodeAndroidLocation",
                        "Network Latitude:" + locationNetwork!!.latitude
                    )
                    Log.d(
                        "CodeAndroidLocation",
                        "Network Latitude:" + locationNetwork!!.longitude
                    )

                } else {
                    Log.d("CodeAndroidLocation", "GPS Latitude:" + locationGps!!.latitude)
                    Log.d("CodeAndroidLocation", "GPS Latitude:" + locationGps!!.longitude)
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}