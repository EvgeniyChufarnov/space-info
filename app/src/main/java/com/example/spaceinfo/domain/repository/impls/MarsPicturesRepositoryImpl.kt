package com.example.spaceinfo.domain.repository.impls

import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.entities.MarsPicturesForLastSolEntity
import com.example.spaceinfo.domain.data.remote.MarsPicturesApi
import com.example.spaceinfo.domain.data.safeApiCall
import com.example.spaceinfo.domain.repository.MarsPicturesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MarsPicturesRepositoryImpl @Inject constructor(
    private val marsPicturesApi: MarsPicturesApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MarsPicturesRepository {

    override suspend fun getPictures(roverName: String): ResultWrapper<MarsPicturesForLastSolEntity> {
        val missionManifest = safeApiCall(dispatcher) {
            marsPicturesApi.getMissionManifest(roverName)
        }

        return if (missionManifest is ResultWrapper.Success) {
            safeApiCall(dispatcher) {
                marsPicturesApi.getPictures(roverName, missionManifest.value.photoManifest.lastSol.toString())
            }
        } else {
            ResultWrapper.NetworkError
        }
    }
}