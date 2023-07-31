package hu.paulolajos.lightsensordemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.lightsensordemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}