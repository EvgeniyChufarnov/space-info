package com.example.spaceinfo.domain.data.remote

import com.example.spaceinfo.BuildConfig
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceApi {
    @GET("planetary/apod")
    suspend fun getPictureOfADay(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): PictureOfADayEntity
}