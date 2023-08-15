package hu.paulolajos.taxidemo.models

data class AvailableDrive(
    val name:String,
    val user: String,
    val lat: Double,
    val lng:Double,
    val price:Double,
    val distance:Int
)
