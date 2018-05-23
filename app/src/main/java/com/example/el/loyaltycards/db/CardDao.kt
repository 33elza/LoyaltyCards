package com.example.el.loyaltycards.db

import android.arch.persistence.room.*
import com.example.el.loyaltycards.entity.Card


@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: Card)

    @Query("SELECT * FROM card")
    fun cards(): List<Card>

    @Delete
    fun delete(card: Card)

    @Query("SELECT * FROM card ORDER BY name ASC")
    fun cardsSortedByName(): List<Card>
}