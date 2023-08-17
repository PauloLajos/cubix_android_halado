package hu.paulolajos.weatherdemo_mvvm_starter.repository

import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import hu.paulolajos.weatherdemo_mvvm_starter.network.WeatherAPI
import javax.inject.Inject
class WeatherRepository
@Inject
constructor(private val apiService: WeatherAPI) {
    suspend fun getWeather() = apiService.getWeatherData()
}