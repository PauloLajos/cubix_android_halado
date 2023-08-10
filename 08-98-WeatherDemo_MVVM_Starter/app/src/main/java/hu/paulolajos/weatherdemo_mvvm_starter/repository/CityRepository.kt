package hu.paulolajos.weatherdemo_mvvm_starter.repository

import androidx.lifecycle.LiveData
import hu.paulolajos.weatherdemo_mvvm_starter.database.CityDAO
import hu.paulolajos.weatherdemo_mvvm_starter.model.db.City

class CityRepository(private val cityDao: CityDAO) {

    fun getAllCities() : LiveData<List<City>> {
        return cityDao.getAllCities()
    }

    suspend fun insert(city: City) {
        cityDao.insertCity(city)
    }

    suspend fun delete(city: City) {
        cityDao.deleteCity(city)
    }
}