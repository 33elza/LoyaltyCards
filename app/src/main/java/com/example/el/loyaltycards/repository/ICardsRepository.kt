package com.example.el.loyaltycards.repository

import com.example.el.loyaltycards.entity.Card

interface ICardsRepository {
    fun getCards()
    fun insertCard(card: Card)
    fun deleteCard(card: Card)
    fun getCardsSortedByName()

    data class CardDeleted(val card: Card)
    data class CardInserted(val card: Card)

    data class Cards(val cardList: List<Card>)
}