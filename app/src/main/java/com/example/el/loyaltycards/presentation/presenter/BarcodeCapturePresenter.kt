package com.example.el.loyaltycards.presentation.presenter

import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.presentation.view.BarcodeCaptureView
import me.dm7.barcodescanner.zxing.ZXingScannerView

class BarcodeCapturePresenter : MvpPresenter<BarcodeCaptureView>() {

    fun startCamera(scannerView: ZXingScannerView) {
        scannerView.startCamera()
    }

    fun stopCamera(scannerView: ZXingScannerView) {
        scannerView.stopCamera()
    }
}