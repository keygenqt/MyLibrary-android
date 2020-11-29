package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelBookGenre

@Dao
interface ModelBookGenreDao {

    @Query("SELECT * FROM ModelBookGenre ORDER BY LENGTH(id) LIMIT :limit")
    fun getAll(limit: Int = 10000): List<ModelBookGenre>

    @Query("SELECT * FROM ModelBookGenre WHERE id=:id")
    fun getModel(id: String): ModelBookGenre?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: List<ModelBookGenre>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelBookGenre)

    @Query("DELETE FROM ModelBookGenre")
    suspend fun deleteAll()
}