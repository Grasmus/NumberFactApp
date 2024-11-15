package com.numberfacts.domain.entities.exceptions

data class ApiErrorException(
    val code: Int?
): Exception()
