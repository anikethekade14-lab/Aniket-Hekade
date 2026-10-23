package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.IosPink
import com.example.ui.theme.IosPurple
import com.example.viewmodel.SettingsViewModel
import kotlin.math.sin

// ==================== SIRI 26 DEMO ====================
@Composable
fun SiriDemoScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  var siriResponse by remember { mutableStateOf("What can I help you with on your Realme C71 4G?") }
  var isListening by remember { mutableStateOf(false) }

  val transition = rememberInfiniteTransition(label = "siriWave")
  val wavePhase by transition.animateFloat(
    initialValue = 0f,
    targetValue = 6.28f,
    animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
    label = "wavePhase"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Siri 26",
      onBackClick = { viewModel.navigateBack() }
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      DemoDisclaimerBadge(text = "Interactive Siri 26 Simulation for Realme C71 4G")

      Spacer(modifier = Modifier.weight(0.6f))

      // Siri Glowing Orb / Wave
      Box(
        modifier = Modifier
          .size(160.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              listOf(IosCyan.copy(alpha = 0.8f), IosPurple.copy(alpha = 0.5f), Color.Transparent)
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val center = Offset(size.width / 2, size.height / 2)
          val radius = size.width / 2.5f
          for (i in 0..3) {
            val offsetRadius = radius + sin(wavePhase + i) * 12.dp.toPx()
            drawCircle(
              brush = Brush.linearGradient(listOf(IosCyan, IosIndigo, IosPink)),
              radius = offsetRadius.coerceAtLeast(10f),
              center = center,
              style = Stroke(width = 3.dp.toPx())
            )
          }
        }

        Icon(
          imageVector = Icons.Default.Mic,
          contentDescription = "Siri",
          tint = Color.White,
          modifier = Modifier.size(54.dp)
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = siriResponse,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.weight(1f))

      // Suggested Prompts
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "TRY ASKING",
          style = MaterialTheme.typography.labelSmall,
          color = IosDarkTextSecondary,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        SiriPromptPill(text = "\"What is my Realme C71 battery health?\"") {
          siriResponse = "Your Realme C71 4G battery is in normal health condition with maximum capacity."
        }

        Spacer(modifier = Modifier.height(8.dp))

        SiriPromptPill(text = "\"Open Android Wi-Fi settings\"") {
          siriResponse = "Opening Android Wi-Fi network settings..."
          AndroidSettingsBridge.openWifiSettings(context)
        }

        Spacer(modifier = Modifier.height(8.dp))

        SiriPromptPill(text = "\"How much RAM is installed?\"") {
          siriResponse = "Your Realme C71 4G is equipped with 4 GB of physical LPDDR4X memory."
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun SiriPromptPill(text: String, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF1C1C1E))
      .border(0.5.dp, Color(0xFF2C2C2E), RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodyMedium,
      color = IosCyan
    )
  }
}

