package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
  @PrimaryKey val id: Int = 1,
  val isDarkMode: Boolean = true,
  val accentStyle: String = "dynamic_multicolor", // "blue", "dynamic_multicolor", "titanium"
  val biometricAppLockEnabled: Boolean = false,
  val hasPromptedInitialBiometric: Boolean = false,
  val airplaneMode: Boolean = false,
  val vpnActive: Boolean = false,
  val alwaysOnDisplayEnabled: Boolean = true,
  val nightShiftEnabled: Boolean = true,
  val trueToneEnabled: Boolean = true,
  val backgroundAppRefresh: Boolean = true,
  val hapticsEnabled: Boolean = true,
  val lockPasscode: String = "2626"
)
