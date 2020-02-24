package com.example.kouveemanagement

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

object Animation {

    fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(100)
            .setListener(object : AnimatorListenerAdapter() {
            })
            .rotation(if (rotate) 90f else 0f)
        return rotate
    }

    fun showIn(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.translationY = view.height.toFloat()
        view.animate()
            .setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
            })
            .alpha(1f)
            .start()
    }

    fun showOut(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 1f
        view.translationY = 0f
        view.animate()
            .setDuration(200)
            .translationY(view.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(0f)
            .start()
    }

    fun init(view: View) {
        view.visibility = View.GONE
        view.translationY = view.height.toFloat()
        view.alpha = 0f
    }
}
