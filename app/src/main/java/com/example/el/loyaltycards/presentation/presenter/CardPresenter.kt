package com.example.el.loyaltycards.presentation.presenter

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.view.CardView
import com.example.el.loyaltycards.ui.activity.CARD_BARCODE_MODULE
import com.example.el.loyaltycards.ui.activity.CARD_DETAILS_MODULE
import ru.terrakok.cicerone.Router
import javax.inject.Inject

const val CARD_KEY = "CARD_KEY"
const val MODULE_KEY = "MODULE_KEY"

enum class Module(var card: Card?) {
    DETAILS(card = null),
    BARCODE(card = null)
}

@InjectViewState
class CardPresenter : MvpPresenter<CardView>() {

    @Inject
    lateinit var router: Router

    init {
        App.component.inject(this)
    }

    fun onCreate(extras: Bundle?) {
        if (extras != null) {
            when (Module.values()[extras.get(MODULE_KEY) as Int]) {
                Module.DETAILS -> router.newRootScreen(CARD_DETAILS_MODULE, extras.get(CARD_KEY))
                Module.BARCODE -> router.newRootScreen(CARD_BARCODE_MODULE, extras.get(CARD_KEY))
            }
        }
    }
}