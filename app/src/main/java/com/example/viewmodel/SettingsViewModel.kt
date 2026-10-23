package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppSettingsEntity
import com.example.data.ProfileEntity
import com.example.data.SettingsRepository
import com.example.system.HardwareTelemetryManager
import com.example.ui.theme.IosBlue
import com.example.ui.theme.IosCyan
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosPurple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SettingsScreen {
  object Main : SettingsScreen()
  object ProfileDetail : SettingsScreen()
  object General : SettingsScreen()
  object AboutPhone : SettingsScreen()
  object SoftwareUpdate : SettingsScreen()
  object StorageDetail : SettingsScreen()
  object Connectivity : SettingsScreen()
  object DisplayAndBrightness : SettingsScreen()
  object SoundsAndHaptics : SettingsScreen()
  object BatteryDetail : SettingsScreen()
  object FaceIdAndPasscode : SettingsScreen()
  object PrivacyAndSecurity : SettingsScreen()
  object GoogleServices : SettingsScreen()
  object SiriDemo : SettingsScreen()
  object AppleIntelligenceDemo : SettingsScreen()
  object AirDropDemo : SettingsScreen()
  object FindMyDemo : SettingsScreen()
  object ICloudDemo : SettingsScreen()
  object AlwaysOnDisplayDemo : SettingsScreen()
  object NotificationsDetail : SettingsScreen()
  object AccessibilityDetail : SettingsScreen()
  object FocusDetail : SettingsScreen()
  object ScreenTimeDetail : SettingsScreen()
  object WallpaperDetail : SettingsScreen()
  object ControlCenterDetail : SettingsScreen()
  object AppsDetail : SettingsScreen()
}

