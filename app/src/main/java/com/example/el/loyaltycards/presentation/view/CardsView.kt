package com.example.el.loyaltycards.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.el.loyaltycards.entity.Card

@StateStrategyType(SingleStateStrategy::class)
interface CardsView : MvpView {
    fun setCards(cards: List<Card>)

    @StateStrategyType(SkipStrategy::class)
    fun showDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun notifyCardDeleted(selectedIndex: Int)
}