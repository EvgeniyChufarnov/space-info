package com.example.spaceinfo.di

import android.content.Context
import androidx.room.Room
import com.example.spaceinfo.domain.data.local.PictureOfADayDao
import com.example.spaceinfo.domain.data.local.PictureOfADayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providePictureOfADayDatabase(@ApplicationContext appContext: Context): PictureOfADayDatabase {
        return Room.databaseBuilder(
            appContext,
            PictureOfADayDatabase::class.java,
            "pictures.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePictureOfADayDao(database: PictureOfADayDatabase): PictureOfADayDao {
        return database.pictureOfADayDao()
    }
}