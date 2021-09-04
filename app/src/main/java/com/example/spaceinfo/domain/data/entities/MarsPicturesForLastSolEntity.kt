package com.example.spaceinfo.domain.data.entities

import com.google.gson.annotations.SerializedName

data class MarsPicturesForLastSolEntity (
    @SerializedName("photos")
    val photos : List<Photos>
)

data class Photos (
    @SerializedName("img_src")
    val imagePath : String,
)