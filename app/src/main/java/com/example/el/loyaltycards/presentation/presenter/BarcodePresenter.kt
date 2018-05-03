package com.example.el.loyaltycards.presentation.presenter

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.view.BarcodeView
import com.example.el.loyaltycards.ui.activity.CARD_CONTAINER_MODULE
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class BarcodePresenter : MvpPresenter<BarcodeView>() {

    private var card: Card? = null

    @Inject
    lateinit var router: Router

    fun onCreate(arguments: Bundle?) {
        if (card == null) {
            card = arguments?.get(CARD_KEY) as Card?
            viewState.setCard(card)
        }
        App.component.inject(this)
    }

    fun onMoreButtonListener() {
        router.navigateTo(CARD_CONTAINER_MODULE, Module.DETAILS.apply { this.card = this@BarcodePresenter.card })
    }

}