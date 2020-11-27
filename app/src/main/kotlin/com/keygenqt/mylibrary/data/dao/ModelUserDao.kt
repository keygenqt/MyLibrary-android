package com.keygenqt.mylibrary.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keygenqt.mylibrary.data.models.ModelUser

@Dao
interface ModelUserDao {

    @Query("SELECT * FROM ModelUser WHERE id =:id")
    fun getModel(id: String): ModelUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ModelUser)

    @Query("DELETE FROM ModelUser")
    suspend fun deleteAll()
}