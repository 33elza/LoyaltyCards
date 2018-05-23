package com.example.el.loyaltycards.utils

import android.net.Uri
import com.example.el.loyaltycards.R

class CardValidator : ICardValidator {

    override fun isNameValid(name: String, onInvalid: (error: Int) -> Unit): Boolean {
        if (name.isEmpty()) {
            onInvalid(R.string.empty_card_name_error)
            return false
        } else {
            return true
        }
    }

    override fun isCodeValid(code: String, onInvalid: (error: Int) -> Unit): Boolean {
        if (code.trim().isEmpty()) {
            onInvalid(R.string.empty_code_error)
            return false
        } else {
            return true
        }
    }

    override fun isFrontPhotoValid(frontPhoto: Uri?, onInvalid: (error: Int) -> Unit): Boolean {
        if (frontPhoto == null) {
            onInvalid(R.string.empty_front_photo)
            return false
        } else {
            return true
        }
    }
}