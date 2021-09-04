package com.example.spaceinfo.domain.repository

import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.entities.MarsPicturesForLastSolEntity

interface MarsPicturesRepository {
    suspend fun getPictures(roverName: String): ResultWrapper<MarsPicturesForLastSolEntity>
}