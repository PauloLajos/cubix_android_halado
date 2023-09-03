package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.weatherdemo_mvvm_starter.databinding.ActivityWeatherDetailsBinding
import java.util.*

class WeatherDetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_CITY = "KEY_CITY"
    }

    private lateinit var binding: ActivityWeatherDetailsBinding

    private lateinit var cityName: String
    private var weatherApiKey = ""

    private val weatherViewModel: WeatherViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityName = intent.getStringExtra(KEY_CITY)!!
        binding.tvCity.text = cityName

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

        binding.viewModel = weatherViewModel
        binding.lifecycleOwner = this
        weatherViewModel.getWeatherData(cityName,weatherApiKey)
    }
}