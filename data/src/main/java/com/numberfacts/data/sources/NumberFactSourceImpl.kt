package com.numberfacts.data.sources

import com.numberfacts.data.api.ApiService
import com.numberfacts.data.database.dao.NumberFactDao
import com.numberfacts.data.extensions.entities.toModel
import com.numberfacts.data.extensions.string.toNumberFact
import com.numberfacts.data.utils.QueryBuilder
import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection
import com.numberfacts.domain.entities.numberfact.NumberFact
import java.util.Date
import javax.inject.Inject

interface NumberFactSource {
    suspend fun getNumberFact(number: Int): NumberFact
    suspend fun getRandomNumberFact(): NumberFact
    suspend fun saveNumberFact(numberFact: NumberFact)
    suspend fun deleteNumberFactById(numberFactId: Int)
    suspend fun getAllSavedNumberFacts(
        orderBy: OrderBy,
        orderDirection: OrderDirection,
        from: Int?,
        till: Int?
    ): List<NumberFact>
}

class NumberFactSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val numberFactDao: NumberFactDao
): NumberFactSource {

    override suspend fun getNumberFact(number: Int) =
        apiService.getNumberFact(number).toNumberFact(Date())

    override suspend fun getRandomNumberFact() =
        apiService.getRandomNumberFact().toNumberFact(Date())

    override suspend fun saveNumberFact(numberFact: NumberFact) =
        numberFactDao.insertOne(numberFact.toModel())

    override suspend fun deleteNumberFactById(numberFactId: Int) {
        val numberFactModel = numberFactDao.getById(numberFactId)

        numberFactModel?.let { numberFact ->
            numberFactDao.delete(numberFact)
        } ?: throw Exception("NumberFactSource::deleteNumberFact, Invalid id")
    }

    override suspend fun getAllSavedNumberFacts(
        orderBy: OrderBy,
        orderDirection: OrderDirection,
        from: Int?,
        till: Int?
    ): List<NumberFact> {
        val query = QueryBuilder("SELECT * FROM number_fact")
            .applyFilter(from, till)
            .orderBy(orderBy, orderDirection)
            .build()

        return numberFactDao.getAll(query).map { numberFactModel ->
            numberFactModel.mapToDomain()
        }
    }
}
