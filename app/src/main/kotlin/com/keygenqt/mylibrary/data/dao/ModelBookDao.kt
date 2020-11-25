package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelBook

@Dao
interface ModelBookDao {

    @Query("SELECT * FROM ModelBook LIMIT :limit")
    fun getAll(limit: Int = 10000): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE id=:id")
    fun getModel(id: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelBook)

    @Query("DELETE FROM ModelBook")
    suspend fun deleteAll()
}