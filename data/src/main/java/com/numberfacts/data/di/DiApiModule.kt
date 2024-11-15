package com.numberfacts.data.di

import com.numberfacts.data.BuildConfig
import com.numberfacts.data.api.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

@Module
class ApiModule {
    @Provides
    fun provideScalarsConverterFactory(): ScalarsConverterFactory {
        return ScalarsConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(scalarsConverterFactory: ScalarsConverterFactory): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(scalarsConverterFactory)
            .baseUrl(BuildConfig.API_URL)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create<ApiService>()
}
