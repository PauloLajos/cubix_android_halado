package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import hu.paulolajos.weatherdemo_mvvm_starter.network.WeatherAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResult>()
    var weatherData: LiveData<WeatherResult> = _weatherData

    fun getWeatherData(cityName: String, weatherApiKey: String) {

        val weatherCall = prepareCall(cityName, weatherApiKey)

        weatherCall.enqueue(object : Callback<WeatherResult> {

            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                //
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                _weatherData.value = response.body()
            }
        })
    }

    private fun prepareCall(cityName: String, weatherApiKey: String): Call<WeatherResult> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherAPI::class.java)

        return weatherApi.getWeatherData(
            cityName,
            "metric",
            weatherApiKey
        )
    }
}