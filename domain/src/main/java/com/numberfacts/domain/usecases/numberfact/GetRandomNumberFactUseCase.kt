package com.numberfacts.domain.usecases.numberfact

import com.numberfacts.domain.entities.numberfact.NumberFact
import com.numberfacts.domain.repositories.NumberFactRepository
import com.numberfacts.domain.usecases.BaseUseCase
import javax.inject.Inject

class GetRandomNumberFactUseCase @Inject constructor(
    private val numberFactRepository: NumberFactRepository
): BaseUseCase<NumberFact, Unit>() {

    override suspend fun execute(params: Unit?) =
        numberFactRepository.getRandomNumberFact()
}
