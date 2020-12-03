package com.keygenqt.mylibrary.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelSearchBook

data class RelationSearchBook(
    @Embedded var model: ModelSearchBook,
    @Relation(
        parentColumn = "modelId",
        entityColumn = "id"
    )
    var search: ModelBook
)