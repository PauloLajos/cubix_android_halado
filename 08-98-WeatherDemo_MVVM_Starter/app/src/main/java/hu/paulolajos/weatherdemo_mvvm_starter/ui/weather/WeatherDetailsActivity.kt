package hu.paulolajos.weatherdemo_mvvm_starter.ui.weather

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import hu.paulolajos.weatherdemo_mvvm_starter.R
import hu.paulolajos.weatherdemo_mvvm_starter.databinding.ActivityWeatherDetailsBinding
import hu.paulolajos.weatherdemo_mvvm_starter.model.network.WeatherResult
import hu.paulolajos.weatherdemo_mvvm_starter.network.WeatherAPI
import hu.paulolajos.weatherdemo_mvvm_starter.ui.weather.model.WeatherViewModel
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

    // mvvm
    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var binding: ActivityWeatherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityName = intent.getStringExtra(KEY_CITY)!!
        //binding.tvCity.text = cityName

        // mvvm
        binding.viewModel = weatherViewModel
        binding.lifecycleOwner = this
        weatherViewModel.refreshWeatherData(this,cityName)
    }
}