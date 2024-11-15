package com.numberfacts.data.extensions.entities

import com.numberfacts.data.models.NumberFactModel
import com.numberfacts.domain.entities.numberfact.NumberFact

fun NumberFact.toModel() =
    NumberFactModel(
        id = id ?: 0,
        number = number,
        fact = fact,
        dateTime = dateTime
    )
