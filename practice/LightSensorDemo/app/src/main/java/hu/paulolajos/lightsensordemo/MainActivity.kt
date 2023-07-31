package hu.paulolajos.lightsensordemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import hu.paulolajos.lightsensordemo.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    // Initialised sensorManager & two variables
    // for storing brightness value
    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default nightmode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // searched our textview id and stored it
        text = binding.tvText

        // setupSensor Called
        setUpSensor()
    }

    // Declared setupSensor function
    private fun setUpSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    // These are two methods from sensorEventListner Interface
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0]

            text.text = StringBuilder()
                .append("Sensor: ")
                .append(light1)
                .append("\n")
                .append(brightness(light1))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    // Created a function to show messages according to the brightness
    private fun brightness(brightness: Float): String {

        return when (brightness.toInt()) {
            0 -> "Pitch black"
            in 1..10 -> "Dark"
            in 11..50 -> "Grey"
            in 51..5000 -> "Normal"
            in 5001..40000 -> "Incredibly bright"
            else -> "This light will blind you"
        }
    }

    // This is onResume function of our app
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // This is onPause function of our app
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}