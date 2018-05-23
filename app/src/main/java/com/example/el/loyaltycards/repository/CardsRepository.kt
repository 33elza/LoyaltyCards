package com.example.el.loyaltycards.repository

import com.example.el.loyaltycards.db.DataBase
import com.example.el.loyaltycards.entity.Card
import kotlinx.coroutines.experimental.async
import org.greenrobot.eventbus.EventBus

class CardsRepository(private val db: DataBase) : ICardsRepository {

    override fun insertCard(card: Card) {
        async {
            db.cardsDao().insert(card)
            EventBus.getDefault().post(ICardsRepository.CardInserted(card))
        }
    }

    override fun deleteCard(card: Card) {
        async {
            db.cardsDao().delete(card)
            EventBus.getDefault().post(ICardsRepository.CardDeleted(card))
        }
    }

    override fun getCards() {
        async {
            EventBus.getDefault().post(ICardsRepository.Cards(db.cardsDao().cards()))
        }
    }

    override fun getCardsSortedByName() {
        async {
            EventBus.getDefault().post(ICardsRepository.Cards(db.cardsDao().cardsSortedByName()))
        }
    }
}