package com.example.el.loyaltycards.presentation.presenter

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.view.CardDetailsView

@InjectViewState
class CardDetailsPresenter : MvpPresenter<CardDetailsView>() {

    private var card: Card? = null

    fun onCreate(arguments: Bundle?) {
        if (card == null) {
            val card = arguments?.get(CARD_KEY) as Card?
            viewState.setCard(card)
        }
    }




}