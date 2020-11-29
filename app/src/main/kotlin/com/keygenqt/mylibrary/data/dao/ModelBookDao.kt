package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelBook

@Dao
interface ModelBookDao {

    @Query("SELECT * FROM ModelBook  WHERE type = :type LIMIT :limit")
    fun getAll(type: String, limit: Int = 10000): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE id=:id")
    fun getAllById(id: String): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink AND type = :type")
    fun getModel(selfLink: String, type: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: List<ModelBook>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelBook)

    @Update
    fun update(model: ModelBook)

    @Query("DELETE FROM ModelBook WHERE id = :id")
    suspend fun deleteAllById(id: String)

    @Query("DELETE FROM ModelBook WHERE type = :type")
    suspend fun deleteAll(type: String)
}