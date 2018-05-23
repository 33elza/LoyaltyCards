package com.example.el.loyaltycards.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.view.CardsView
import com.example.el.loyaltycards.repository.ICardsRepository
import com.example.el.loyaltycards.ui.activity.CARD_CONTAINER_MODULE
import com.example.el.loyaltycards.ui.adapter.CardListAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class CardsPresenter : MvpPresenter<CardsView>() {

    @Inject
    lateinit var cardsRepository: ICardsRepository

    @Inject
    lateinit var router: Router

    private var selectedCard: Card? = null
    private var selectedIndex: Int? = null

    private var selectedMenuItemId: Int = R.id.sortByDateItem

    var cards = ArrayList<Card>()

    init {
        App.component.inject(this)
    }

    fun onCreate() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        when (selectedMenuItemId) {
            R.id.sortByDateItem -> cardsRepository.getCards()
            R.id.sortByNameItem -> cardsRepository.getCardsSortedByName()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardsEvent(cards: ICardsRepository.Cards) {
        this.cards = cards.cardList as ArrayList<Card>
        viewState.setCards(cards.cardList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardDeleted(card: ICardsRepository.CardDeleted) {
        if (selectedIndex != null) {
            cards.removeAt(selectedIndex!!)
            viewState.notifyCardDeleted(selectedIndex!!)

            selectedIndex = null
            selectedCard = null
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardInserted(cards: ICardsRepository.CardInserted) {
        when (selectedMenuItemId) {
            R.id.sortByDateItem -> cardsRepository.getCards()
            R.id.sortByNameItem -> cardsRepository.getCardsSortedByName()
        }
        viewState.setCards(this@CardsPresenter.cards)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun onAddFabListener() {
        router.navigateTo(CARD_CONTAINER_MODULE, Module.DETAILS.apply { card = null })
    }

    fun onEditCard() {
        router.navigateTo(CARD_CONTAINER_MODULE, Module.DETAILS.apply { card = this@CardsPresenter.selectedCard })
    }

    fun onDeleteCard() {
        cardsRepository.deleteCard(selectedCard!!)
    }

    fun getAdapterListener(): CardListAdapter.Listener {
        return object : CardListAdapter.Listener {
            override fun onCardLongClick(card: Card, index: Int) {
                this@CardsPresenter.selectedCard = card
                this@CardsPresenter.selectedIndex = index
                viewState.showDialog()
            }

            override fun onCardClick(card: Card, index: Int) {
                router.navigateTo(CARD_CONTAINER_MODULE, Module.BARCODE.apply { this.card = card })
            }
        }
    }

    fun onSortByNameItemClicked() {
        cardsRepository.getCardsSortedByName()
        selectedMenuItemId = R.id.sortByNameItem
    }

    fun onSortByDateItemClicked() {
        cardsRepository.getCards()
        selectedMenuItemId = R.id.sortByDateItem
    }

    fun getSelectedMenuItem(): Int {
        return selectedMenuItemId
    }
}