package hu.paulolajos.taxidemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.taxidemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}