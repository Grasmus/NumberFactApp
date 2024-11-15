package com.numberfacts.data.di

import com.numberfacts.data.repositories.NumberFactRepositoryImpl
import com.numberfacts.data.sources.NumberFactSource
import com.numberfacts.data.sources.NumberFactSourceImpl
import com.numberfacts.data.utils.ExceptionHandler
import com.numberfacts.data.utils.ExceptionHandlerImpl
import com.numberfacts.domain.repositories.NumberFactRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SourcesModule {
    @Binds
    abstract fun bindNumberFactSource(
        numberFactSourceImpl: NumberFactSourceImpl
    ): NumberFactSource
}

@Module
abstract class RepositoriesModule {
    @Binds
    abstract fun bindNumberFactRepository(
        numberFactRepositoryImpl: NumberFactRepositoryImpl
    ): NumberFactRepository
}

@Module
abstract class UtilsModule {
    @Binds
    abstract fun bindExceptionHandler(
        exceptionHandlerImpl: ExceptionHandlerImpl
    ): ExceptionHandler
}