data class SearchableSettingItem(
  val title: String,
  val section: String,
  val screen: SettingsScreen,
  val subtitle: String? = null
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

  private val database = try {
    AppDatabase.getDatabase(application)
  } catch (_: Throwable) {
    null
  }

  private val repository = database?.let {
    try {
      SettingsRepository(it.settingsDao(), viewModelScope)
    } catch (_: Throwable) {
      null
    }
  }

  val telemetryManager = HardwareTelemetryManager(application)
  val telemetry = telemetryManager.telemetry

  private val _fallbackProfile = MutableStateFlow(ProfileEntity())
  val profile: StateFlow<ProfileEntity> = repository?.profileFlow?.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = ProfileEntity()
  ) ?: _fallbackProfile.asStateFlow()

  private val _fallbackAppSettings = MutableStateFlow(AppSettingsEntity())
  val appSettings: StateFlow<AppSettingsEntity> = repository?.appSettingsFlow?.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = AppSettingsEntity()
  ) ?: _fallbackAppSettings.asStateFlow()

  private val _screenStack = MutableStateFlow<List<SettingsScreen>>(listOf(SettingsScreen.Main))
  val screenStack: StateFlow<List<SettingsScreen>> = _screenStack.asStateFlow()

  val currentScreen: SettingsScreen get() = _screenStack.value.lastOrNull() ?: SettingsScreen.Main

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _isBiometricLocked = MutableStateFlow(false)
  val isBiometricLocked: StateFlow<Boolean> = _isBiometricLocked.asStateFlow()

  private val _showFirstLaunchBiometricPrompt = MutableStateFlow(false)
  val showFirstLaunchBiometricPrompt: StateFlow<Boolean> = _showFirstLaunchBiometricPrompt.asStateFlow()

  init {
    viewModelScope.launch {
      try {
        appSettings.collect { settings ->
          if (!settings.hasPromptedInitialBiometric) {
            _showFirstLaunchBiometricPrompt.value = true
          }
          if (settings.biometricAppLockEnabled && !_isBiometricLocked.value) {
            _isBiometricLocked.value = true
          }
        }
      } catch (_: Throwable) {}
    }
  }

  fun navigateTo(screen: SettingsScreen) {
    _screenStack.value = _screenStack.value + screen
  }

  fun navigateBack(): Boolean {
    val current = _screenStack.value
    return if (current.size > 1) {
      _screenStack.value = current.dropLast(1)
      true
    } else {
      false
    }
  }

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun getAccentColor(style: String): Color {
    return when (style) {
      "dynamic_multicolor" -> IosCyan
      "purple" -> IosPurple
      "emerald" -> IosGreen
      "sunset" -> IosOrange
      else -> IosBlue
    }
  }

  fun saveProfile(displayName: String, email: String, avatarUri: String?) {
    val current = profile.value
    val updated = current.copy(displayName = displayName, email = email, avatarUri = avatarUri)
    _fallbackProfile.value = updated
    viewModelScope.launch {
      try {
        repository?.updateProfile(displayName, email, avatarUri)
      } catch (_: Throwable) {}
    }
  }

  fun toggleDarkMode(isDark: Boolean) {
    val current = appSettings.value
    val updated = current.copy(isDarkMode = isDark)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun setAccentStyle(style: String) {
    val current = appSettings.value
    val updated = current.copy(accentStyle = style)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun toggleBiometricAppLock(enabled: Boolean) {
    val current = appSettings.value
    val updated = current.copy(
      biometricAppLockEnabled = enabled,
      hasPromptedInitialBiometric = true
    )
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun dismissFirstLaunchBiometric(enableLock: Boolean) {
    _showFirstLaunchBiometricPrompt.value = false
    val current = appSettings.value
    val updated = current.copy(
      biometricAppLockEnabled = enableLock,
      hasPromptedInitialBiometric = true
    )
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun unlockBiometrics() {
    _isBiometricLocked.value = false
  }

  fun toggleAirplaneMode(enabled: Boolean) {
    val current = appSettings.value
    val updated = current.copy(airplaneMode = enabled)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun toggleVpn(enabled: Boolean) {
    val current = appSettings.value
    val updated = current.copy(vpnActive = enabled)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun toggleAlwaysOnDisplay(enabled: Boolean) {
    val current = appSettings.value
    val updated = current.copy(alwaysOnDisplayEnabled = enabled)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun toggleNightShift(enabled: Boolean) {
    val current = appSettings.value
    val updated = current.copy(nightShiftEnabled = enabled)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  fun toggleTrueTone(enabled: Boolean) {
    val current = appSettings.value
    val updated = current.copy(trueToneEnabled = enabled)
    _fallbackAppSettings.value = updated
    viewModelScope.launch {
      try {
        repository?.updateSettings(updated)
      } catch (_: Throwable) {}
    }
  }

  val allSearchableItems = listOf(
    SearchableSettingItem("Apple ID Profile", "Apple Account", SettingsScreen.ProfileDetail, "Name, phone, email, password & security"),
    SearchableSettingItem("Wi-Fi", "Connectivity", SettingsScreen.Connectivity, "Network connection and Android Wi-Fi settings"),
    SearchableSettingItem("Bluetooth", "Connectivity", SettingsScreen.Connectivity, "Pair and manage Bluetooth accessories"),
    SearchableSettingItem("Mobile Service", "Connectivity", SettingsScreen.Connectivity, "SIM cards, mobile data, 4G LTE"),
    SearchableSettingItem("Personal Hotspot", "Connectivity", SettingsScreen.Connectivity, "Tethering and portable hotspot"),
    SearchableSettingItem("Airplane Mode", "Connectivity", SettingsScreen.Main, "Disable cellular, Wi-Fi, and Bluetooth"),
    SearchableSettingItem("VPN & Device Management", "Connectivity", SettingsScreen.Connectivity, "VPN configuration"),
    SearchableSettingItem("Display & Brightness", "Display", SettingsScreen.DisplayAndBrightness, "Dark mode, text size, True Tone, Night Shift"),
    SearchableSettingItem("Always-On Display", "Display", SettingsScreen.AlwaysOnDisplayDemo, "Realme lock screen widgets and low-power clock"),
    SearchableSettingItem("Sounds & Haptics", "Audio", SettingsScreen.SoundsAndHaptics, "Ringtones, volume, vibration patterns"),
    SearchableSettingItem("Battery", "Hardware", SettingsScreen.BatteryDetail, "Battery health, percentage, Realme charging status"),
    SearchableSettingItem("General", "System", SettingsScreen.General, "About, Software Update, Storage, Reset"),
    SearchableSettingItem("About Phone", "General", SettingsScreen.AboutPhone, "Realme C71 4G, Android 16 specs, 4 GB RAM"),
    SearchableSettingItem("Software Update", "General", SettingsScreen.SoftwareUpdate, "iOS 26.1 Preview build status"),
    SearchableSettingItem("Storage", "General", SettingsScreen.StorageDetail, "Manage apps and internal storage"),
    SearchableSettingItem("Face ID & Passcode", "Security", SettingsScreen.FaceIdAndPasscode, "App lock, biometric credentials, PIN"),
    SearchableSettingItem("Privacy & Security", "Security", SettingsScreen.PrivacyAndSecurity, "Permissions, location services, analytics"),
    SearchableSettingItem("Google Account & Services", "Accounts", SettingsScreen.GoogleServices, "Android sync, Play Services"),
    SearchableSettingItem("Siri 26", "Intelligence", SettingsScreen.SiriDemo, "Voice assistant simulation"),
    SearchableSettingItem("Apple Intelligence", "Intelligence", SettingsScreen.AppleIntelligenceDemo, "Visual intelligence & writing tools demo"),
    SearchableSettingItem("AirDrop", "Connectivity", SettingsScreen.AirDropDemo, "Simulated device discovery and file sharing"),
    SearchableSettingItem("Find My", "Services", SettingsScreen.FindMyDemo, "Locate Realme C71 4G and accessories"),
    SearchableSettingItem("iCloud", "Services", SettingsScreen.ICloudDemo, "Storage breakdown and cloud backup preview"),
    SearchableSettingItem("Notifications", "System", SettingsScreen.NotificationsDetail, "App notifications and Android notification channels"),
    SearchableSettingItem("Accessibility", "System", SettingsScreen.AccessibilityDetail, "Vision, hearing, motor and Android accessibility settings"),
    SearchableSettingItem("Focus", "System", SettingsScreen.FocusDetail, "Do Not Disturb, Work, and Sleep modes"),
    SearchableSettingItem("Screen Time", "System", SettingsScreen.ScreenTimeDetail, "App limits and usage graphs"),
    SearchableSettingItem("Wallpaper", "Display", SettingsScreen.WallpaperDetail, "iOS 26 dynamic wallpapers and Android wallpaper picker"),
    SearchableSettingItem("Control Center", "System", SettingsScreen.ControlCenterDetail, "Included controls and shortcuts"),
    SearchableSettingItem("Apps", "System", SettingsScreen.AppsDetail, "Manage installed apps and permissions")
  )
}
