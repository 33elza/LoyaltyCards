package com.example.el.loyaltycards.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Nullable
import java.sql.Types

@Entity(tableName = "card")
data class Card(

        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "color")
        var color: Int,

        @ColumnInfo(name = "code")
        var code: String,

        @ColumnInfo(name = "front_photo", typeAffinity = Types.VARCHAR)
        @Nullable
        var frontPhoto: Uri?,

        @ColumnInfo(name = "back_photo", typeAffinity = Types.VARCHAR)
        @Nullable
        var backPhoto: Uri?,

        @ColumnInfo(name = "note")
        @Nullable
        var note: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readParcelable(Uri::class.java.classLoader),
            parcel.readParcelable(Uri::class.java.classLoader),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(color)
        parcel.writeString(code)
        parcel.writeParcelable(frontPhoto, flags)
        parcel.writeParcelable(backPhoto, flags)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}





