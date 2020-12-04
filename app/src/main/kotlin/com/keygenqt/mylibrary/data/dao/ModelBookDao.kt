/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.data.dao

import androidx.room.*
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.relations.RelationBook
import com.keygenqt.mylibrary.data.relations.RelationSearchBook

@Dao
interface ModelBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ModelBook)

    @Transaction
    @Query("SELECT * FROM ModelBook WHERE selfLink=:selfLink")
    fun findModelByLink(selfLink: String): RelationBook?

    @Query("DELETE FROM ModelBook WHERE id = :id")
    fun deleteById(id: Long)
}

