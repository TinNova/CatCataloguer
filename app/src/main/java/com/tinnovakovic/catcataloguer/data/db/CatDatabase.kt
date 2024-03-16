package com.tinnovakovic.catcataloguer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatImageEntity

@Database(entities = arrayOf(CatEntity::class, CatImageEntity::class), version = 1, exportSchema = false)
abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

}
