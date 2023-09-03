package hu.paulolajos.recyclerviewcar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import hu.paulolajos.recyclerviewcar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val carAdapter by lazy { CarAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        //carAdapter = CarAdapter()
        binding.recyclerView.adapter = carAdapter
        //fetchData()
    }

    private fun fetchData() {
        // Fetch your list of cars from a data source (e.g., API, database)
        val cars = CarApi.fetchCars()
        // Pass the list of cars to the adapter
        carAdapter.differ.submitList(cars)
    }
}