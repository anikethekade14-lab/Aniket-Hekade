package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.viewmodel.SettingsViewModel

@Composable
fun ConnectivityScreen(
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
      title = "Connectivity",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Wi-Fi Section
      item {
        IOSGroupedCard(
          headerText = "Wi-Fi Network",
          footerText = "Android 16 restricts third-party apps from toggling Wi-Fi directly. Tap to configure networks in official Android Settings."
        ) {
          IOSSettingsRow(
            title = "Current Wi-Fi",
            icon = Icons.Default.Wifi,
            iconBackground = IosBlue,
            valueText = if (telemetry.wifiConnected) telemetry.wifiSsid else "Disconnected",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Open Android Wi-Fi Settings",
            icon = Icons.Default.Wifi,
            iconBackground = IosBlue,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openWifiSettings(context) }
          )
        }
      }

      // Bluetooth Section
      item {
        IOSGroupedCard(
          headerText = "Bluetooth",
          footerText = "View active Bluetooth state and manage paired accessories through official Android settings."
        ) {
          IOSSettingsRow(
            title = "Bluetooth Status",
            icon = Icons.Default.Bluetooth,
            iconBackground = IosBlue,
            valueText = if (telemetry.bluetoothEnabled) "On" else "Off",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Open Android Bluetooth Settings",
            icon = Icons.Default.Bluetooth,
            iconBackground = IosBlue,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openBluetoothSettings(context) }
          )
        }
      }

      // Mobile Network & Hotspot
      item {
        IOSGroupedCard(headerText = "Cellular & Hotspot") {
          IOSSettingsRow(
            title = "Mobile Service (SIM & Data)",
            subtitle = "Manage SIMs, 4G LTE, Roaming",
            icon = Icons.Default.PhoneAndroid,
            iconBackground = IosGreen,
            onClick = { AndroidSettingsBridge.openMobileNetworkSettings(context) }
          )

          IOSSettingsRow(
            title = "Personal Hotspot",
            subtitle = "USB & Wi-Fi Tethering",
            icon = Icons.Default.WifiTethering,
            iconBackground = IosGreen,
            onClick = { AndroidSettingsBridge.openHotspotSettings(context) }
          )

          IOSSettingsRow(
            title = "Airplane Mode",
            icon = Icons.Default.AirplanemodeActive,
            iconBackground = IosOrange,
            trailing = IOSTrailingContent.Switch(
              checked = settings.airplaneMode,
              onCheckedChange = {
                viewModel.toggleAirplaneMode(it)
                AndroidSettingsBridge.openAirplaneModeSettings(context)
              }
            )
          )

          IOSSettingsRow(
            title = "VPN & Device Management",
            icon = Icons.Default.VpnKey,
            iconBackground = IosBlue,
            valueText = if (settings.vpnActive) "Connected" else "Not Connected",
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openVpnSettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
