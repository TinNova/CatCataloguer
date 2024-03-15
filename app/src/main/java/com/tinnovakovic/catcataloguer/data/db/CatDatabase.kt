package com.tinnovakovic.catcataloguer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity

@Database(entities = arrayOf(CatEntity::class), version = 1, exportSchema = false)
abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

}
