package hu.paulolajos.weatherdemo_mvvm_starter.network

import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("/data/2.5/weather")
    fun getWeatherData(@Query("q") cityName: String,
                       @Query("units") units: String,
                       @Query("appid") appId: String): Call<WeatherResult>
}