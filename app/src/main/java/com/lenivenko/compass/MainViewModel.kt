package com.lenivenko.compass

import androidx.lifecycle.*
import com.lenivenko.compass.compass_server.OrientationService
import com.lenivenko.compass.compass_server.models.Rotation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private val orientationService: OrientationService) : ViewModel(), LifecycleObserver {
    private val _rotation: MutableLiveData<Rotation> = MutableLiveData()
    val rotation: LiveData<Rotation> = _rotation

    init {
        GlobalScope.launch {
            val channel = orientationService.observe()
            for (r in channel) {
                _rotation.postValue(r)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() = orientationService.startTrack()


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() = orientationService.stopTrack()


}