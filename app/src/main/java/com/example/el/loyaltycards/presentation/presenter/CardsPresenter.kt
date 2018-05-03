package com.example.el.loyaltycards.presentation.presenter

import android.graphics.Color
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.App
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
import kotlin.collections.ArrayList

@InjectViewState
class CardsPresenter : MvpPresenter<CardsView>() {

    @Inject
    lateinit var cardsRepository: ICardsRepository

    @Inject
    lateinit var router: Router

    private var selectedCard: Card? = null
    private var selectedIndex: Int? = null

    var cards = ArrayList<Card>()

    //TODO удалить
    val random = Random()

    init {
        App.component.inject(this)
    }

    fun onCreate() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        cardsRepository.getCards()
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun onAddFabListener() {
        cardsRepository.insertCard(Card(name = System.currentTimeMillis().toString(),
                color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))))
        cardsRepository.getCards()

        //router.navigateTo(CARD_CONTAINER_MODULE, Module.DETAILS.apply { card = null })
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
}