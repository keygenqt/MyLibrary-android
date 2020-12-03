package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.relationships.RelationSearchBook

@Dao
interface ModelBookDao {

    @Query("SELECT * FROM ModelBook WHERE id NOT IN (:ids)")
    fun findByLink(ids: List<String>): List<ModelBook>

    @Query("DELETE FROM ModelBook")
    suspend fun deleteByLink()

    //    @Query("SELECT * FROM ModelBook  WHERE type = :type AND enabled=1 LIMIT :limit")
    //    fun getAll(type: String, limit: Int = 10000): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE id=:id")
    fun getAllById(id: String): List<ModelBook>

    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun getAllByLink(selfLink: String): List<ModelBook>

    //    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink AND type = :type")
    //    fun getModel(selfLink: String, type: String): ModelBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ModelBook)

    @Update
    fun update(model: ModelBook)

    @Query("DELETE FROM ModelBook WHERE id = :id")
    suspend fun deleteAllById(id: String)

}