package com.example.el.loyaltycards.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.presenter.BarcodePresenter
import com.example.el.loyaltycards.presentation.view.BarcodeView
import kotlinx.android.synthetic.main.fragment_barcode.*

class BarcodeFragment : MvpAppCompatFragment(), BarcodeView {

    @InjectPresenter
    lateinit var presenter: BarcodePresenter

    companion object {
        @JvmStatic
        fun newInstance() = BarcodeFragment()
    }

    // LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.inject(this)
        presenter.onCreate(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_barcode, container, false)

        val moreButton : Button = rootView.findViewById(R.id.moreButton)
        moreButton.setOnClickListener{ presenter.onMoreButtonListener() }

        return rootView
    }

    override fun setCard(card: Card?) {
        if (card != null) {
            cardName.setText(card.name)
        }
    }
}
