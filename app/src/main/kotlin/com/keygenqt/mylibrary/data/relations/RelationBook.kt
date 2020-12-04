package com.keygenqt.mylibrary.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.keygenqt.mylibrary.data.models.ModelBook
import com.keygenqt.mylibrary.data.models.ModelBookGenre
import com.keygenqt.mylibrary.data.models.ModelBookUser
import com.keygenqt.mylibrary.data.models.ModelSearchBook

data class RelationBook(
    @Embedded var model: ModelBook,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    var user: ModelBookUser? = null,

    @Relation(
        parentColumn = "genreId",
        entityColumn = "id"
    )
    var genre: ModelBookGenre? = null,
)