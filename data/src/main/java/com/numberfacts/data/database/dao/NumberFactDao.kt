package com.numberfacts.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.numberfacts.data.models.NumberFactModel

@Dao
interface NumberFactDao {

    @Query("SELECT * from number_fact WHERE id = :numberFactId")
    suspend fun getById(numberFactId: Int): NumberFactModel?

    @Insert
    suspend fun insertOne(numberFact: NumberFactModel)

    @Delete
    suspend fun delete(numberFact: NumberFactModel)

    @RawQuery
    suspend fun getAll(query: SupportSQLiteQuery): List<NumberFactModel>
}
