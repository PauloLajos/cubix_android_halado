package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import hu.paulolajos.weatherdemo_mvvm_starter.R
import hu.paulolajos.weatherdemo_mvvm_starter.databinding.ActivityWeatherDetailsBinding
import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import hu.paulolajos.weatherdemo_mvvm_starter.network.WeatherAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_CITY = "KEY_CITY"
    }

    private lateinit var cityName: String
    private lateinit var binding: ActivityWeatherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityName = intent.getStringExtra(KEY_CITY)!!
        binding.tvCity.text = cityName
    }

    override fun onResume() {
        super.onResume()

        getWeatherData()
    }

    private fun getWeatherData() {
        val weatherCall = prepareCall()

        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                //tvCityName.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherData = response.body()
                val icon = weatherData?.weather?.get(0)?.icon
                processResponse(weatherData, icon)
            }
        })
    }

    private fun processResponse(
        weatherData: WeatherResult?,
        icon: String?
    ) {
        Glide.with(this@WeatherDetailsActivity)
            .load("https://openweathermap.org/img/w/$icon.png")
            .into(binding.ivWeatherIcon)


        binding.tvMain.text = weatherData?.weather?.get(0)?.main
        binding.tvDescription.text = weatherData?.weather?.get(0)?.description
        binding.tvTemperature.text =
            getString(R.string.temperature, weatherData?.main?.temp?.toFloat().toString())

        val sdf = SimpleDateFormat("h:mm a z", Locale.getDefault())
        val sunriseDate = Date((weatherData?.sys?.sunrise?.toLong())!! * 1000)
        val sunriseTime = sdf.format(sunriseDate)
        binding.tvSunrise.text = getString(R.string.sunrise, sunriseTime)
        val sunsetDate = Date(weatherData.sys.sunset?.toLong()!! * 1000)
        val sunsetTime = sdf.format(sunsetDate)
        binding.tvSunset.text = getString(R.string.sunset, sunsetTime)
    }

    private fun prepareCall(): Call<WeatherResult> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherAPI::class.java)

        var weatherApiKey = ""
        try {
            val app: ApplicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this.packageManager.getApplicationInfo(
                    this.packageName,
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                this.packageManager
                    .getApplicationInfo(this.packageName, PackageManager.GET_META_DATA)
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
}