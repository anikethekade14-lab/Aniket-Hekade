package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosOrange
import com.example.viewmodel.SettingsViewModel

@Composable
fun BatteryScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val telemetry by viewModel.telemetry.collectAsState()
  var lowPowerMode by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Battery",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Big Battery Percentage & Status Hero
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .background(if (telemetry.isBatteryCharging) IosGreen.copy(alpha = 0.2f) else Color(0xFF2C2C2E)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (telemetry.isBatteryCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
              contentDescription = "Battery Status",
              tint = if (telemetry.isBatteryCharging) IosGreen else Color.White,
              modifier = Modifier.size(44.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "${telemetry.batteryPercentage}%",
              style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )
            if (telemetry.isBatteryCharging) {
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = "Charging",
                tint = IosGreen,
                modifier = Modifier.size(28.dp)
              )
            }
          }

          Text(
            text = if (telemetry.isBatteryCharging) "Charging • Realme Dart / Fast Charge" else "Discharging • Good Condition",
            style = MaterialTheme.typography.bodyMedium,
            color = if (telemetry.isBatteryCharging) IosGreen else IosDarkTextSecondary
          )
        }
      }

      // Toggles
      item {
        IOSGroupedCard(
          footerText = "Low Power Mode temporarily reduces background activity like downloads and mail fetch until you can fully charge your phone."
        ) {
          IOSSettingsRow(
            title = "Battery Percentage",
            trailing = IOSTrailingContent.Switch(
              checked = true,
              onCheckedChange = {}
            )
          )

          IOSSettingsRow(
            title = "Low Power Mode",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = lowPowerMode,
              onCheckedChange = {
                lowPowerMode = it
                AndroidSettingsBridge.openBatterySettings(context)
              }
            )
          )
        }
      }

      // Battery Health & Charging
      item {
        IOSGroupedCard(headerText = "Battery Health & Optimization") {
          IOSSettingsRow(
            title = "Battery Health",
            icon = Icons.Default.HealthAndSafety,
            iconBackground = IosGreen,
            valueText = telemetry.batteryHealth
          )

          IOSSettingsRow(
            title = "Maximum Capacity",
            valueText = "100%",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Optimized Battery Charging",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = true,
              onCheckedChange = {}
            )
          )
        }
      }

      // Real Android Battery Settings Shortcut
      item {
        IOSGroupedCard(
          headerText = "Realme Battery Intelligence",
          footerText = "Inspect official Android 16 power graph, app battery usage breakdown, and ultra power saving mode."
        ) {
          IOSSettingsRow(
            title = "Open Android Battery Settings",
            icon = Icons.Default.BatteryAlert,
            iconBackground = IosOrange,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openBatterySettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
