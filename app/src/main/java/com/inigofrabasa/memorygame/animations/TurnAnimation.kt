package com.inigofrabasa.memorygame.animations

import android.graphics.Camera
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation

class TurnAnimation(private var fromView: View, private var toView: View) : Animation() {

    init {
        duration = 800
        fillAfter = false
        interpolator = AccelerateDecelerateInterpolator()
    }

    private var camera: Camera? = null
    private var centerX = 0f
    private var centerY = 0f
    private var forward = true

    fun reverse() {
        forward = false
        val switchView = toView
        toView = fromView
        fromView = switchView
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {

        super.initialize(width, height, parentWidth, parentHeight)

        centerX = width / 2.toFloat()
        centerY = height / 2.toFloat()
        camera = Camera()
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {

        val radians = Math.PI * interpolatedTime
        var degrees = (180.0 * radians / Math.PI).toFloat()

        if (interpolatedTime >= 0.5f) {
            degrees -= 180f
            fromView.visibility = View.GONE
            toView.visibility = View.VISIBLE
        }

        if (forward) degrees = -degrees

        val matrix = t.matrix
        camera!!.save()
        camera!!.rotateY(degrees)
        camera!!.getMatrix(matrix)
        camera!!.restore()
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
    }
}