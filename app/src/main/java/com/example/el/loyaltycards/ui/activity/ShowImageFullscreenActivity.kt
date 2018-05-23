package com.example.el.loyaltycards.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.presentation.presenter.ShowImageFullscreenPresenter
import com.example.el.loyaltycards.presentation.view.ShowImageFullscreenView
import kotlinx.android.synthetic.main.activity_show_image_fullscreen.*

class ShowImageFullscreenActivity : MvpAppCompatActivity(), ShowImageFullscreenView {

    @InjectPresenter
    lateinit var presenter: ShowImageFullscreenPresenter

    var uri: Uri? = null

    // LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image_fullscreen)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenImageView.setOnClickListener { presenter.onFullscreenImageViewClick() }

        presenter.onCreate()
    }

    // ShowImageFullscreenView
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.onPostCreate()
    }

    override fun loadImage() {
        Glide.with(this).load(intent.extras.get(URI) as Uri).into(fullscreenImageView)
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }

    override fun setFullScreenImageViewVisibility() {
        fullscreenImageView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun setFullScreenImageViewVisibilityInitial() {
        fullscreenImageView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        val UI_ANIMATION_DELAY = 300
    }
}
