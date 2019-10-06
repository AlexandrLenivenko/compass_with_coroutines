package com.lenivenko.compass.compass_server

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.lenivenko.compass.compass_server.models.Rotation
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel

internal class OrientationServiceImpl(context: Context) : OrientationService, SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var currentDegree = 0f
    private val channel: Channel<Rotation> = Channel()
    private lateinit var job: Job

    override fun startTrack() {
        // for the system's orientation sensor registered listeners
        job = Job()
        sensorManager.registerListener(
            this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun stopTrack() {
        // to stop the listener and save battery
        job.cancel()
        sensorManager.unregisterListener(this)
        currentDegree = 0f
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //do nothing
    }

    override fun onSensorChanged(event: SensorEvent) {
        // get the angle around the z-axis rotated
        val degree = Math.round(event.values[0]).toFloat()
        GlobalScope.launch(IO + job) {
            channel.send(Rotation(currentDegree, degree))
        }

        currentDegree = -degree
    }

    override fun observe(): Channel<Rotation> = channel
}