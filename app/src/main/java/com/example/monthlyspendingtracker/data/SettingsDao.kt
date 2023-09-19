package com.example.monthlyspendingtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update

@Dao
interface SettingsDao {
    @Insert
    fun insertSettings(settings: SettingsEntity)

    @Update
    fun updateSettings(settings: SettingsEntity)

//    @Query("SELECT COUNT(category) FROM settings")
//    fun getSettingsCount(): Int
//
//    @Query("SELECT * FROM settings")
//    fun getSettings(): List<SettingsEntity>
}