package hu.paulolajos.proximitysensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import hu.paulolajos.proximitysensor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    // on below line we are creating
    // a variable for our text view.
    private lateinit var sensorStatusTV: TextView

    // on below line we are creating
    // a variable for our proximity sensor
    private lateinit var proximitySensor: Sensor

    // on below line we are creating
    // a variable for sensor manager
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // on below line we are initializing our all variables.
        sensorStatusTV = binding.tvProxySensor

        // on below line we are initializing our sensor manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // on below line we are initializing our proximity sensor variable
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // check if the sensor type is proximity sensor.
        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] == 0f) {
                // here we are setting our status to our textview..
                // if sensor event return 0 then object is closed
                // to sensor else object is away from sensor.
                sensorStatusTV.text = "Object is Near to sensor"
            } else {
                // on below line we are setting text for text view
                // as object is away from sensor.
                sensorStatusTV.text = "Object is Away from sensor"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    // This is onResume function of our app
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // This is onPause function of our app
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}