package com.lenivenko.compass.compass_server

import android.content.Context
import android.hardware.SensorManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val deviceServicesModule = module {
    single<OrientationService> { OrientationServiceImpl(sensorManager = get()) }

    single { androidContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
}