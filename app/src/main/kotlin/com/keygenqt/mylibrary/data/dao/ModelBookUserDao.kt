package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelBookUser

@Dao
interface ModelBookUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelBookUser)

    @Query("DELETE FROM ModelBookUser WHERE id = :id")
    fun deleteById(id: Long)
}