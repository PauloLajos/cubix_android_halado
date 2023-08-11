package hu.paulolajos.weatherdemo_mvvm_starter.ui.cities

import android.os.Bundle
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import hu.paulolajos.weatherdemo_mvvm_starter.R
import hu.paulolajos.weatherdemo_mvvm_starter.databinding.ActivityScrollingBinding
import hu.paulolajos.weatherdemo_mvvm_starter.ui.cities.adapter.CityAdapter
import hu.paulolajos.weatherdemo_mvvm_starter.model.db.City


class CitiesListActivity : AppCompatActivity() {

    private lateinit var cityAdapter: CityAdapter
    private val citiesViewModel: CitiesViewModel by viewModels()
    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            showAddCityDialog()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        cityAdapter = CityAdapter(this, citiesViewModel)
        binding.listCities.adapter = cityAdapter


        citiesViewModel.allCities.observe(this) { cities ->
            cityAdapter.submitList(cities)
        }
    }

    private fun showAddCityDialog() {
        MaterialDialog(this).show {
            noAutoDismiss()
            title(text = getString(R.string.title_city_add_dialog))
            input()

            positiveButton(text=getString(R.string.btn_add)) {
                val cityName = it.getInputField().text.toString()
                if (cityName.isNotEmpty()) {

                    //saveCity(City(null, cityName))
                    citiesViewModel.insert(City(null, cityName))

                    it.dismiss()
                } else {
                    it.getInputField().error = getString(R.string.error_empty_field)
                }
            }
            negativeButton(text=getString(R.string.btn_dismiss)) {
                it.dismiss()
            }
        }
    }
}
