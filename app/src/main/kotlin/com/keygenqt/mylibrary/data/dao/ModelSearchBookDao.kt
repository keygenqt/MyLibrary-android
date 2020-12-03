package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelSearchBook
import com.keygenqt.mylibrary.data.relations.RelationSearchBook

@Dao
interface ModelSearchBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ModelSearchBook)

    @Query("DELETE FROM ModelSearchBook WHERE path = :path")
    suspend fun deleteByPath(path: String)

    @Transaction
    @Query("SELECT * FROM ModelSearchBook WHERE path = :path AND modelId NOT IN (:ids) ORDER BY modelId DESC")
    fun findSearchModels(path: String, ids: List<Long>): List<RelationSearchBook>

    @Query("DELETE FROM ModelSearchBook WHERE selfLink=:selfLink")
    suspend fun deleteByLink(selfLink: String)
}