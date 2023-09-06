package hu.paulolajos.taxidemo.ui.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import hu.paulolajos.taxidemo.R
import hu.paulolajos.taxidemo.databinding.FragmentRouteBinding
import hu.paulolajos.taxidemo.models.GoogleDirections
import hu.paulolajos.taxidemo.models.LocationModel
import hu.paulolajos.taxidemo.models.OrderData
import hu.paulolajos.taxidemo.models.OrdersInProgress
import hu.paulolajos.taxidemo.util.ApiKey.mapsApiKey
import okhttp3.OkHttpClient
import okhttp3.Request
import java.math.RoundingMode
import java.text.DecimalFormat

class RouteFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var distance: Int? = null
    private var rating: Double? = null
    private var decodedPoly: String? = null

    private var targetLocation: LatLng? = null
    private var targetName: String? = null
    private var myLastLocation: LocationModel? = null
    private var traffic: String? = null

    private var autocompleteFragment: AutocompleteSupportFragment? = null

    //private var root: View? = null
    private var alreadyHaveOrder = false

    private var _binding: FragmentRouteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        const val TAG = "RouteFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Retrieve and inflate the layout for this fragment
        _binding = FragmentRouteBinding.inflate(inflater, container, false)

        //retainInstance = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyLastLocation()
        listenToOrders()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as? AutocompleteSupportFragment
        autocompleteFragment?.requireView()!!.visibility = View.INVISIBLE

        binding.sendToMapButton.setOnClickListener {
            Log.d(TAG, "sendToMapButton")
            val uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/OrderRequests")

            val geofire = GeoFire(ref)
            geofire.setLocation(
                uid,
                GeoLocation(myLastLocation!!.latitude, myLastLocation!!.longitude),
                GeoFire.CompletionListener { key, error ->
                    //
                })
            val secondref = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/OrderRequestsTarget")

            val secondgeofire = GeoFire(secondref)
            secondgeofire.setLocation(
                uid,
                GeoLocation(targetLocation!!.latitude, targetLocation!!.longitude),
                GeoFire.CompletionListener { key, error ->
                    //
                })

            val thirdref = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/OrderRequestsTarget/" + uid + "/name")
            thirdref.setValue(targetName)

            val fourthref = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/OrderData/" + uid)

            val orderData = OrderData(getPrice()!!, distance!!)
            fourthref.setValue(orderData)

            binding.progressBarRoute.visibility = View.VISIBLE
            binding.sendToMapButton.visibility = View.INVISIBLE
            binding.priceTextView.visibility = View.INVISIBLE
            binding.distanceTextView.visibility = View.INVISIBLE
            binding.trafficTextView.visibility = View.INVISIBLE
            binding.infoTextView.visibility = View.VISIBLE
            binding.cancelOrderButton.visibility = View.VISIBLE
            autocompleteFragment?.requireView()!!.visibility = View.INVISIBLE
        }

        binding.cancelOrderButton.setOnClickListener {

            val uid = FirebaseAuth.getInstance().uid

            val secondref = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/OrderRequestsTarget/" + uid)

            val thirdref = FirebaseDatabase
                .getInstance("https://taxidemo-638fe-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/OrderData/" + uid)
            val ref = FirebaseDatabase
                .getInstance()
                .reference
                .child("OrderRequests").child(uid.toString())

            ref.removeValue()
            secondref.removeValue()
            thirdref.removeValue()

            binding.progressBarRoute.visibility = View.INVISIBLE
            binding.sendToMapButton.visibility = View.INVISIBLE
            binding.priceTextView.visibility = View.VISIBLE
            binding.distanceTextView.visibility = View.VISIBLE
            binding.trafficTextView.visibility = View.VISIBLE
            binding.infoTextView.visibility = View.INVISIBLE
            binding.cancelOrderButton.visibility = View.INVISIBLE
            autocompleteFragment?.requireView()!!.visibility = View.VISIBLE

            distance = null
            traffic = null

            binding.distanceTextView.text = ""
            binding.priceTextView.text = ""
            binding.trafficTextView.text = ""
        }

        Places.initialize(
            requireActivity().applicationContext,
            mapsApiKey
        )

        val placesClient = Places.createClient(requireActivity().applicationContext)

        autocompleteFragment!!.setHint("Click here and enter your destination")
        //autocompleteFragment!!.requireView().setBackground(resources.getDrawable(R.drawable.rounded_login_register_text))
        autocompleteFragment!!.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i(TAG, "Place: ${place.name}, ${place.latLng.toString()}")
                targetLocation = place.latLng
                targetName = place.name
                findRoute()
            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })
        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("rating")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                rating = snapshot.getValue(Double::class.java)!!
            }

        })
    }

    private fun getPrice(): Double? {
        val value = (distance!! * 3.0 * rating!!) / 1000
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        Log.d(TAG, df.format(value))
        return df.format(value).replace(",", ".").toDouble()
    }

    private fun listenToOrders() {
        Log.d("you note", "start")
        val uid = FirebaseAuth.getInstance().uid
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
                    Log.d(TAG, orderinprogress!!.user)

                    if (orderinprogress!!.user == uid) {
                        val url = getSecondRouteUrl(orderinprogress)
                        alreadyHaveOrder = true
                        GetRoute(url).execute()
                        Log.d(TAG, orderinprogress.user)
                    }
                }
                if (!alreadyHaveOrder)
                    autocompleteFragment?.view?.visibility = View.VISIBLE
            }

        })

        val secondRef = FirebaseDatabase
            .getInstance()
            .reference
            .child("OrderData")
        secondRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.key == uid) {
                        //he was place here
                        binding.progressBarRoute.visibility = View.VISIBLE
                        binding.sendToMapButton.visibility = View.INVISIBLE
                        binding.priceTextView.visibility = View.INVISIBLE
                        binding.distanceTextView.visibility = View.INVISIBLE
                        binding.trafficTextView.visibility = View.INVISIBLE
                        binding.infoTextView.visibility = View.VISIBLE
                        binding.cancelOrderButton.visibility = View.VISIBLE
                        autocompleteFragment?.view?.visibility=View.INVISIBLE
                    }
                }
            }

        })
    }

    fun sendToMap() {
        val fragmentMap = MapFragment()
        val bundle = Bundle()
        bundle.putInt("distance", distance!!)
        bundle.putString("decodedPoly", decodedPoly!!)
        fragmentMap.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, fragmentMap)
            ?.commit()
    }

    fun getClosestDriver() {
        val ref = FirebaseDatabase
            .getInstance()
            .reference
            .child("AvailableDrivers")

        val geofire = GeoFire(ref)
        val geoQuery = geofire.queryAtLocation(
            GeoLocation(
                myLastLocation!!.longitude,
                myLastLocation!!.latitude
            ), 10.0
        )
        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onGeoQueryReady() {
                Log.d(TAG, "onGeoQueryReady")
            }

            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                Log.d(TAG, key.toString())
            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {
                Log.d(TAG, key.toString())
            }

            override fun onKeyExited(key: String?) {
                Log.d(TAG, key.toString())
            }

            override fun onGeoQueryError(error: DatabaseError?) {
                Log.d(TAG, error.toString())
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun getMyLastLocation() {
        var longitude = 0.0;
        var latitude = 0.0
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val newLocation = LocationModel(location!!.longitude, location.latitude)
                myLastLocation = newLocation
                Log.d(TAG, (myLastLocation!!.latitude + 0.08).toString())
                Log.d(TAG, (myLastLocation!!.longitude + 0.08).toString())
                Log.d(TAG, (myLastLocation!!.latitude - 0.08).toString())
                Log.d(TAG, (myLastLocation!!.longitude - 0.08).toString())
                val northEast =
                    LatLng(myLastLocation!!.latitude + 0.08, myLastLocation!!.longitude + 0.08)
                val southWest =
                    LatLng(myLastLocation!!.latitude - 0.08, myLastLocation!!.longitude - 0.08)
                val border = RectangularBounds.newInstance(southWest, northEast)
                autocompleteFragment!!.setLocationRestriction(border)
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun findRoute() {
        val url = getRouteUrl(myLastLocation!!)
        Log.d(TAG, url)
        GetRoute(url).execute()
        Log.d(TAG, "findRoute")
    }

    fun showDistance() {
        binding.distanceTextView.text = "distance: ${distance.toString()} m"
        binding.priceTextView.text = "Price: ${getPrice().toString()} $"
        binding.trafficTextView.text = traffic
        Log.d(TAG, "showDistance")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getRouteUrl(lastLocation: LocationModel): String {

        Log.d(TAG,"https://maps.googleapis.com/maps/api/directions/json?origin=" +
                "${lastLocation.latitude},${lastLocation.longitude}&destination=" +
                "${targetLocation?.latitude},${targetLocation?.longitude}&departure_time=" +
                "now&key=" + mapsApiKey)

        return "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                "${lastLocation.latitude},${lastLocation.longitude}&destination=" +
                "${targetLocation?.latitude},${targetLocation?.longitude}&departure_time=now&key=" +
                mapsApiKey
    }

    private fun getSecondRouteUrl(orderinprogress: OrdersInProgress): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                "${orderinprogress.startlat},${orderinprogress.startlng}&destination=" +
                "${orderinprogress.targetlat},${orderinprogress.targetlng}&departure_time=now&key=" +
                mapsApiKey
    }

    @SuppressLint("StaticFieldLeak")
    inner class GetRoute(val url: String) : AsyncTask<Void, Void, ForReturn>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): ForReturn {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            var distance = 0
            var durationInTraffic = ""
            var polyline = ""
            try {
                val resObj = Gson().fromJson(data, GoogleDirections::class.java)
                distance = resObj.routes.get(0).legs.get(0).distance.value
                polyline = resObj.routes.get(0).overview_polyline.points
                durationInTraffic = resObj.routes.get(0).legs.get(0).duration_in_traffic.text

            } catch (e: Exception) {
                e.printStackTrace()
            }
            val distanceAndPoly = ForReturn(distance, polyline, durationInTraffic)
            return distanceAndPoly
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(distanceTemp: ForReturn) {
            Log.d(TAG, distanceTemp.toString())

            distance = distanceTemp.distance
            decodedPoly = distanceTemp.decodedPolyline
            traffic = "Time in a traffic jam: " + distanceTemp.traffic
            if (!alreadyHaveOrder) {
                showDistance()
                binding.sendToMapButton.visibility = View.VISIBLE
            } else {
                sendToMap()
            }
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

class ForReturn(val distance: Int, val decodedPolyline: String, val traffic: String) {
}