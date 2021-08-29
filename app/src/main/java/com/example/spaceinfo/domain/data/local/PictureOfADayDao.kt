package com.example.spaceinfo.domain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity

@Dao
interface PictureOfADayDao {
    @Insert(onConflict = REPLACE)
    fun savePicture(pictureOfADay: PictureOfADayEntity)

    @Query("SELECT * FROM pictures WHERE date = :date")
    fun getPicture(date: String): PictureOfADayEntity?
}