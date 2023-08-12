package hu.paulolajos.locationtrackerdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.paulolajos.locationtrackerdemo.R

class MapFragment : Fragment() {
    //
    private lateinit var googleMap: GoogleMap
    private var currentPosition = LatLng(0.0, 0.0)

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        moveCamera(currentPosition)
    }

    private fun moveCamera(currentPosition: LatLng) {
        googleMap.addMarker(MarkerOptions().position(currentPosition).title("you are here"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}