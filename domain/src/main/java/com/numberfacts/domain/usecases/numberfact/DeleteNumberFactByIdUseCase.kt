package com.numberfacts.domain.usecases.numberfact

import com.numberfacts.domain.repositories.NumberFactRepository
import com.numberfacts.domain.usecases.BaseUseCase
import javax.inject.Inject

class DeleteNumberFactByIdUseCase @Inject constructor(
    private val numberFactRepository: NumberFactRepository
): BaseUseCase<Unit, DeleteNumberFactByIdUseCase.Params>() {

    override suspend fun execute(params: Params?) =
        numberFactRepository.deleteNumberFactById(params?.numberFactId)

    data class Params(val numberFactId: Int)
}
