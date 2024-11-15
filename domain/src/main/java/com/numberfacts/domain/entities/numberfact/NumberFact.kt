package com.numberfacts.domain.entities.numberfact

import java.util.Date

data class NumberFact(
    val id: Int?,
    val number: Int?,
    val fact: String?,
    val dateTime: Date?
)
