package com.example.el.loyaltycards.presentation.view

import android.net.Uri
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*

@StateStrategyType(AddToEndSingleStrategy::class)
interface CardDetailsView : MvpView {
    fun setName(name: String)
    fun setCode(code: String)
    fun setFrontPhoto(frontPhoto: Uri?)
    fun setBackPhoto(backPhoto: Uri?)
    fun setNote(note: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openImageCapture(requestImage: Int)

    @StateStrategyType(SingleStateStrategy::class)
    fun showError(error: Int)

    @StateStrategyType(SkipStrategy::class)
    fun openBarcodeCaptureActivity()
}