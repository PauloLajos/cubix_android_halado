package com.cubix.tracklocationservice.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cubix.tracklocationservice.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by activityViewModels()

    private var currentPosition = LatLng(0.0, 0.0)
    private var gMap: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap
        moveCamera(currentPosition)
    }

    private fun moveCamera(currentPosition: LatLng) {
        gMap!!.addMarker(MarkerOptions().position(currentPosition).title("you are here"))
        gMap!!.animateCamera(CameraUpdateFactory.newLatLng(currentPosition))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        viewModel.position.observe(viewLifecycleOwner) { position ->
            currentPosition = position
            gMap?.clear()
        }
    }
}