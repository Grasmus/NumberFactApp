package com.numberfacts.data.extensions.string

import com.numberfacts.domain.entities.numberfact.NumberFact
import java.util.Date

fun String.toNumberFact(dateTime: Date): NumberFact {
    var numberString = ""

    for (char in this) {
        if (char.isDigit() || char == '-') {
            numberString += char
        } else {
            break
        }
    }

    return NumberFact(null, numberString.toIntOrNull(), this, dateTime)
}
