package com.numberfacts.data.extensions.exceptions

import com.numberfacts.domain.entities.exceptions.ApiErrorException
import retrofit2.HttpException

fun HttpException.toApiException() = ApiErrorException(this.code())
