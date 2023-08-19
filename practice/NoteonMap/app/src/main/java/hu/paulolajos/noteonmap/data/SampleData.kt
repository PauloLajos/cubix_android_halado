package hu.paulolajos.noteonmap.data

import com.google.android.gms.maps.model.LatLng

    val sampleData = listOf(
        Note(
            user = "user1",
            text = "Komló étterem",
            latLng = LatLng(46.64566,21.27229)
        ),
        Note(
            user = "user2",
            text = "Rondella",
            latLng = LatLng(46.64599,21.28533)
        ),
        Note(
            user = "user3",
            text = "Almásy kastély",
            latLng = LatLng(46.64423,21.28406)
        ),
        Note(
            user = "user4",
            text = "Erkel Ferenc szülőháza",
            latLng = LatLng(46.64700,21.26798)
        ),
    )
