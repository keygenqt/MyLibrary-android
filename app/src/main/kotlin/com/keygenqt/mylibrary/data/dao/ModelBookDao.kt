package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelBook

@Dao
interface ModelBookDao {
    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findAllByLink(selfLink: String): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findModel(selfLink: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ModelBook)

    @Update
    fun update(model: ModelBook)

    @Query("DELETE FROM ModelBook WHERE selfLink=:selfLink")
    suspend fun deleteByLink(selfLink: String)
}

