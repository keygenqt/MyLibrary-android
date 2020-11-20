package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelUser

@Dao
interface ModelUserDao {

    @Query("SELECT * FROM ModelUser LIMIT 1")
    fun getModel(): ModelUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: ModelUser)

    @Query("DELETE FROM ModelUser")
    suspend fun deleteAll()
}