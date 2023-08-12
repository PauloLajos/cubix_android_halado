package hu.paulolajos.architecturecomponentsdemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

class SensorLifeCycleObserver(context: Context) : DefaultLifecycleObserver, SensorEventListener {

    // init
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accSensor: Sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startSensorMonitoring() {
        sensorManager.registerListener(
            this,
            accSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopSensorMonitoring() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("TAG_SENSOR",
            "X: ${event!!.values[0]} Y: ${event.values[1]} Z: ${event.values[2]}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }
}