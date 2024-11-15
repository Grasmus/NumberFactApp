package com.numberfacts.domain.usecases.numberfact

import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection
import com.numberfacts.domain.entities.numberfact.NumberFact
import com.numberfacts.domain.repositories.NumberFactRepository
import com.numberfacts.domain.usecases.BaseUseCase
import javax.inject.Inject

class GetSavedNumberFactsUseCase @Inject constructor(
    private val numberFactRepository: NumberFactRepository
): BaseUseCase<List<NumberFact>, GetSavedNumberFactsUseCase.Params>() {

    override suspend fun execute(params: Params?) =
        numberFactRepository.getAllSavedNumberFacts(
            params?.orderBy ?: OrderBy.Number,
            params?.orderDirection ?: OrderDirection.Descending,
            params?.from,
            params?.till
        )

    data class Params(
        val orderBy: OrderBy,
        val orderDirection: OrderDirection,
        val from: Int? = null,
        val till: Int? = null
    )
}
