package hu.paulolajos.databindingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import hu.paulolajos.databindingdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Data binding !!!
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.dataBindingDemoString = "Hello data binding!"

        binding.tvViewBinding.text = "Hello view binding!"

        binding.car = Car("Suzuki Vitara", "2020.01.01.")
    }
}