package com.example.el.loyaltycards.presentation.presenter

import android.net.Uri
import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.presentation.view.ShowImageFullscreenView
import com.example.el.loyaltycards.ui.activity.ShowImageFullscreenActivity

@InjectViewState
class ShowImageFullscreenPresenter : MvpPresenter<ShowImageFullscreenView>() {

    private val mHideHandler = Handler()

    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        viewState.setFullScreenImageViewVisibilityInitial()
    }

    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    private var imageUri: Uri? = null

    fun onCreate(uri: Uri) {
        mVisible = true
        imageUri = uri
        viewState.loadImage(imageUri!!)
    }

    fun onFullscreenImageViewClick() {
        toggle()
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        viewState.hideActionBar()
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, ShowImageFullscreenActivity.UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        viewState.setFullScreenImageViewVisibility()
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
    }

    fun onPostCreate() {
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }
}