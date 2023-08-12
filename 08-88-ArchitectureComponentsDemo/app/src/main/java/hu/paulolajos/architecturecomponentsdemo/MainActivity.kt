package hu.paulolajos.architecturecomponentsdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import hu.paulolajos.architecturecomponentsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private val sensorViewModel: SensorViewModel by viewModels()
    private lateinit var sensorViewModel: SensorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1. LifeCycleObserver
        //lifecycle.addObserver(SensorLifeCycleObserver(this))

        //2. LiveData
        /*val acceleroLiveData = SensorLiveData(this)

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
         */

        //3. ViewModel
        sensorViewModel = ViewModelProvider(this)
            .get(modelClass = SensorViewModel::class.java)

        sensorViewModel.getAcceleroLiveData(this)?.observe(this) { t ->
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