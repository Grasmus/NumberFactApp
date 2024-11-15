package com.numberfacts.data.utils

import com.numberfacts.data.extensions.exceptions.toApiException
import com.numberfacts.domain.entities.exceptions.ConnectionErrorException
import com.numberfacts.domain.entities.exceptions.UnexpectedErrorException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExceptionHandlerImpl @Inject constructor(): ExceptionHandler {
    override fun getError(throwable: Throwable) =
        when (throwable) {
            is IOException -> ConnectionErrorException()
            is HttpException -> throwable.toApiException()
            else -> UnexpectedErrorException(throwable)
        }
}

interface ExceptionHandler {
    fun getError(throwable: Throwable): Exception
}
