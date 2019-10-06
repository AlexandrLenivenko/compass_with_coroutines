package com.lenivenko.compass

import com.lenivenko.compass.compass_server.orientationServiceModule
import org.koin.core.module.Module

val appComponent: List<Module> = listOf(orientationServiceModule)