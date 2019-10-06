package com.lenivenko.compass.compass_server

import com.lenivenko.compass.compass_server.models.Rotation
import kotlinx.coroutines.channels.Channel


interface OrientationService {
    fun startTrack()
    fun stopTrack()

    fun observe(): Channel<Rotation>
}