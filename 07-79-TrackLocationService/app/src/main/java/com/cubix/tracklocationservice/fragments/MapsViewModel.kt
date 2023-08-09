package com.cubix.tracklocationservice.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {
    private val mutablePosition = MutableLiveData<LatLng>()
    val position: LiveData<LatLng> get() = mutablePosition

    fun setPosition(position: LatLng) {
        mutablePosition.value = position
    }
}