package hu.paulolajos.locationtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import hu.paulolajos.locationtracker.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var googleMap: GoogleMap? = null

    companion object {
        private val marker = LatLng(46.645870,21.285489)
        private const val DefaultCameraZoom = 10F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment: SupportMapFragment? =
            supportFragmentManager.findFragmentById(R.id.googleMap) as? SupportMapFragment

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                googleMap = mapFragment?.awaitMap()
                googleMap?.awaitMapLoad()

                moveCamera()
            }
        }
    }

    private fun moveCamera() {
        googleMap?.addMarker(
            MarkerOptions()
                .position(marker)
                .title("Marker")
        )
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                marker,
                DefaultCameraZoom
            )
        )
    }
}