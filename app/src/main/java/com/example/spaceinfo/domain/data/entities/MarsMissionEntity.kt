package com.example.spaceinfo.domain.data.entities

import com.google.gson.annotations.SerializedName

data class MarsMissionEntity(
    @SerializedName("photo_manifest")
    val photoManifest: PhotoManifest
)

data class PhotoManifest (
    @SerializedName("max_sol")
    val lastSol: Int
)