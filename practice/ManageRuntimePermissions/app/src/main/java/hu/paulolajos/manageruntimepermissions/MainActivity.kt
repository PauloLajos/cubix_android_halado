package hu.paulolajos.manageruntimepermissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.paulolajos.manageruntimepermissions.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvText.text = StringBuilder().append("Hello world!")
    }
}