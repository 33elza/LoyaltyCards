package com.example.el.loyaltycards.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.presentation.presenter.BarcodeCapturePresenter
import com.example.el.loyaltycards.presentation.view.BarcodeCaptureView
import me.dm7.barcodescanner.zxing.ZXingScannerView
import javax.inject.Inject


class BarcodeCaptureActivity : MvpAppCompatActivity(), BarcodeCaptureView {

    @InjectPresenter
    lateinit var presenter: BarcodeCapturePresenter

    @Inject
    lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.inject(this)
        setContentView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        sendBarcodeResult()
        presenter.startCamera(scannerView)
    }

    override fun onPause() {
        super.onPause()
        presenter.stopCamera(scannerView)
    }

    fun sendBarcodeResult() {
        scannerView.setResultHandler(ZXingScannerView.ResultHandler { rawResult ->
            val intent = Intent()
            intent.putExtra("barcode", rawResult.text)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
    }
}
