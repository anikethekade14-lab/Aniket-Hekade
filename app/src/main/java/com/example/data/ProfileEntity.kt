package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class ProfileEntity(
  @PrimaryKey val id: Int = 1,
  val displayName: String = "Aniket",
  val email: String = "anikethekade14@icloud.com",
  val avatarUri: String? = null,
  val icloudStorageUsedGb: Float = 16.4f,
  val icloudStorageTotalGb: Float = 50.0f,
  val accountTier: String = "iCloud+ 50 GB",
  val appleIdDescription: String = "Apple Account, iCloud, and Media"
)
