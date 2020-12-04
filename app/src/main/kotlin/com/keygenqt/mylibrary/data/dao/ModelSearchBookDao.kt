package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelSearchBook
import com.keygenqt.mylibrary.data.relations.RelationSearchBook

@Dao
interface ModelSearchBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelSearchBook)

    @Query("DELETE FROM ModelSearchBook WHERE path = :path")
    fun deleteByPath(path: String)

    @Transaction
    @Query("SELECT * FROM ModelSearchBook WHERE path = :path ORDER BY modelId DESC")
    fun findSearchModels(path: String): List<RelationSearchBook>

    @Query("SELECT DISTINCT path FROM ModelSearchBook")
    fun findLinks(): List<String>

    @Query("DELETE FROM ModelSearchBook WHERE selfLink=:selfLink")
    fun deleteByLink(selfLink: String)
}