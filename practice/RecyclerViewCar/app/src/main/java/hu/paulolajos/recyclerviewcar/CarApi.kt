package hu.paulolajos.recyclerviewcar

object CarApi {
    fun fetchCars(): MutableList<Car> {
        // Make API call or fetch data from a database
        // Return the list of cars
        val carList: MutableList<Car> = mutableListOf()
        carList.add(
            Car(
                "0",
                "Suzuki",
                "Vitara",
                2001,
                "",
                "good"
            ),
        )
        carList.add(
            Car(
                "1",
                "Suzuki",
                "Swift",
                2002,
                "",
                "good"
            )
        )
        carList.add(
            Car(
                "2",
                "Suzuki",
                "SX4",
                2003,
                "",
                "good"
            )
        )
        return carList
    }
}