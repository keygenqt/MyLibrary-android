package com.keygenqt.mylibrary.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import java.lang.RuntimeException
import kotlin.reflect.full.declaredFunctions

class RoomDatabase(val context: Context) {
    val db: RoomConf = Room.databaseBuilder(
        context,
        RoomConf::class.java,
        "RoomDatabase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    inline fun <reified T> getDao(): T {
        val className = T::class.qualifiedName!!
        T::class.java.annotations.firstOrNull { it !is Dao }?.let {
            db::class.declaredFunctions.forEach { method ->
                if (className == method.returnType.toString()) {
                    return method.call(db) as T
                }
            }
        }
        throw RuntimeException("Not found dao!")
    }
}