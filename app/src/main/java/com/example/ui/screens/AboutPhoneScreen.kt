package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.system.AndroidSettingsBridge
import com.example.ui.components.IOSGroupedCard
import com.example.ui.components.IOSSettingsRow
import com.example.ui.components.IOSTopAppBar
import com.example.ui.components.IOSTrailingContent
import com.example.viewmodel.SettingsViewModel

@Composable
fun AboutPhoneScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val telemetry by viewModel.telemetry.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "About",
      backText = "General",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Primary Device Identification (Realme C71 4G)
      item {
        IOSGroupedCard(
          headerText = "Device Identity",
          footerText = "Identified as your genuine Android device. Never falsely labeled as an iPhone."
        ) {
          IOSSettingsRow(
            title = "Name",
            valueText = telemetry.deviceName,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Manufacturer",
            valueText = telemetry.manufacturer,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Model Number",
            valueText = telemetry.modelNumber,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "UI Theme Layer",
            valueText = "iOS 26 Liquid Theme",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Operating System",
            valueText = telemetry.androidVersion,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Android API Level",
            valueText = "API ${telemetry.apiLevel}",
            showSeparator = false,
            trailing = IOSTrailingContent.None
          )
        }
      }

      // Memory & Hardware Specifications
      item {
        IOSGroupedCard(headerText = "Hardware Specifications") {
          IOSSettingsRow(
            title = "RAM Capacity",
            subtitle = "LPDDR4X Physical Memory",
            valueText = telemetry.totalRamGb,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Available Memory",
            valueText = telemetry.availableRamGb,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Internal Storage",
            valueText = telemetry.totalStorageGb,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Available Storage",
            valueText = telemetry.freeStorageGb,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Kernel Version",
            valueText = telemetry.kernelVersion,
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Build Number",
            valueText = telemetry.buildNumber,
            showSeparator = false,
            trailing = IOSTrailingContent.None
          )
        }
      }

      // Real Android Settings Button
      item {
        IOSGroupedCard(
          footerText = "Open official Android 16 Device Information to view official regulatory labels, IMEI, and SIM status."
        ) {
          IOSSettingsRow(
            title = "Open Android Device Info",
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openAboutDeviceSettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
