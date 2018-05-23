package com.example.el.loyaltycards.db

import android.arch.persistence.room.TypeConverter
import android.net.Uri

class UriConverter {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }

    @TypeConverter
    fun uriToString(uri: Uri?): String? {
        return if (uri == null) null else uri.toString()
    }
}