package hu.paulolajos.taxidemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.taxidemo.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}