package com.example.el.loyaltycards.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface ShowImageFullscreenView: MvpView {
    fun hideActionBar()
    fun setFullScreenImageViewVisibility()
    fun setFullScreenImageViewVisibilityInitial()
    fun loadImage()

}