package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelSearch

@Dao
interface ModelSearchDao {

    @Query("SELECT * FROM ModelSearch WHERE id=:id")
    fun findModels(id: String): ModelSearch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelSearch)

    @Query("DELETE FROM ModelSearch")
    fun deleteAll()
}