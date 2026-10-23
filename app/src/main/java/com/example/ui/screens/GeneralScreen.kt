package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.system.AndroidSettingsBridge
import com.example.ui.components.IOSGroupedCard
import com.example.ui.components.IOSSettingsRow
import com.example.ui.components.IOSTopAppBar
import com.example.ui.components.IOSTrailingContent
import com.example.ui.theme.IosBlue
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosPurple
import com.example.viewmodel.SettingsScreen
import com.example.viewmodel.SettingsViewModel

@Composable
fun GeneralScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val telemetry by viewModel.telemetry.collectAsState()
  val settings by viewModel.appSettings.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "General",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // About & Update
      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "About",
            icon = Icons.Default.Info,
            iconBackground = Color(0xFF8E8E93),
            valueText = telemetry.deviceName,
            onClick = { viewModel.navigateTo(SettingsScreen.AboutPhone) }
          )

          IOSSettingsRow(
            title = "Software Update",
            icon = Icons.Default.SystemUpdate,
            iconBackground = IosBlue,
            valueText = "iOS 26.1 Preview",
            showSeparator = false,
            onClick = { viewModel.navigateTo(SettingsScreen.SoftwareUpdate) }
          )
        }
      }

      // Storage & App Refresh
      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "iPhone Storage",
            subtitle = "Realme C71 4G Storage",
            icon = Icons.Default.SdStorage,
            iconBackground = IosPurple,
            valueText = "${telemetry.freeStorageGb} Free",
            onClick = { viewModel.navigateTo(SettingsScreen.StorageDetail) }
          )

          IOSSettingsRow(
            title = "Background App Refresh",
            icon = Icons.Default.Refresh,
            iconBackground = IosGreen,
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = settings.backgroundAppRefresh,
              onCheckedChange = {
                // Toggle demo
              }
            )
          )
        }
      }

      // Real Android System Shortcuts
      item {
        IOSGroupedCard(
          headerText = "Regional & Input Shortcuts",
          footerText = "Tapping these options opens the corresponding configuration in Android 16."
        ) {
          IOSSettingsRow(
            title = "Date & Time",
            icon = Icons.Default.Schedule,
            iconBackground = IosOrange,
            onClick = { AndroidSettingsBridge.openDateSettings(context) }
          )

          IOSSettingsRow(
            title = "Keyboard",
            icon = Icons.Default.Keyboard,
            iconBackground = Color(0xFF8E8E93),
            onClick = { AndroidSettingsBridge.openKeyboardSettings(context) }
          )

          IOSSettingsRow(
            title = "Language & Region",
            icon = Icons.Default.Language,
            iconBackground = IosBlue,
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openLanguageSettings(context) }
          )
        }
      }

      // Reset
      item {
        IOSGroupedCard(
          headerText = "System Recovery",
          footerText = "Opens official Android system recovery & backup settings. Destructive actions are never run inside this app."
        ) {
          IOSSettingsRow(
            title = "Transfer or Reset",
            icon = Icons.Default.Restore,
            iconBackground = Color(0xFFFF453A),
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openResetSettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
