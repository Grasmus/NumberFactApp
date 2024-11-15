package com.numberfacts.domain.usecases.numberfact

import com.numberfacts.domain.entities.numberfact.NumberFact
import com.numberfacts.domain.repositories.NumberFactRepository
import com.numberfacts.domain.usecases.BaseUseCase
import javax.inject.Inject

class GetNumberFactUseCase @Inject constructor(
    private val numberFactRepository: NumberFactRepository
): BaseUseCase<NumberFact, GetNumberFactUseCase.Prams>() {

    override suspend fun execute(params: Prams?) =
        numberFactRepository.getNumberFact(params?.number)

    data class Prams(val number: Int)
}
