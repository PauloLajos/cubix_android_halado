package hu.paulolajos.weatherdemo_mvvm_starter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.paulolajos.weatherdemo_mvvm_starter.model.db.City

@Dao
interface CityDAO {
    @Query("SELECT * FROM city")
    fun getAllCities(): LiveData<List<City>>

    @Insert
    suspend fun insertCity(city: City) : Long

    @Insert
    suspend fun insertCities(vararg city: City): List<Long>

    @Insert
    suspend fun insertCitiesList(cities: List<City>): List<Long>

    @Delete
    suspend fun deleteCity(city: City)

    @Query("DELETE FROM city")
    suspend fun deleteAllCity()

    @Update
    suspend fun updateCity(city: City)
}