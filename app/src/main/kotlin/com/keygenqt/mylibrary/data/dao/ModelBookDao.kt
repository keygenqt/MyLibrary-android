package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.data.relations.RelationSearchBook

@Dao
interface ModelBookDao {
    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findAllByLink(selfLink: String): List<ModelBook>

//    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
//    fun findModelByLink(selfLink: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelBook)

    @Update
    fun update(model: ModelBook)

    @Query("DELETE FROM ModelBook WHERE selfLink=:selfLink")
    fun deleteByLink(selfLink: String)

    @Transaction
    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findModelByLink(selfLink: String): RelationBook?

    @Query("DELETE FROM ModelBook WHERE id = :id")
    fun deleteById(id: Long)
}

