package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelBook

@Dao
interface ModelBookDao {

    @Query("SELECT * FROM ModelBook  WHERE type = :type ORDER BY LENGTH(id) LIMIT :limit")
    fun getAll(type: String, limit: Int = 10000): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE id=:id")
    fun getModel(id: String): ModelBook?

    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink AND type = :type")
    fun getModel(selfLink: String, type: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: List<ModelBook>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelBook)

    @Query("DELETE FROM ModelBook WHERE type = :type")
    suspend fun deleteAll(type: String)
}