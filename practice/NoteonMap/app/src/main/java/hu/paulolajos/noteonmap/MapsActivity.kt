package hu.paulolajos.noteonmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.paulolajos.noteonmap.databinding.ActivityMapsBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import hu.paulolajos.noteonmap.data.Note
import hu.paulolajos.noteonmap.databinding.DialogInputBinding
import hu.paulolajos.noteonmap.viewModel.NotesViewModel


/***
 * Firebase Realtime Database access: 2023.09.18.
 */


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private var cameraPosition: Location? = null
    private val defaultLocation = LatLng(46.6473027,21.2784255)

    private val notesViewModel: NotesViewModel by lazy {
        ViewModelProvider(this)[NotesViewModel::class.java]
    }



    private lateinit var binding: ActivityMapsBinding

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE =     1001
        private const val DEFAULT_ZOOM = 15
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
        outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        updateUI()

        notesViewModel.notes.observe(this) {
            if (it != null) {
                updateUI()
            }
        }

        getLocationPermission()
        getDeviceLocation()

        lastKnownLocation?.apply {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude), DEFAULT_ZOOM.toFloat()))
        }

        mMap.setOnMarkerClickListener { marker ->
            showNoteDetail(notesViewModel.notes.value?.firstOrNull { note ->
                note.latLng == marker.position }, marker.position)}

        mMap.setOnMapLongClickListener { latLng ->
            showNoteDetail(null, latLng)}
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mMap.isMyLocationEnabled) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun updateUI() {
        notesViewModel.notes.value?.forEach {
            val bmp = BitmapDescriptorFactory.fromBitmap(buildBitmap(it, 36.0F, Color.BLACK)!!)

            mMap.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title("${it.text} - ${it.user}")
                    .icon(bmp)
            )
                ?.showInfoWindow()
        }

        try {
            if (mMap.isMyLocationEnabled) {
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    fun buildBitmap(note: Note, textSize: Float, textColor: Int): Bitmap? {
        val lines = "${note.text}\n - ${note.user}".split("\n")
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        var baseline: Float = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(lines.maxByOrNull{ it.length }) + 0.5f).toInt() + 16 // round
        val height = lines.size * 44
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image).apply {
            val canvasPaint = Paint()
            canvasPaint.color = Color.WHITE
            canvasPaint.style = Paint.Style.FILL
            drawRect(0.0f, 0.0F, width.toFloat(), height.toFloat(), canvasPaint)
            canvasPaint.color = Color.BLACK
            canvasPaint.style = Paint.Style.STROKE
            drawRect(0.0f, 0.0F, (width-1).toFloat(), (height-1).toFloat(), canvasPaint)
        }
        lines.forEach {
            canvas.drawText(it, 8.0f, baseline, paint)
            baseline += 44
        }
        return image
    }

    private fun showNoteDetail( note: Note?, latLng: LatLng): Boolean {
        val dialogInputBinding: DialogInputBinding = DialogInputBinding.inflate(layoutInflater)
        dialogInputBinding.user = note?.user
        dialogInputBinding.text = note?.text

        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Note")
            .setView( dialogInputBinding.root)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        if (note == null) {
            dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                val user = dialogInputBinding.user ?: ""
                val text = dialogInputBinding.text ?: ""
                if (user.isNotEmpty() and text.isNotEmpty()) {
                    notesViewModel.addNote(Note(user = user, text = text, latLng = latLng))
                    updateUI()
                }
            }
        }
        dialogBuilder.show()
        return true
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        mMap.isMyLocationEnabled =
            (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }
}