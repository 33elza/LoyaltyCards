package com.example.el.loyaltycards.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.presenter.BarcodePresenter
import com.example.el.loyaltycards.presentation.view.BarcodeView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_barcode.*
import kotlinx.android.synthetic.main.fragment_barcode.view.*

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
        rootView.moreButton.setOnClickListener { presenter.onMoreButtonListener() }

        return rootView
    }

    // BarcodeView
    override fun setCard(card: Card?) {
        if (card != null) {
            cardName.setText(card.name)
        }
    }

    override fun setBarcode(cardCode: String, barcodeFormat: BarcodeFormat?) {
        if (barcodeFormat != null && isBarcode(cardCode, barcodeFormat)) {
            barcodeImageView.viewTreeObserver.apply {
                if (isAlive) {
                    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val bitMatrix = MultiFormatWriter().encode(cardCode, barcodeFormat, barcodeImageView.measuredWidth, barcodeImageView.measuredHeight)
                            barcodeImageView.setImageBitmap(BarcodeEncoder().createBitmap(bitMatrix))

                            barcodeImageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    })
                }
                cardBarcodetextView.setText("")
            }
        } else {
            cardBarcodetextView.setText(cardCode)
            barcodeImageView.setImageDrawable(null)
        }
    }

    private fun isBarcode(cardCode: String, barcodeFormat: BarcodeFormat?): Boolean {
        try {
            MultiFormatWriter().encode(cardCode, barcodeFormat, barcodeImageView.width, barcodeImageView.height)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}
