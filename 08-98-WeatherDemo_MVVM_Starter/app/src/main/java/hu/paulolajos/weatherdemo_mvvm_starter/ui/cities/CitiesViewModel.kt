package hu.paulolajos.weatherdemo_mvvm_starter.ui.cities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import hu.paulolajos.weatherdemo_mvvm_starter.database.AppDatabase
import hu.paulolajos.weatherdemo_mvvm_starter.model.db.City
import hu.paulolajos.weatherdemo_mvvm_starter.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitiesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : CityRepository
    val allCities: LiveData<List<City>>

    init {
        val citiesDao = AppDatabase.getInstance(application).cityDao()
        repository = CityRepository(citiesDao)
        allCities = repository.getAllCities()
    }

    fun insert(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(city)
    }

    fun delete(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(city)
    }

}