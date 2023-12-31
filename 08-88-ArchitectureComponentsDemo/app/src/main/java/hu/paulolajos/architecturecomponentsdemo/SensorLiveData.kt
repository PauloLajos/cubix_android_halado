package hu.paulolajos.architecturecomponentsdemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.lifecycle.LiveData

class SensorLiveData(private val context: Context) : LiveData<SensorEvent>(),
    SensorEventListener {

    // init
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val acceleroSensor: Sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    override fun onActive() {
        super.onActive()
        sensorManager.registerListener(
            this,
            acceleroSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(this)
        Toast.makeText(context, "INACTIVATE", Toast.LENGTH_SHORT).show()
    }


    override fun onSensorChanged(event: SensorEvent?) {
        value = event!!
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }
}