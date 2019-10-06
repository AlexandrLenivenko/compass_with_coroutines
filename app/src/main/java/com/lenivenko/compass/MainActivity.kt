package com.lenivenko.compass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.lifecycle.lifecycleScope
import com.lenivenko.compass.compass_server.OrientationService
import com.lenivenko.compass.compass_server.models.Rotation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val orientationService: OrientationService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenResumed {
            val channel = orientationService.observe()
            for (rotation in channel) {
                setNewState(rotation.currentDegree, rotation.newDegree)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationService.startTrack()
    }

    private suspend fun setNewState(currentDegree: Float, degree: Float) {
        withContext(Main) {
            // get the angle around the z-axis rotated
            heading.text = "Heading: $degree degrees"

            // create a rotation animation (reverse turn degree degrees)
            val ra = RotateAnimation(
                currentDegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )

            // how long the animation will take place
            ra.duration = 10

            // set the animation after the end of the reservation status
            ra.fillAfter = true

            // Start the animation
            image.startAnimation(ra)
        }
    }

    override fun onStop() {
        super.onStop()
        orientationService.stopTrack()
    }
}
