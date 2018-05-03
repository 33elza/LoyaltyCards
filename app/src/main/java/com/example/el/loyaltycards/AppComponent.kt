package com.example.el.loyaltycards

import com.example.el.loyaltycards.presentation.presenter.BarcodePresenter
import com.example.el.loyaltycards.presentation.presenter.CardPresenter
import com.example.el.loyaltycards.presentation.presenter.CardsPresenter
import com.example.el.loyaltycards.ui.activity.CardActivity
import com.example.el.loyaltycards.ui.activity.CardsActivity
import com.example.el.loyaltycards.ui.fragment.BarcodeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(cardsPresenter: CardsPresenter)
    fun inject(cardPresenter: CardPresenter)
    fun inject(cardsActivity: CardsActivity)
    fun inject(cardActivity: CardActivity)
    fun inject(barcodeFragment: BarcodeFragment)
    fun inject(barcodePresenter: BarcodePresenter)
}