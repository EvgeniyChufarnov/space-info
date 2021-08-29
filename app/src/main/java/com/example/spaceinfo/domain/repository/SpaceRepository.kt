package com.example.spaceinfo.domain.repository

import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity

interface SpaceRepository {
    suspend fun getPictureOfADay(date: String): ResultWrapper<PictureOfADayEntity>
}