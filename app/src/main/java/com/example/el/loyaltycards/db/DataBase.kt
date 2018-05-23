package com.example.el.loyaltycards.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.el.loyaltycards.entity.Card

@Database(entities = [(Card::class)], version = 1)
@TypeConverters(UriConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun cardsDao(): CardDao
}