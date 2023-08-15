package hu.paulolajos.taxidemo.models

data class OrderData(
    val price:Double,
    val distance: Int
) {
    constructor():this(0.0,0)
}
