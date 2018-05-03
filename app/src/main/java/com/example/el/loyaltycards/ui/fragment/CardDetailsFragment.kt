package com.example.el.loyaltycards.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.presenter.CardDetailsPresenter
import com.example.el.loyaltycards.presentation.view.CardDetailsView
import kotlinx.android.synthetic.main.fragment_card_details.*

class CardDetailsFragment : MvpAppCompatFragment(), CardDetailsView {

    @InjectPresenter
    lateinit var presenter: CardDetailsPresenter

    companion object {
        @JvmStatic
        fun newInstance() = CardDetailsFragment()
    }

    // LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    // CardDetailsView
    override fun setCard(card: Card?) {
        if (card != null) {
            cardNameEditText.setText(card.name)
            cardNameEditText.isEnabled = false
        }
    }

}