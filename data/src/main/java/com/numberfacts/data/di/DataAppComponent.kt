package com.numberfacts.data.di

import android.content.Context
import com.numberfacts.domain.repositories.NumberFactRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
@Retention
annotation class DataScope

@DataScope
@Component(
    modules = [
        ApiModule::class,
        RepositoriesModule::class,
        SourcesModule::class,
        UtilsModule::class,
        DatabaseModule::class
    ]
)
interface DataAppComponent {
    fun getNumberFactRepository(): NumberFactRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataAppComponent
    }
}
