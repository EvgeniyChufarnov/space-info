package com.example.spaceinfo.domain.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

private const val IMAGE_TYPE = "image"
private const val VIDEO_TYPE = "video"

@Entity(tableName = "pictures")
data class PictureOfADayEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "explanation")
    @SerializedName("explanation")
    val explanation: String,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    val mediaPath: String,

    @ColumnInfo(name = "hdurl")
    @SerializedName("hdurl")
    val mediaPathHD: String?,

    @ColumnInfo(name = "media_type")
    @SerializedName("media_type")
    val type: String
) {
    fun isImage() = type == IMAGE_TYPE
    fun isVideo() = type == VIDEO_TYPE
}