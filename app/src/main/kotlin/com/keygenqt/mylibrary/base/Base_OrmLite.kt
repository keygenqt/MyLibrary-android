package com.keygenqt.mylibrary.base

import android.content.*
import android.database.sqlite.*
import com.j256.ormlite.support.*
import com.keygenqt.db.*

/**
 * BaseOrmLite class for initialization and migration in OrmLite
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
class BaseOrmLite(context: Context, dbName: String, dbVersion: Int) :
    OrmliteBase(context, dbName, dbVersion) {

    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }
}