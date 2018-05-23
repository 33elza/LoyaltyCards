package com.example.el.loyaltycards.utils

import android.net.Uri

interface ICardValidator {
    fun isNameValid(name: String, onInvalid: (error: Int) -> Unit): Boolean
    fun isCodeValid(code: String, onInvalid: (error: Int) -> Unit): Boolean
    fun isFrontPhotoValid(frontPhoto: Uri?, onInvalid: (error: Int) -> Unit): Boolean
}