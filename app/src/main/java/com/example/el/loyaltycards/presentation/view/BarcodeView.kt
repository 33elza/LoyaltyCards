package com.example.el.loyaltycards.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.el.loyaltycards.entity.Card

@StateStrategyType(AddToEndSingleStrategy::class)
interface BarcodeView : MvpView {
    fun setCard(card: Card?)
}