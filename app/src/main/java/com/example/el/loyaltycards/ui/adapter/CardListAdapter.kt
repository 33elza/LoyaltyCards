package com.example.el.loyaltycards.ui.adapter

import android.graphics.Color
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import kotlinx.android.synthetic.main.item_object.view.*

class CardListAdapter(var cards: List<Card>, val listener: Listener) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_object, parent, false))
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.index = position

        val card = cards[position]

        holder.cardNameTextView.setText(card.name)
        holder.cardView.setCardBackgroundColor(card.color)
        if (ColorUtils.calculateLuminance(card.color) < 0.5) {
            holder.cardNameTextView.setTextColor(Color.WHITE)
        } else {
            holder.cardNameTextView.setTextColor(Color.BLACK)
        }
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var index = 0
        val cardNameTextView = itemView.findViewById<TextView>(R.id.cardNameTextView)
        val cardView = itemView.findViewById<CardView>(R.id.cardView)

        init {
            itemView.cardView.setOnClickListener {
                listener.onCardClick(cards[index], index)
            }
            itemView.cardView.setOnLongClickListener {
                listener.onCardLongClick(cards[index], index)
                true
            }
        }
    }

    interface Listener {
        fun onCardClick(card: Card, index: Int)
        fun onCardLongClick(card: Card, index: Int)
    }
}