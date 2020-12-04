package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelBook

@Dao
interface ModelBookDao {
    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findAllByLink(selfLink: String): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findModelByLink(selfLink: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelBook)

    @Update
    fun update(model: ModelBook)

    @Query("DELETE FROM ModelBook WHERE selfLink=:selfLink")
    fun deleteByLink(selfLink: String)
}

