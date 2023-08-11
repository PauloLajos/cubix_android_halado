package hu.paulolajos.databindingdemo

object DummyData {
    fun getDummyData(): List<Car> {
        val cars = arrayListOf<Car>()

        cars.add(Car("Opel", "2020.02.29."))
        cars.add(Car("Volkswagen", "2021.04.12."))
        cars.add(Car("Renault", "2019.09.09."))
        cars.add(Car("Toyota", "2022.12.01."))

        return cars
    }
}