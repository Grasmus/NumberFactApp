package com.numberfacts.data.repositories

import com.numberfacts.data.sources.NumberFactSource
import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection
import com.numberfacts.domain.entities.numberfact.NumberFact
import com.numberfacts.domain.repositories.NumberFactRepository
import javax.inject.Inject

class NumberFactRepositoryImpl @Inject constructor(
    private val numberFactSource: NumberFactSource
): BaseRepository(), NumberFactRepository {

    override suspend fun getNumberFact(number: Int?) =
        safeExecuteSuspend {
            number?.let { num ->
                numberFactSource.getNumberFact(num)
            } ?: throw Exception("NumberFactRepository: number was null!")
        }

    override suspend fun getRandomNumberFact() =
        safeExecuteSuspend {
            numberFactSource.getRandomNumberFact()
        }

    override suspend fun saveNumberFact(numberFact: NumberFact?) =
        safeExecuteSuspend {
            numberFact?.let { num ->
                numberFactSource.saveNumberFact(num)
            } ?: throw Exception("NumberFactRepository: number fact was null!")
        }

    override suspend fun deleteNumberFactById(numberFactId: Int?) =
        safeExecuteSuspend {
            numberFactId?.let { id ->
                numberFactSource.deleteNumberFactById(id)
            } ?: throw Exception("NumberFactRepository: number fact id was null!")
        }

    override suspend fun getAllSavedNumberFacts(
        orderBy: OrderBy,
        orderDirection: OrderDirection,
        from: Int?,
        till: Int?
    ) = safeExecuteSuspend {
        numberFactSource.getAllSavedNumberFacts(orderBy, orderDirection, from, till)
    }
}
