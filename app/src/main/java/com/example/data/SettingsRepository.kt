package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsRepository(private val dao: SettingsDao, private val scope: CoroutineScope) {

  init {
    // Ensure default initial rows exist in database
    scope.launch(Dispatchers.IO) {
      dao.insertProfile(ProfileEntity())
      dao.insertAppSettings(AppSettingsEntity())
    }
  }

  val profileFlow: Flow<ProfileEntity> = dao.getProfile().map { it ?: ProfileEntity() }

  val appSettingsFlow: Flow<AppSettingsEntity> = dao.getAppSettings().map { it ?: AppSettingsEntity() }

  suspend fun updateProfile(
    displayName: String,
    email: String,
    avatarUri: String?
  ) {
    val current = dao.getProfile()
    dao.insertProfile(
      ProfileEntity(
        id = 1,
        displayName = displayName,
        email = email,
        avatarUri = avatarUri
      )
    )
  }

  suspend fun updateDarkMode(isDark: Boolean) {
    dao.insertAppSettings(
      AppSettingsEntity(
        id = 1,
        isDarkMode = isDark
      )
    )
  }

  suspend fun updateAccentStyle(accent: String) {
    dao.insertAppSettings(
      AppSettingsEntity(
        id = 1,
        accentStyle = accent
      )
    )
  }

  suspend fun updateBiometricSettings(
    enabled: Boolean,
    hasPrompted: Boolean = true
  ) {
    dao.insertAppSettings(
      AppSettingsEntity(
        id = 1,
        biometricAppLockEnabled = enabled,
        hasPromptedInitialBiometric = hasPrompted
      )
    )
  }

  suspend fun updateSettings(settings: AppSettingsEntity) {
    dao.insertAppSettings(settings)
  }
}
