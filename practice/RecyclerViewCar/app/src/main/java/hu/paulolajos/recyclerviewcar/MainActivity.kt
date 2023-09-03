package hu.paulolajos.recyclerviewcar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.recyclerviewcar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvText.text = StringBuilder().append("Hello word!")
    }
}