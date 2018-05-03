package com.example.el.loyaltycards.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.el.loyaltycards.entity.Card

@Database(entities = [(Card::class)], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun cardsDao(): CardDao
}