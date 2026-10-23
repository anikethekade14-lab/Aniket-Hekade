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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosPurple
import com.example.ui.theme.IosRed
import com.example.viewmodel.SettingsViewModel

@Composable
fun StorageDetailScreen(
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
      title = "iPhone Storage",
      backText = "General",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Storage Meter Hero
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
          Text(
            text = "${telemetry.freeStorageGb} Free of ${telemetry.totalStorageGb}",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Multi-color segmented bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFF38383A))
          ) {
            Box(modifier = Modifier.weight(0.35f).height(12.dp).background(IosRed))
            Box(modifier = Modifier.weight(0.20f).height(12.dp).background(Color(0xFFFFD60A)))
            Box(modifier = Modifier.weight(0.15f).height(12.dp).background(IosPurple))
            Box(modifier = Modifier.weight(0.10f).height(12.dp).background(Color(0xFF8E8E93)))
            Spacer(modifier = Modifier.weight(0.20f))
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            StorageLegend(color = IosRed, label = "Apps")
            Spacer(modifier = Modifier.width(10.dp))
            StorageLegend(color = Color(0xFFFFD60A), label = "Media")
            Spacer(modifier = Modifier.width(10.dp))
            StorageLegend(color = IosPurple, label = "System")
            Spacer(modifier = Modifier.width(10.dp))
            StorageLegend(color = Color(0xFF8E8E93), label = "Other")
            Spacer(modifier = Modifier.width(10.dp))
            StorageLegend(color = Color(0xFF38383A), label = "Free")
          }
        }
      }

      // Manage Apps via Android Settings
      item {
        IOSGroupedCard(
          headerText = "Application Storage Management",
          footerText = "Opens official Android 16 Application Manager where you can clear cache or free up space on your Realme C71 4G."
        ) {
          IOSSettingsRow(
            title = "Manage Installed Apps",
            subtitle = "View app sizes and clear cache",
            icon = Icons.Default.Apps,
            iconBackground = IosBlue,
            onClick = { AndroidSettingsBridge.openApplicationSettings(context) }
          )

          IOSSettingsRow(
            title = "iOS Settings App Info",
            subtitle = "Package details, permissions & storage",
            icon = Icons.Default.Info,
            iconBackground = Color(0xFF8E8E93),
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openAppDetailsSettings(context) }
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
private fun StorageLegend(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
      color = IosDarkTextSecondary
    )
  }
}
