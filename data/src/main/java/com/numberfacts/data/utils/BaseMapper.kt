package com.numberfacts.data.utils

interface BaseMapper<OUTPUT> {
    fun mapToDomain(): OUTPUT
}
