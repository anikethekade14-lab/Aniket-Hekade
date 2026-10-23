package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.system.AndroidSettingsBridge
import com.example.ui.components.IOSGroupedCard
import com.example.ui.components.IOSSettingsRow
import com.example.ui.components.IOSTopAppBar
import com.example.ui.components.IOSTrailingContent
import com.example.ui.theme.IosBlue
import com.example.ui.theme.IosCyan
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosIndigo
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosPurple
import com.example.viewmodel.SettingsScreen
import com.example.viewmodel.SettingsViewModel

@Composable
fun DisplayScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val settings by viewModel.appSettings.collectAsState()
  val telemetry by viewModel.telemetry.collectAsState()

  var previewBrightness by remember(telemetry.currentBrightnessPercent) {
    mutableFloatStateOf(telemetry.currentBrightnessPercent.toFloat())
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Display & Brightness",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Light / Dark Mode Visual Selector
      item {
        IOSGroupedCard(headerText = "Appearance") {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            // Light Option
            AppearanceCard(
              title = "Light",
              isDark = false,
              isSelected = !settings.isDarkMode,
              onClick = { viewModel.toggleDarkMode(false) }
            )

            // Dark Option
            AppearanceCard(
              title = "Dark",
              isDark = true,
              isSelected = settings.isDarkMode,
              onClick = { viewModel.toggleDarkMode(true) }
            )
          }

          IOSSettingsRow(
            title = "Automatic (Follow System)",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = false,
              onCheckedChange = { /* Dynamic */ }
            )
          )
        }
      }

      // iOS 26 Dynamic Accent Palette
      item {
        IOSGroupedCard(
          headerText = "iOS 26 Accent Tint",
          footerText = "Select your preferred dynamic neon color for toggles and highlights."
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            AccentPill(
              name = "Cyan",
              color = IosCyan,
              isSelected = settings.accentStyle == "dynamic_multicolor",
              onClick = { viewModel.setAccentStyle("dynamic_multicolor") }
            )

            AccentPill(
              name = "Azure",
              color = IosBlue,
              isSelected = settings.accentStyle == "blue",
              onClick = { viewModel.setAccentStyle("blue") }
            )

            AccentPill(
              name = "Purple",
              color = IosPurple,
              isSelected = settings.accentStyle == "purple",
              onClick = { viewModel.setAccentStyle("purple") }
            )

            AccentPill(
              name = "Emerald",
              color = IosGreen,
              isSelected = settings.accentStyle == "emerald",
              onClick = { viewModel.setAccentStyle("emerald") }
            )

            AccentPill(
              name = "Sunset",
              color = IosOrange,
              isSelected = settings.accentStyle == "sunset",
              onClick = { viewModel.setAccentStyle("sunset") }
            )
          }
        }
      }

      // Brightness Slider & Android Display Settings
      item {
        IOSGroupedCard(
          headerText = "Brightness",
          footerText = "Current system brightness level: ${telemetry.currentBrightnessPercent}%. Tap below to adjust real hardware brightness."
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 10.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.BrightnessLow,
                contentDescription = null,
                tint = IosDarkTextSecondary,
                modifier = Modifier.size(18.dp)
              )
              Slider(
                value = previewBrightness,
                onValueChange = { previewBrightness = it },
                valueRange = 0f..100f,
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
                imageVector = Icons.Default.BrightnessMedium,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
          }

          IOSSettingsRow(
            title = "Open Android Display Settings",
            subtitle = "Modify system adaptive brightness & timeout",
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openDisplaySettings(context) }
          )
        }
      }

      // Visual Features
      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "True Tone",
            subtitle = "Simulated color temperature adaptation",
            trailing = IOSTrailingContent.Switch(
              checked = settings.trueToneEnabled,
              onCheckedChange = { viewModel.toggleTrueTone(it) }
            )
          )

          IOSSettingsRow(
            title = "Night Shift",
            subtitle = "Scheduled sunset to sunrise",
            trailing = IOSTrailingContent.Switch(
              checked = settings.nightShiftEnabled,
              onCheckedChange = { viewModel.toggleNightShift(it) }
            )
          )

          IOSSettingsRow(
            title = "Always-On Display Demo",
            icon = Icons.Default.StayCurrentPortrait,
            iconBackground = IosIndigo,
            valueText = if (settings.alwaysOnDisplayEnabled) "On" else "Off",
            onClick = { viewModel.navigateTo(SettingsScreen.AlwaysOnDisplayDemo) }
          )

          IOSSettingsRow(
            title = "Wallpaper",
            icon = Icons.Default.Wallpaper,
            iconBackground = IosCyan,
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openWallpaperSettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
private fun AppearanceCard(
  title: String,
  isDark: Boolean,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .width(84.dp)
        .height(110.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(if (isDark) Color(0xFF1C1C1E) else Color(0xFFF2F2F7))
        .border(
          width = if (isSelected) 2.dp else 1.dp,
          color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF38383A),
          shape = RoundedCornerShape(12.dp)
        )
        .padding(8.dp)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFE5E5EA))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFFFFFFF))
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface,
      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
    )

    Spacer(modifier = Modifier.height(4.dp))

    Box(
      modifier = Modifier
        .size(18.dp)
        .clip(CircleShape)
        .border(
          width = 1.5.dp,
          color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF636366),
          shape = CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      if (isSelected) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
        )
      }
    }
  }
}

@Composable
private fun AccentPill(
  name: String,
  color: Color,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(color)
        .border(
          width = if (isSelected) 2.5.dp else 0.dp,
          color = Color.White,
          shape = CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = Color.Black,
          modifier = Modifier.size(18.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = name,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
      color = if (isSelected) MaterialTheme.colorScheme.onSurface else IosDarkTextSecondary
    )
  }
}
