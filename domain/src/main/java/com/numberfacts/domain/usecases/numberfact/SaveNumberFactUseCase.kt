package com.numberfacts.domain.usecases.numberfact

import com.numberfacts.domain.entities.numberfact.NumberFact
import com.numberfacts.domain.repositories.NumberFactRepository
import com.numberfacts.domain.usecases.BaseUseCase
import javax.inject.Inject

class SaveNumberFactUseCase @Inject constructor(
    private val numberFactRepository: NumberFactRepository
): BaseUseCase<Unit, SaveNumberFactUseCase.Params>() {

    override suspend fun execute(params: Params?) =
        numberFactRepository.saveNumberFact(params?.numberFact)

    data class Params(val numberFact: NumberFact)
}
