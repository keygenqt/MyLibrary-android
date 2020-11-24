package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelRoot

@Dao
interface ModelRootDao {
    @Query("SELECT * FROM ModelRoot ORDER BY version ASC")
    fun getAll(): List<ModelRoot>

    @Query("SELECT * FROM ModelRoot WHERE version=:version")
    fun getModel(version: String): ModelRoot

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ModelRoot)

    @Query("DELETE FROM ModelRoot")
    suspend fun deleteAll()
}