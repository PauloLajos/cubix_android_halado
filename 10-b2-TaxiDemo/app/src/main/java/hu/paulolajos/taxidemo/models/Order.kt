package hu.paulolajos.taxidemo.models

import com.google.android.gms.maps.model.LatLng

data class Order(
    val startLocation:LocationModel,
    val endLocation: LatLng,
    val price:Double,
    val distance:Double
)
