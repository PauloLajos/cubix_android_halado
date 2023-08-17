package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import hu.paulolajos.weatherdemo_mvvm_starter.repository.WeatherRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository)
    : ViewModel() {

    private val _response = MutableLiveData<WeatherResult>()
    val weatherResponse: LiveData<WeatherResult>
        get() = _response


    init {
        getWeather()
    }

    private fun getWeather() = viewModelScope.launch {
        repository.getWeather().let { response ->

            if (response.isSuccessful) {
                _response.postValue(response.body())
            } else {
                Log.d("tag", "getWeather Error: ${response.code()}")
            }
        }
    }
}