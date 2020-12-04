package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelBookGenre

@Dao
interface ModelBookGenreDao {

    @Query("SELECT * FROM ModelBookGenre WHERE id NOT IN (:ids) ORDER BY id DESC")
    fun findModels(ids: List<Long>): List<ModelBookGenre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelBookGenre)

    @Query("DELETE FROM ModelBookGenre")
    fun deleteAll()
}