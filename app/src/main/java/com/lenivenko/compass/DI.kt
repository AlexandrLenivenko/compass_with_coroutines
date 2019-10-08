package com.lenivenko.compass

import com.lenivenko.compass.compass_server.deviceServicesModule
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule = module{
    viewModel { MainViewModel(orientationService = get())}
}

val appComponent: List<Module> = listOf(deviceServicesModule, viewModelModule)