package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.system.AndroidSettingsBridge
import com.example.ui.components.IOSGroupedCard
import com.example.ui.components.IOSSearchBar
import com.example.ui.components.IOSSettingsRow
import com.example.ui.components.IOSTrailingContent
import com.example.ui.theme.IosBlue
import com.example.ui.theme.IosCyan
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosIndigo
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosPink
import com.example.ui.theme.IosPurple
import com.example.ui.theme.IosRed
import com.example.viewmodel.SettingsScreen
import com.example.viewmodel.SettingsViewModel

@Composable
fun MainSettingsScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile by viewModel.profile.collectAsState()
  val settings by viewModel.appSettings.collectAsState()
  val telemetry by viewModel.telemetry.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Large Header Title
    item {
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = "Settings",
        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
          .padding(horizontal = 20.dp, vertical = 6.dp)
          .testTag("settings_main_title")
      )
    }

    // Search Bar
    item {
      IOSSearchBar(
        query = searchQuery,
        onQueryChange = { viewModel.onSearchQueryChanged(it) },
        placeholder = "Search",
        testTag = "main_search_input"
      )
      Spacer(modifier = Modifier.height(4.dp))
    }

    // Dynamic Search Filter Results
    if (searchQuery.isNotBlank()) {
      val filteredItems = viewModel.allSearchableItems.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
            it.section.contains(searchQuery, ignoreCase = true) ||
            (it.subtitle?.contains(searchQuery, ignoreCase = true) == true)
      }

      if (filteredItems.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No results for \"$searchQuery\"",
              style = MaterialTheme.typography.bodyMedium,
              color = IosDarkTextSecondary
            )
          }
        }
      } else {
        item {
          IOSGroupedCard(headerText = "Search Results") {
            filteredItems.forEachIndexed { idx, item ->
              IOSSettingsRow(
                title = item.title,
                subtitle = item.subtitle,
                valueText = item.section,
                showSeparator = idx < filteredItems.size - 1,
                trailing = IOSTrailingContent.Chevron,
                onClick = {
                  viewModel.onSearchQueryChanged("")
                  viewModel.navigateTo(item.screen)
                }
              )
            }
          }
        }
      }
    } else {
      // Profile Card Section
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable { viewModel.navigateTo(SettingsScreen.ProfileDetail) }
            .testTag("profile_header_card")
            .padding(16.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Profile Avatar
            if (!profile.avatarUri.isNullOrBlank()) {
              AsyncImage(
                model = profile.avatarUri,
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape)
              )
            } else {
              Box(
                modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.linearGradient(
                      listOf(Color(0xFF8E8E93), Color(0xFF636366))
                    )
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = profile.displayName.take(1).uppercase(),
                  style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color.White
                )
              }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = profile.displayName,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 19.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = profile.email,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = IosDarkTextSecondary
              )
              Text(
                text = "Apple Account, iCloud+, Media",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.primary
              )
            }

            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = "Open Profile",
              tint = Color(0xFF636366),
              modifier = Modifier.size(13.dp)
            )
          }
        }
      }

      // Connectivity Group
      item {
        IOSGroupedCard {
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
            title = "Wi-Fi",
            icon = Icons.Default.Wifi,
            iconBackground = IosBlue,
            valueText = if (telemetry.wifiConnected) telemetry.wifiSsid else "Off",
            onClick = { viewModel.navigateTo(SettingsScreen.Connectivity) }
          )

          IOSSettingsRow(
            title = "Bluetooth",
            icon = Icons.Default.Bluetooth,
            iconBackground = IosBlue,
            valueText = if (telemetry.bluetoothEnabled) "On" else "Off",
            onClick = { viewModel.navigateTo(SettingsScreen.Connectivity) }
          )

          IOSSettingsRow(
            title = "Mobile Service",
            icon = Icons.Default.PhoneAndroid,
            iconBackground = IosGreen,
            valueText = "Realme 4G LTE",
            onClick = { AndroidSettingsBridge.openMobileNetworkSettings(context) }
          )

          IOSSettingsRow(
            title = "Personal Hotspot",
            icon = Icons.Default.WifiTethering,
            iconBackground = IosGreen,
            valueText = "Off",
            onClick = { AndroidSettingsBridge.openHotspotSettings(context) }
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

      // Display & Sounds
      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "Display & Brightness",
            icon = Icons.Default.BrightnessMedium,
            iconBackground = IosBlue,
            valueText = if (settings.isDarkMode) "Dark" else "Light",
            onClick = { viewModel.navigateTo(SettingsScreen.DisplayAndBrightness) }
          )

          IOSSettingsRow(
            title = "Always-On Display",
            icon = Icons.Default.StayCurrentPortrait,
            iconBackground = IosIndigo,
            valueText = if (settings.alwaysOnDisplayEnabled) "On" else "Off",
            onClick = { viewModel.navigateTo(SettingsScreen.AlwaysOnDisplayDemo) }
          )

          IOSSettingsRow(
            title = "Wallpaper",
            icon = Icons.Default.Wallpaper,
            iconBackground = IosCyan,
            onClick = { viewModel.navigateTo(SettingsScreen.WallpaperDetail) }
          )

          IOSSettingsRow(
            title = "Sounds & Haptics",
            icon = Icons.AutoMirrored.Filled.VolumeUp,
            iconBackground = IosRed,
            showSeparator = false,
            onClick = { viewModel.navigateTo(SettingsScreen.SoundsAndHaptics) }
          )
        }
      }

      // System Core Group
      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "Notifications",
            icon = Icons.Default.Notifications,
            iconBackground = IosRed,
            onClick = { viewModel.navigateTo(SettingsScreen.NotificationsDetail) }
          )

          IOSSettingsRow(
            title = "Focus",
            icon = Icons.Default.Bedtime,
            iconBackground = IosPurple,
            valueText = "Off",
            onClick = { viewModel.navigateTo(SettingsScreen.FocusDetail) }
          )

          IOSSettingsRow(
            title = "Screen Time",
            icon = Icons.Default.HourglassBottom,
            iconBackground = IosIndigo,
            valueText = "4h 12m",
            onClick = { viewModel.navigateTo(SettingsScreen.ScreenTimeDetail) }
          )

          IOSSettingsRow(
            title = "General",
            icon = Icons.Default.Settings,
            iconBackground = Color(0xFF8E8E93),
            onClick = { viewModel.navigateTo(SettingsScreen.General) }
          )

          IOSSettingsRow(
            title = "Control Center",
            icon = Icons.Default.Apps,
            iconBackground = Color(0xFF8E8E93),
            onClick = { viewModel.navigateTo(SettingsScreen.ControlCenterDetail) }
          )

          IOSSettingsRow(
            title = "Accessibility",
            icon = Icons.Default.Face,
            iconBackground = IosBlue,
            onClick = { viewModel.navigateTo(SettingsScreen.AccessibilityDetail) }
          )

          IOSSettingsRow(
            title = "Battery",
            icon = Icons.Default.BatteryFull,
            iconBackground = IosGreen,
            valueText = "${telemetry.batteryPercentage}%" + if (telemetry.isBatteryCharging) " ⚡" else "",
            onClick = { viewModel.navigateTo(SettingsScreen.BatteryDetail) }
          )

          IOSSettingsRow(
            title = "Privacy & Security",
            icon = Icons.Default.Security,
            iconBackground = IosBlue,
            onClick = { viewModel.navigateTo(SettingsScreen.PrivacyAndSecurity) }
          )

          IOSSettingsRow(
            title = "Apps",
            icon = Icons.Default.Apps,
            iconBackground = Color(0xFF5E5CE6),
            showSeparator = false,
            onClick = { viewModel.navigateTo(SettingsScreen.AppsDetail) }
          )
        }
      }

      // Accounts & Ecosystem
      item {
        IOSGroupedCard(headerText = "Accounts & Cloud") {
          IOSSettingsRow(
            title = "iCloud",
            icon = Icons.Default.Cloud,
            iconBackground = IosBlue,
            valueText = "${profile.icloudStorageUsedGb} GB of ${profile.icloudStorageTotalGb} GB",
            onClick = { viewModel.navigateTo(SettingsScreen.ICloudDemo) }
          )

          IOSSettingsRow(
            title = "Find My",
            icon = Icons.Default.LocationOn,
            iconBackground = IosGreen,
            valueText = "This Realme C71",
            onClick = { viewModel.navigateTo(SettingsScreen.FindMyDemo) }
          )

          IOSSettingsRow(
            title = "Google Account & Services",
            icon = Icons.Default.Sync,
            iconBackground = Color(0xFF4285F4),
            valueText = "Android 16 Sync",
            showSeparator = false,
            onClick = { viewModel.navigateTo(SettingsScreen.GoogleServices) }
          )
        }
      }

      // Next-Gen iOS 26 Showpiece Features
      item {
        IOSGroupedCard(headerText = "iOS 26 Experiences & Intelligence") {
          IOSSettingsRow(
            title = "Apple Intelligence",
            icon = Icons.Default.AutoAwesome,
            iconBrush = Brush.linearGradient(listOf(IosIndigo, IosPurple, IosPink)),
            valueText = "Beta Preview",
            onClick = { viewModel.navigateTo(SettingsScreen.AppleIntelligenceDemo) }
          )

          IOSSettingsRow(
            title = "Siri 26",
            icon = Icons.Default.Mic,
            iconBrush = Brush.linearGradient(listOf(IosCyan, IosIndigo)),
            onClick = { viewModel.navigateTo(SettingsScreen.SiriDemo) }
          )

          IOSSettingsRow(
            title = "AirDrop",
            icon = Icons.Default.Share,
            iconBackground = IosBlue,
            valueText = "Contacts Only",
            onClick = { viewModel.navigateTo(SettingsScreen.AirDropDemo) }
          )

          IOSSettingsRow(
            title = "Face ID & Passcode",
            icon = Icons.Default.Fingerprint,
            iconBackground = IosGreen,
            valueText = if (settings.biometricAppLockEnabled) "Enabled" else "Off",
            showSeparator = false,
            onClick = { viewModel.navigateTo(SettingsScreen.FaceIdAndPasscode) }
          )
        }
      }

      // Footer
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "iOS Settings 26 • Realme C71 4G",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = IosDarkTextSecondary
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Android 16 • 4 GB RAM • Powered by Jetpack Compose",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = IosDarkTextSecondary.copy(alpha = 0.7f)
          )
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}
