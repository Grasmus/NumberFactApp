package com.numberfacts.feature

import android.app.Application
import com.numberfacts.common.di.ApplicationComponent
import com.numberfacts.common.di.DaggerApplicationComponent
import com.numberfacts.data.di.DaggerDataAppComponent

class NumberFactsApp: Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        initDI()
    }

    private fun initDI() {
        appComponent = DaggerApplicationComponent
            .factory()
            .create(
                DaggerDataAppComponent.factory().create(this.applicationContext)
            )
    }
}
