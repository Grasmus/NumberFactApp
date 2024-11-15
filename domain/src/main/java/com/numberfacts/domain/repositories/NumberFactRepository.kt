package com.numberfacts.domain.repositories

import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection
import com.numberfacts.domain.entities.numberfact.NumberFact

interface NumberFactRepository {
    suspend fun getNumberFact(number: Int?): NumberFact
    suspend fun getRandomNumberFact(): NumberFact
    suspend fun saveNumberFact(numberFact: NumberFact?)
    suspend fun deleteNumberFactById(numberFactId: Int?)
    suspend fun getAllSavedNumberFacts(
        orderBy: OrderBy,
        orderDirection: OrderDirection,
        from: Int?,
        till: Int?
    ): List<NumberFact>
}
