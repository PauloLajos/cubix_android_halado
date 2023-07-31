package hu.paulolajos.sensorspresent

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import hu.paulolajos.sensorspresent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Information about Sensors present in the
    // device is supplied by Sensor Manager of the device
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the variable sensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // getSensorList(Sensor.TYPE_ALL) lists all the sensors present in the device
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        // Text View that shall display this list
        val textView = binding.tvList

        // Converting List to String and displaying
        // every sensor and its information on a new line
        for (sensors in deviceSensors) {
            textView.append(sensors.toString() + "\n\n")
        }

        textView.movementMethod = ScrollingMovementMethod()
    }
}