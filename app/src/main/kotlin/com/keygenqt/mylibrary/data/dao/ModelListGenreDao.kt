package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelListGenre

@Dao
interface ModelListGenreDao {

    @Query("SELECT * FROM ModelListGenre ORDER BY id DESC")
    fun findModels(): List<ModelListGenre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelListGenre)

    @Query("DELETE FROM ModelListGenre")
    fun deleteAll()

    @Query("DELETE FROM ModelListGenre WHERE id = :id")
    fun deleteById(id: Long)
}