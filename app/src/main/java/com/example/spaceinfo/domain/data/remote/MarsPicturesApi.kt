package com.example.spaceinfo.domain.data.remote

import com.example.spaceinfo.BuildConfig
import com.example.spaceinfo.domain.data.entities.MarsMissionEntity
import com.example.spaceinfo.domain.data.entities.MarsPicturesForLastSolEntity
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarsPicturesApi {
    @GET("mars-photos/api/v1/manifests/{rover_name}")
    suspend fun getMissionManifest(
        @Path("rover_name") roverName: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): MarsMissionEntity

    @GET("mars-photos/api/v1/rovers/{rover_name}/photos")
    suspend fun getPictures(
        @Path("rover_name") roverName: String,
        @Query("sol") sol: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): MarsPicturesForLastSolEntity
}