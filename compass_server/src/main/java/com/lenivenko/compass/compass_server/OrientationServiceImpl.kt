package com.lenivenko.compass.compass_server

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.lenivenko.compass.compass_server.models.Rotation
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class OrientationServiceImpl(private val sensorManager: SensorManager) : OrientationService,
    SensorEventListener {

    private val acceslerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var azimut = 0f

    var gravity: FloatArray? = null
    var geomagnetic: FloatArray? = null

    private val channel: Channel<Rotation> = Channel()
    private lateinit var job: Job

    override fun startTrack() {
        // for the system's orientation sensor registered listeners
        job = Job()

        sensorManager.registerListener(this, acceslerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun stopTrack() {
        // to stop the listener and save battery
        job.cancel()
        sensorManager.unregisterListener(this)
        azimut = 0f
    }


    override fun onAccuracyChanged(sensor: Sensor, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values
        if (gravity != null && geomagnetic != null) {
            val r = FloatArray(9)
            val i = FloatArray(9)
            val success = SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                val currentAzimut = (Math.toDegrees(orientation[0].toDouble()).toFloat() + 360) % 360
                GlobalScope.launch(IO + job) {
                    channel.send(Rotation(currentAzimut, azimut))
                }
                azimut = currentAzimut // orientation contains: azimut, pitch and roll
            }
        }
    }

    override fun observe(): Channel<Rotation> = channel
}