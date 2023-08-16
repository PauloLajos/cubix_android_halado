package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.paulolajos.weatherdemo_mvvm_starter.ui.weather.repository.Weather
import hu.paulolajos.weatherdemo_mvvm_starter.ui.weather.repository.WeatherRepository

class WeatherViewModel : ViewModel() {
    private val weatherRepository = WeatherRepository()

    private val _weatherData = MutableLiveData<Weather>()
    val weatherData: LiveData<Weather> = _weatherData

    fun refreshWeatherData(context: Context,cityName: String) {
        val weather = weatherRepository.getWeatherData(context,cityName)
        _weatherData.value = weather
    }
}