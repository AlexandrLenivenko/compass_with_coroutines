package com.lenivenko.compass.compass_server

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val orientationServiceModule = module {
    single<OrientationService> { OrientationServiceImpl(context = androidContext())}
}