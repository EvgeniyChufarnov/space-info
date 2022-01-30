package com.example.spaceinfo.di

import android.app.Application
import com.example.spaceinfo.domain.repository.MarsPicturesRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SpaceInfoApplication: Application() {

    @Inject
    lateinit var marsPicturesRepository: MarsPicturesRepository
}