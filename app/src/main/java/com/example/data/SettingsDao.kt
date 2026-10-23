package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getProfile(): Flow<ProfileEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProfile(profile: ProfileEntity)

  @Update
  suspend fun updateProfile(profile: ProfileEntity)

  @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
  fun getAppSettings(): Flow<AppSettingsEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAppSettings(settings: AppSettingsEntity)

  @Update
  suspend fun updateAppSettings(settings: AppSettingsEntity)
}
