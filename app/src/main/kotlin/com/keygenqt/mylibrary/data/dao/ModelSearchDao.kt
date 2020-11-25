package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelSearch

@Dao
interface ModelSearchDao {

    @Query("SELECT * FROM ModelSearch")
    fun getAll(): List<ModelSearch>

    @Query("SELECT * FROM ModelSearch WHERE id=:id")
    fun getModel(id: String): ModelSearch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ModelSearch)

    @Query("DELETE FROM ModelSearch")
    suspend fun deleteAll()
}