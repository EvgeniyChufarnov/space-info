package com.example.spaceinfo.domain.repository.impls

import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.safeApiCall
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import com.example.spaceinfo.domain.data.local.PictureOfADayDao
import com.example.spaceinfo.domain.data.remote.SpaceApi
import com.example.spaceinfo.domain.repository.SpaceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val spaceApi: SpaceApi,
    private val pictureOfADayDao: PictureOfADayDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SpaceRepository {

    override suspend fun getPictureOfADay(date: String): ResultWrapper<PictureOfADayEntity> {
        val localCacheResult = withContext(dispatcher) {
            pictureOfADayDao.getPicture(date)
        }

        return if (localCacheResult != null) {
            ResultWrapper.Success(localCacheResult)
        } else {
            val remoteResult = safeApiCall(dispatcher) {
                spaceApi.getPictureOfADay(date)
            }

            if (remoteResult is ResultWrapper.Success) {
                withContext(dispatcher) {
                    pictureOfADayDao.savePicture(remoteResult.value)
                }
            }

            remoteResult
        }
    }
}