// ==================== APPLE INTELLIGENCE DEMO ====================
@Composable
fun AppleIntelligenceDemoScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Apple Intelligence",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(
          text = "Apple Intelligence Preview: Designed for iOS 26 concept demonstration on Realme C71 4G."
        )
      }

      // Feature Hero
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(listOf(IosIndigo, IosPurple, IosPink))
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Intelligence",
              tint = Color.White,
              modifier = Modifier.size(38.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Apple Intelligence",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )

          Text(
            text = "Personal intelligence integrated across iOS 26",
            style = MaterialTheme.typography.bodyMedium,
            color = IosDarkTextSecondary,
            textAlign = TextAlign.Center
          )
        }
      }

      // Features Cards
      item {
        IOSGroupedCard(headerText = "Core Capabilities Preview") {
          IOSSettingsRow(
            title = "Writing Tools",
            subtitle = "Proofread, rewrite, and summarize text",
            icon = Icons.Default.Edit,
            iconBackground = IosPurple,
            valueText = "Ready"
          )

          IOSSettingsRow(
            title = "Visual Intelligence",
            subtitle = "Camera recognition for objects & places",
            icon = Icons.Default.AutoAwesome,
            iconBackground = IosCyan,
            valueText = "Preview"
          )

          IOSSettingsRow(
            title = "On-Device Neural Engine",
            subtitle = "Simulated for 4 GB RAM budget",
            icon = Icons.Default.Security,
            iconBackground = IosGreen,
            showSeparator = false,
            valueText = "Optimized"
          )
        }
      }

      item {
        IOSGroupedCard(
          headerText = "Language & Regional Models",
          footerText = "Apple Intelligence features are demonstration models in this application."
        ) {
          IOSSettingsRow(
            title = "Language",
            valueText = "English (United States)",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Siri Voice",
            valueText = "Voice 4 (American)",
            showSeparator = false,
            trailing = IOSTrailingContent.None
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

// ==================== AIRDROP DEMO ====================
@Composable
fun AirDropDemoScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  var selectedDiscovery by remember { mutableStateOf("Contacts Only") }

  val transition = rememberInfiniteTransition(label = "airdropRadar")
  val radarSweep by transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing)),
    label = "radar"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "AirDrop",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(text = "AirDrop Simulation: Cross-device Apple AirDrop is simulated on Android.")
      }

      // Animated Radar Hero
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
          ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val center = Offset(size.width / 2, size.height / 2)
              val maxRadius = size.width / 2

              // Expanding ring 1
              val r1 = (maxRadius * radarSweep).coerceAtLeast(1f)
              drawCircle(
                color = IosBlue.copy(alpha = (1f - radarSweep).coerceIn(0f, 1f)),
                radius = r1,
                center = center,
                style = Stroke(width = 2.dp.toPx())
              )

              // Expanding ring 2
              val phase2 = (radarSweep + 0.5f) % 1f
              val r2 = (maxRadius * phase2).coerceAtLeast(1f)
              drawCircle(
                color = IosCyan.copy(alpha = (1f - phase2).coerceIn(0f, 1f)),
                radius = r2,
                center = center,
                style = Stroke(width = 2.dp.toPx())
              )
            }

            Box(
              modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(IosBlue),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "AirDrop",
                tint = Color.White,
                modifier = Modifier.size(34.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Discovering Nearby Devices...",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
          )

          Text(
            text = "Your Realme C71 4G is visible to other devices",
            style = MaterialTheme.typography.bodyMedium,
            color = IosDarkTextSecondary
          )
        }
      }

      // AirDrop Visibility
      item {
        IOSGroupedCard(headerText = "Allow Me To Be Discovered By") {
          val options = listOf("Receiving Off", "Contacts Only", "Everyone for 10 Minutes")
          options.forEachIndexed { idx, opt ->
            IOSSettingsRow(
              title = opt,
              trailing = if (selectedDiscovery == opt) IOSTrailingContent.None else IOSTrailingContent.None,
              valueText = if (selectedDiscovery == opt) "✓" else "",
              showSeparator = idx < options.size - 1,
              onClick = { selectedDiscovery = opt }
            )
          }
        }
      }

      item {
        IOSGroupedCard(
          headerText = "Nearby Devices Discovered",
          footerText = "Tap a device to simulate sending an AirDrop package with haptic feedback."
        ) {
          IOSSettingsRow(
            title = "Aniket's MacBook Pro 16\"",
            subtitle = "Ready to receive",
            icon = Icons.Default.Share,
            iconBackground = IosBlue,
            onClick = {
              triggerHaptic(context)
              Toast.makeText(context, "Simulated AirDrop sent to MacBook Pro 16\"!", Toast.LENGTH_SHORT).show()
            }
          )

          IOSSettingsRow(
            title = "iPad Pro 13\"",
            subtitle = "Ready to receive",
            icon = Icons.Default.Share,
            iconBackground = IosCyan,
            showSeparator = false,
            onClick = {
              triggerHaptic(context)
              Toast.makeText(context, "Simulated AirDrop sent to iPad Pro 13\"!", Toast.LENGTH_SHORT).show()
            }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

// ==================== FIND MY DEMO ====================
@Composable
fun FindMyDemoScreen(
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
      title = "Find My",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(text = "Find My Simulation: Device location tracking demo on Realme C71 4G.")
      }

      // Map View Canvas
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E2836))
            .border(1.dp, Color(0xFF2C3E50), RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center
        ) {
          // Decorative simulated map grid
          Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 30.dp.toPx()
            var x = 0f
            while (x < size.width) {
              drawLine(Color(0xFF283747), Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
              x += step
            }
            var y = 0f
            while (y < size.height) {
              drawLine(Color(0xFF283747), Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
              y += step
            }
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(IosGreen.copy(alpha = 0.25f))
                .border(2.dp, IosGreen, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Pin",
                tint = IosGreen,
                modifier = Modifier.size(28.dp)
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = telemetry.deviceName,
              style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Text(
              text = "Location: With You (Live)",
              style = MaterialTheme.typography.labelSmall,
              color = IosDarkTextSecondary
            )
          }
        }
      }

      item {
        IOSGroupedCard(headerText = "Actions for This Phone") {
          IOSSettingsRow(
            title = "Play Sound",
            subtitle = "Ping phone with audio & haptics",
            icon = Icons.Default.VolumeUp,
            iconBackground = IosGreen,
            onClick = {
              triggerHaptic(context)
              Toast.makeText(context, "Playing simulated Find My chime...", Toast.LENGTH_LONG).show()
            }
          )

          IOSSettingsRow(
            title = "Lost Mode",
            subtitle = "Display contact info if misplaced",
            icon = Icons.Default.Security,
            iconBackground = IosOrange,
            valueText = "Off",
            onClick = {
              Toast.makeText(context, "Lost mode is a demo option", Toast.LENGTH_SHORT).show()
            }
          )

          IOSSettingsRow(
            title = "Find My Network",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = true,
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

// ==================== ICLOUD DEMO ====================
@Composable
fun ICloudDemoScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val profile by viewModel.profile.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "iCloud",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(text = "iCloud Simulation: Offline cloud storage interface simulation.")
      }

      item {
        IOSGroupedCard(
          headerText = "Storage Breakdown",
          footerText = "iCloud+ 50 GB Subscription Plan demo."
        ) {
          IOSSettingsRow(
            title = "Total Storage",
            valueText = "${profile.icloudStorageUsedGb} GB of ${profile.icloudStorageTotalGb} GB Used"
          )

          IOSSettingsRow(
            title = "iCloud Photos",
            valueText = "8.2 GB",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "iCloud Drive",
            valueText = "4.1 GB",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Device Backups",
            valueText = "3.2 GB",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "iCloud Private Relay",
            subtitle = "Hide IP address in Safari demo",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = true,
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

// ==================== ALWAYS-ON DISPLAY DEMO ====================
@Composable
fun AlwaysOnDisplayDemoScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val settings by viewModel.appSettings.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Always-On Display",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(
          text = "Always-On Display Demo: Simulates iOS 26 low-power OLED lock screen standby."
        )
      }

      // Black OLED Preview Card
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .border(1.5.dp, Color(0xFF2C2C2E), RoundedCornerShape(20.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Friday, October 23",
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF8E8E93)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "09:41",
              style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp, fontWeight = FontWeight.Bold),
              color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row {
              WidgetPill(label = "85% Battery ⚡")
              Spacer(modifier = Modifier.width(8.dp))
              WidgetPill(label = "24° Sunny")
            }
          }
        }
      }

      item {
        IOSGroupedCard(
          headerText = "Standby Options",
          footerText = "When your phone is locked and upright, Always-On Display keeps vital widgets dimmed."
        ) {
          IOSSettingsRow(
            title = "Always-On Display",
            trailing = IOSTrailingContent.Switch(
              checked = settings.alwaysOnDisplayEnabled,
              onCheckedChange = { viewModel.toggleAlwaysOnDisplay(it) }
            )
          )

          IOSSettingsRow(
            title = "Show Wallpaper",
            trailing = IOSTrailingContent.Switch(
              checked = false,
              onCheckedChange = {}
            )
          )

          IOSSettingsRow(
            title = "Show Notifications",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = true,
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

@Composable
private fun WidgetPill(label: String) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF1C1C1E))
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = Color(0xFF8E8E93)
    )
  }
}

