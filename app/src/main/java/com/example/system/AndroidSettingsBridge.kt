package com.example.system

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast

object AndroidSettingsBridge {

  private fun safeStart(context: Context, intent: Intent, featureName: String) {
    try {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(intent)
    } catch (e: Exception) {
      // Fallback to primary Settings screen if specific screen intent fails
      try {
        val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(fallbackIntent)
        Toast.makeText(context, "Opened System Settings ($featureName)", Toast.LENGTH_SHORT).show()
      } catch (inner: Exception) {
        Toast.makeText(context, "Unable to open system settings for $featureName", Toast.LENGTH_SHORT).show()
      }
    }
  }

  fun openWifiSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_WIFI_SETTINGS), "Wi-Fi")
  }

  fun openBluetoothSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_BLUETOOTH_SETTINGS), "Bluetooth")
  }

  fun openMobileNetworkSettings(context: Context) {
    val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
    safeStart(context, intent, "Mobile Network")
  }

  fun openHotspotSettings(context: Context) {
    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
    safeStart(context, intent, "Personal Hotspot")
  }

  fun openAirplaneModeSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS), "Airplane Mode")
  }

  fun openVpnSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_VPN_SETTINGS), "VPN")
  }

  fun openDisplaySettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_DISPLAY_SETTINGS), "Display")
  }

  fun openWallpaperSettings(context: Context) {
    val intent = Intent(Intent.ACTION_SET_WALLPAPER)
    safeStart(context, intent, "Wallpaper")
  }

  fun openSoundSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_SOUND_SETTINGS), "Sound & Haptics")
  }

  fun openNotificationSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
      }
    } else {
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
      }
    }
    safeStart(context, intent, "Notifications")
  }

  fun openBatterySettings(context: Context) {
    val intent = Intent(Intent.ACTION_POWER_USAGE_SUMMARY)
    safeStart(context, intent, "Battery")
  }

  fun openApplicationSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS), "Applications")
  }

  fun openAppDetailsSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
      data = Uri.fromParts("package", context.packageName, null)
    }
    safeStart(context, intent, "App Details")
  }

  fun openSecuritySettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_SECURITY_SETTINGS), "Privacy & Security")
  }

  fun openAccessibilitySettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), "Accessibility")
  }

  fun openAboutDeviceSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_DEVICE_INFO_SETTINGS), "About Phone")
  }

  fun openDateSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_DATE_SETTINGS), "Date & Time")
  }

  fun openKeyboardSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), "Keyboard")
  }

  fun openLanguageSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_LOCALE_SETTINGS), "Language")
  }

  fun openGoogleAccountSettings(context: Context) {
    safeStart(context, Intent(Settings.ACTION_SYNC_SETTINGS), "Google Accounts")
  }

  fun openResetSettings(context: Context) {
    // Open system reset settings safely (never triggers actual reset!)
    val intent = Intent(Settings.ACTION_SETTINGS)
    safeStart(context, intent, "System Settings (Reset)")
  }
}
