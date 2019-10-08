package com.lenivenko.compass

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.rotation.observe(this, Observer { rotation -> setNewState(rotation.currentDegree, rotation.newDegree) })
        lifecycle.addObserver(viewModel)
    }

    private fun setNewState(currentDegree: Float, degree: Float) {

        // get the angle around the z-axis rotated
        heading.text = "Heading: $degree degrees"

        // create a rotation animation (reverse turn degree degrees)
        val ra = RotateAnimation(-currentDegree, -degree,
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
