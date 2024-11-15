package com.numberfacts.data.repositories

import com.numberfacts.data.utils.ExceptionHandler
import javax.inject.Inject

abstract class BaseRepository {

    @Inject
    lateinit var exceptionHandler: ExceptionHandler

    protected suspend fun <RESULT> safeExecuteSuspend(call: suspend () -> RESULT) =
        try {
            call()
        } catch (throwable: Throwable) {
            throw exceptionHandler.getError(throwable)
        }
}
