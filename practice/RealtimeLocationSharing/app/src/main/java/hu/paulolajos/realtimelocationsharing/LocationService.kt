package hu.paulolajos.realtimelocationsharing

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LocationService : Service() {

    private val CHANNEL_ID = "LocationService"
    var latitude = 0.0
    var longitude = 0.0
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    companion object {
        const val REQUEST_CODE_LOCATION_PERMISSION = 1001
    }

    override fun onCreate() {
        super.onCreate()

        //To get instance of our firebase database
        firebaseDatabase = FirebaseDatabase.getInstance()

        //Location is the root node in the realtime database
        //To get reference of our database
        databaseReference = firebaseDatabase.getReference("Location")

        //Function to show notification with latitude and longitude of user
        showNotification(latitude, longitude)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Function to get current location using LocationManager Api
        getCurrentLocation(this)

        //Function to show live location on map
        //startMapsActivity()

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun showNotification(latitude: Double, longitude: Double) {

        //It requires to create notification channel when android version is more than or equal to oreo
        createNotificationChannel()

        val notificationIntent = Intent(this, MapsActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service")
            .setContentText("Latitude = $latitude Longitude = $longitude")
            //.setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID, "Location Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)

    }

    private fun checkLocationPermission() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            val permissionList =
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            requestPermissions(
                permissionList,
                REQUEST_CODE_LOCATION_PERMISSION
            )

        } else {

            //This function checks gps is enabled or not in the device
            showGpsEnablePopup()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.size >= 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                //This function checks gps is enabled or not in the device
                showGpsEnablePopup()

            } else {

                Toast.makeText(this, "Permission Denied !", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun showGpsEnablePopup() {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true) //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

        val task =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.

                //Gps enabled

            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> // Location settings are not satisfied. But could be fixed by showing the
                        // user dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Gps enabled
                }

                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this, "Gps is required, please turn it on",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun getCurrentLocation(context: Context?) {

        //Check all location permission granted or not
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            //Criteria class indicating the application criteria for selecting a location provider
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.isSpeedRequired = true

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val provider = locationManager.getBestProvider(criteria, true)

            if (provider != null) {
                locationManager.requestLocationUpdates(
                    provider, 1, 0.1f, object : LocationListener {
                        override fun onLocationChanged(location: Location) {

                            //Location changed

                            latitude = location.latitude
                            longitude = location.longitude

                            //Function to add data to firebase realtime database
                            addDataToDatabase()

                            //Update notification with latest latitude and longitude
                            showNotification(latitude, longitude)

                        }

                        override fun onProviderDisabled(provider: String) {
                            //Provider disabled
                        }

                        override fun onProviderEnabled(provider: String) {
                            //Provider enabled
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                    })
            }
        }
    }

    private fun addDataToDatabase() {

        //setValue method is used to add value to RTFD
        databaseReference.setValue(
            CurrentLocation(latitude, longitude)
        )
    }

    fun startService(context: Context) {
        val startServiceIntent = Intent(context, LocationService::class.java)
        context.startForegroundService(startServiceIntent)
    }

    fun stopService(context: Context) {
        val stopServiceIntent = Intent(context, LocationService::class.java)
        context.stopService(stopServiceIntent)
    }
}