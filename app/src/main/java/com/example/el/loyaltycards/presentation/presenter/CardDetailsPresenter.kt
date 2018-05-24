package com.example.el.loyaltycards.presentation.presenter

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.graphics.Palette
import android.text.Editable
import android.text.TextWatcher
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.view.CardDetailsView
import com.example.el.loyaltycards.repository.ICardsRepository
import com.example.el.loyaltycards.ui.activity.FULLSCREEN_MODULE
import com.example.el.loyaltycards.utils.ICardValidator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.terrakok.cicerone.Router
import javax.inject.Inject

enum class ImageModule(var uri: Uri?) {
    FRONT(uri = null),
    BACK(uri = null)
}

@InjectViewState
class CardDetailsPresenter : MvpPresenter<CardDetailsView>() {

    @Inject
    lateinit var cardsRepository: ICardsRepository

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var cardValidator: ICardValidator

    private var id: Long = 0
    private var name: String = ""
    private var code: String = ""
    private var color: Int = Color.WHITE
    private var frontPhotoUri: Uri? = null
    private var backPhotoUri: Uri? = null
    private var note: String = ""

    init {
        App.component.inject(this)
    }

    fun onCreate(arguments: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        viewState.checkPermissions()

        if (id <= 0 && name.isEmpty() && arguments != null && arguments.containsKey(CARD_KEY)) {
            val card = arguments.get(CARD_KEY) as Card

            id = card.id
            name = card.name
            code = card.code.toString()
            color = card.color
            frontPhotoUri = card.frontPhoto
            backPhotoUri = card.backPhoto
            note = card.note
        }
        viewState.setName(name)
        viewState.setCode(code)
        viewState.setFrontPhoto(frontPhotoUri)
        viewState.setBackPhoto(backPhotoUri)
        viewState.setNote(note)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun onAddPhotoClick(requestImage: Int) {
        viewState.openImageCapture(requestImage)
    }

    fun onSaveCardButtonClick() {
        if (cardValidator.isNameValid(name, onInvalid = { error -> viewState.showError(error) })
                && cardValidator.isCodeValid(code, onInvalid = { error -> viewState.showError(error) })
                && cardValidator.isFrontPhotoValid(frontPhotoUri, onInvalid = { error -> viewState.showError(error) })
        ) {
            cardsRepository.insertCard(Card(id = id,
                    name = name,
                    color = color,
                    code = code,
                    frontPhoto = frontPhotoUri,
                    backPhoto = backPhotoUri,
                    note = note))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardInserted(card: ICardsRepository.CardInserted) {
        router.exit()
    }

    fun getCardNameTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                name = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }
    }

    fun getCardCodeTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isNotEmpty()) {
                    code = charSequence.toString()
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }
    }

    fun getCardNoteTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                note = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }
    }

    fun setBackPhotoUri(imageUri: Uri) {
        backPhotoUri = imageUri
    }

    fun setFrontPhotoUri(imageUri: Uri, bitmap: Bitmap) {
        frontPhotoUri = imageUri

        val dominantSwatch = Palette.from(bitmap).generate().dominantSwatch

        if (dominantSwatch != null) {
            color = dominantSwatch.rgb
        }
    }

    fun onBarcodeReadButtonClick() {
        viewState.openBarcodeCaptureActivity()
    }

    fun onFrontPhotoImageViewClick() {
        router.navigateTo(FULLSCREEN_MODULE, ImageModule.FRONT.apply { uri = frontPhotoUri })
    }

    fun onBackPhotoImageViewClick() {
        router.navigateTo(FULLSCREEN_MODULE, ImageModule.BACK.apply { uri = backPhotoUri })
    }


}