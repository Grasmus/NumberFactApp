package com.numberfacts.data.di

import android.content.Context
import androidx.room.Room
import com.numberfacts.data.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "number_facts-database"
        ).build()

    @Provides
    fun provideNumberFactDao(appDatabase: AppDatabase) =
        appDatabase.numberFactDao()
}
