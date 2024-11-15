package com.numberfacts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.numberfacts.data.database.converters.Converters
import com.numberfacts.data.database.dao.NumberFactDao
import com.numberfacts.data.models.NumberFactModel

@Database(entities = [NumberFactModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun numberFactDao(): NumberFactDao
}
