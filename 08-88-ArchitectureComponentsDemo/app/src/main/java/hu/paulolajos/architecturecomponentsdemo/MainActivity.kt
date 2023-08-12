package hu.paulolajos.architecturecomponentsdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.paulolajos.architecturecomponentsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1. LifeCycleObserver
        //lifecycle.addObserver(SensorLifeCycleObserver(this))

        //2. LiveData
        val acceleroLiveData = SensorLiveData(this)

        acceleroLiveData.observe(this) { t ->
            binding.tvText.text =
                buildString {
                    append("X: ")
                    append(t!!.values[0])
                    append(" Y: ")
                    append(t.values[1])
                    append(" Z: ")
                    append(t.values[2])
                }
        }
    }
}