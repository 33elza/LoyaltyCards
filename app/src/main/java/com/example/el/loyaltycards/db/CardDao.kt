package com.example.el.loyaltycards.db

import android.arch.persistence.room.*
import com.example.el.loyaltycards.entity.Card


@Dao
interface CardDao {

    @Insert
    fun insert(card: Card)

    @Query("SELECT * FROM card")
    fun cards(): List<Card>

    @Update
    fun update(card: Card)

    @Delete
    fun delete(card: Card)
}