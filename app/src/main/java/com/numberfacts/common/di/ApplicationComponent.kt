package com.numberfacts.common.di

import com.numberfacts.data.di.DataAppComponent
import com.numberfacts.feature.details.DetailsFragment
import com.numberfacts.feature.main.MainFragment
import dagger.Component
import javax.inject.Scope

@Scope
@Retention
annotation class ApplicationScope

@ApplicationScope
@Component(
    dependencies = [
        DataAppComponent::class
    ]
)
interface ApplicationComponent {

    fun inject(mainFragment: MainFragment)
    fun inject(detailsFragment: DetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(dataAppComponent: DataAppComponent): ApplicationComponent
    }
}
