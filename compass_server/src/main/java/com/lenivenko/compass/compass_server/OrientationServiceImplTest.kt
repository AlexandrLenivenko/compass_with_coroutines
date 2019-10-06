package com.lenivenko.compass.compass_server

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.lenivenko.compass.compass_server.models.Rotation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class OrientationServiceImplTest(context: Context) : OrientationService {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val channel: Channel<Rotation> = Channel()

    override fun startTrack() {

    }

    override fun stopTrack() {

    }

    override fun observe(): Channel<Rotation> = channel
}