package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosRed
import com.example.viewmodel.SettingsViewModel

@Composable
fun SoundsScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val settings by viewModel.appSettings.collectAsState()

  var ringVolume by remember { mutableFloatStateOf(0.75f) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Sounds & Haptics",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Ringtone and Alerts Volume Slider
      item {
        IOSGroupedCard(headerText = "Ringtone and Alert Volume") {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeDown,
                contentDescription = null,
                tint = IosDarkTextSecondary,
                modifier = Modifier.size(18.dp)
              )
              Slider(
                value = ringVolume,
                onValueChange = { ringVolume = it },
                colors = SliderDefaults.colors(
                  thumbColor = Color.White,
                  activeTrackColor = Color.White,
                  inactiveTrackColor = Color(0xFF38383A)
                ),
                modifier = Modifier
                  .weight(1f)
                  .padding(horizontal = 10.dp)
              )
              Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
          }

          IOSSettingsRow(
            title = "Change with Buttons",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = true,
              onCheckedChange = {}
            )
          )
        }
      }

      // Android Sounds & Haptics Shortcuts
      item {
        IOSGroupedCard(
          headerText = "Real Android Audio Settings",
          footerText = "Configure device ringtones, notifications, and vibration intensities directly in Android 16."
        ) {
          IOSSettingsRow(
            title = "Ringtone Settings",
            subtitle = "Choose SIM 1 / SIM 2 Ringtone",
            icon = Icons.Default.MusicNote,
            iconBackground = IosRed,
            onClick = { AndroidSettingsBridge.openSoundSettings(context) }
          )

          IOSSettingsRow(
            title = "Notification & Alert Sounds",
            subtitle = "Android 16 notification channels",
            icon = Icons.Default.Notifications,
            iconBackground = IosOrange,
            onClick = { AndroidSettingsBridge.openNotificationSettings(context) }
          )

          IOSSettingsRow(
            title = "Vibration & Haptics",
            subtitle = "Realme tactile feedback engine",
            icon = Icons.Default.Vibration,
            iconBackground = IosBlue,
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openSoundSettings(context) }
          )
        }
      }

      // System Sounds & Haptics
      item {
        IOSGroupedCard(headerText = "System Haptics") {
          IOSSettingsRow(
            title = "Keyboard Feedback",
            valueText = "Sound & Haptic",
            onClick = { AndroidSettingsBridge.openKeyboardSettings(context) }
          )

          IOSSettingsRow(
            title = "Lock Sound",
            trailing = IOSTrailingContent.Switch(
              checked = true,
              onCheckedChange = {}
            )
          )

          IOSSettingsRow(
            title = "System Haptics",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = settings.hapticsEnabled,
              onCheckedChange = {}
            )
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
