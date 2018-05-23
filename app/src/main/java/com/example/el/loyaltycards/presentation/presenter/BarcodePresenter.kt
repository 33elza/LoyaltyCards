package com.example.el.loyaltycards.presentation.presenter

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.view.BarcodeView
import com.example.el.loyaltycards.repository.ICardsRepository
import com.example.el.loyaltycards.ui.activity.CARD_CONTAINER_MODULE
import com.google.zxing.BarcodeFormat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.terrakok.cicerone.Router
import javax.inject.Inject

const val EAN_8_SIZE = 8
const val EAN_13_SIZE = 13
const val CODE_128 = 9

@InjectViewState
class BarcodePresenter : MvpPresenter<BarcodeView>() {

    private var card: Card? = null

    @Inject
    lateinit var router: Router

    fun onCreate(arguments: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        if (card == null) {
            card = arguments?.get(CARD_KEY) as Card?
        }
        App.component.inject(this)
    }

    fun onResume() {
        if (card != null) {
            viewState.setCard(card)
            viewState.setBarcode(card!!.code, defineBarcodeFormat(card!!.code))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardInserted(card: ICardsRepository.CardInserted) {
        this.card = card.card
    }

    fun onMoreButtonListener() {
        router.navigateTo(CARD_CONTAINER_MODULE, Module.DETAILS.apply { this.card = this@BarcodePresenter.card })
    }

    fun defineBarcodeFormat(cardCode: String): BarcodeFormat? {
        when (cardCode.length) {
            EAN_8_SIZE -> return BarcodeFormat.valueOf("EAN_8")
            EAN_13_SIZE -> return BarcodeFormat.valueOf("EAN_13")
            CODE_128 -> return BarcodeFormat.valueOf("CODE_128")
            else -> return null
        }
    }
}