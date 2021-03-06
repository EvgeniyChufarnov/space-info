package com.example.spaceinfo.di

import com.example.spaceinfo.domain.repository.SpaceRepository
import com.example.spaceinfo.domain.repository.impls.SpaceRepositoryImpl
import com.example.spaceinfo.domain.data.local.PictureOfADayDao
import com.example.spaceinfo.domain.data.remote.SpaceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoriesModule {
    @Provides
    fun providesBaseUrl(): String = "https://api.nasa.gov/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideSpaceService(retrofit: Retrofit): SpaceApi =
        retrofit.create(SpaceApi::class.java)

    @Provides
    @Singleton
    fun provideSpaceRepository(
        spaceApi: SpaceApi,
        pictureOfADayDao: PictureOfADayDao
    ): SpaceRepository =
        SpaceRepositoryImpl(spaceApi, pictureOfADayDao)
}