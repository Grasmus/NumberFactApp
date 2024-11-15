package com.numberfacts.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.numberfacts.data.utils.BaseMapper
import com.numberfacts.domain.entities.numberfact.NumberFact
import java.util.Date

@Entity("number_fact")
data class NumberFactModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("number") val number: Int?,
    @ColumnInfo("fact") val fact: String?,
    @ColumnInfo("date_time") val dateTime: Date?
): BaseMapper<NumberFact> {

    override fun mapToDomain() =
        NumberFact(
            id = id,
            number = number,
            fact = fact,
            dateTime = dateTime
        )
}
