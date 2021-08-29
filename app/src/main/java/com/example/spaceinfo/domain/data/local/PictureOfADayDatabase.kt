package com.example.spaceinfo.domain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity

@Database(entities = [PictureOfADayEntity::class], version = 1, exportSchema = false)
abstract class PictureOfADayDatabase : RoomDatabase() {
    abstract fun pictureOfADayDao(): PictureOfADayDao
}