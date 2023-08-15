package hu.paulolajos.taxidemo.models

data class LocationModel(
    val longitude: Double,
    val latitude: Double
) {
    constructor() : this(0.0, 0.0)
}