// ==================== GOOGLE SERVICES SCREEN ====================
@Composable
fun GoogleServicesScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Google Account",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(
          text = "Real Android Account Integration: Access genuine Google accounts, sync, and Play services on your Realme C71 4G. We never request or store your Google credentials."
        )
      }

      item {
        IOSGroupedCard(
          headerText = "Google Android Sync",
          footerText = "Open official Android Account settings to manage Google Play, Gmail, Contacts, and Cloud Drive synchronization."
        ) {
          IOSSettingsRow(
            title = "Open Android Google Account Settings",
            icon = Icons.Default.Sync,
            iconBackground = Color(0xFF4285F4),
            onClick = { AndroidSettingsBridge.openGoogleAccountSettings(context) }
          )

          IOSSettingsRow(
            title = "Account Sync Status",
            subtitle = "Android 16 Google Play Services",
            icon = Icons.Default.Sync,
            iconBackground = IosGreen,
            valueText = "Active",
            onClick = { AndroidSettingsBridge.openGoogleAccountSettings(context) }
          )

          IOSSettingsRow(
            title = "Security & Sign-In",
            subtitle = "Manage 2-Step Verification in Google",
            icon = Icons.Default.Security,
            iconBackground = IosBlue,
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openGoogleAccountSettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

// ==================== PRIVACY & SECURITY SCREEN ====================
@Composable
fun PrivacyAndSecurityScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Privacy & Security",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        IOSGroupedCard(
          headerText = "Android 16 Privacy Hub",
          footerText = "Tap to open the official Android 16 Privacy Dashboard to review Camera, Microphone, and Location access history."
        ) {
          IOSSettingsRow(
            title = "Open Android Privacy Settings",
            icon = Icons.Default.Security,
            iconBackground = IosBlue,
            onClick = { AndroidSettingsBridge.openSecuritySettings(context) }
          )

          IOSSettingsRow(
            title = "Location Services",
            valueText = "While Using",
            onClick = { AndroidSettingsBridge.openSecuritySettings(context) }
          )

          IOSSettingsRow(
            title = "Microphone & Camera Permissions",
            valueText = "Android Controlled",
            showSeparator = false,
            onClick = { AndroidSettingsBridge.openSecuritySettings(context) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

// ==================== COMMON DISCLAIMER BADGE ====================
@Composable
fun DemoDisclaimerBadge(text: String) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF1C1C1E))
      .border(0.5.dp, IosBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
      .padding(12.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Notice",
        tint = IosBlue,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
        color = IosDarkTextSecondary
      )
    }
  }
}

private fun triggerHaptic(context: Context) {
  try {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
    }
  } catch (_: Exception) {}
}
