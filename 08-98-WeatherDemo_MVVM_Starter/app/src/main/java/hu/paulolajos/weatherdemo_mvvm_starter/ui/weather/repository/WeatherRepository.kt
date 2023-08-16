package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import hu.paulolajos.weatherdemo_mvvm_starter.network.WeatherAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

data class Weather(
    val temperature: Double,
    val humidity: Int,
    val conditions: String
)

class WeatherRepository {
    fun getWeatherData(context: Context, cityName: String): Weather {

        val weatherCall = prepareCall(context, cityName)

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                //binding.tvCity.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherData = response.body()
                val icon = weatherData?.weather?.get(0)?.icon
                processResponse(weatherData, icon)
            }
        })
        // Fetch weather data from a remote server or local storage
        return Weather(25.5, 70, "Sunny")
    }

    private fun prepareCall(context: Context, cityName: String): Call<WeatherResult> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherAPI::class.java)

        // get WEATHER_API_KEY from local.properties file
        var weatherApiKey = ""
        try {
            val app: ApplicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager
                    .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            }
            weatherApiKey = app.metaData.getString("WEATHER_API_KEY").toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return weatherApi.getWeatherData(
            cityName,
            "metric",
            weatherApiKey
        )
    }

    private fun processResponse(
        weatherData: WeatherResult?,
        icon: String?
    ): Weather {
        return Weather(
            weatherData?.main?.temp!!,
            50,
            50.toString()
        )
    }
}