package hu.paulolajos.noteonmap.data

import com.google.android.gms.maps.model.LatLng

data class Note(
    val user: String = "",
    val text: String = "",
    val latLng: LatLng = LatLng(46.6473027,21.2784255)
)
