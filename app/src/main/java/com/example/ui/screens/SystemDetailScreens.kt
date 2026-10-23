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
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.IosRed
import com.example.viewmodel.SettingsViewModel

// ==================== NOTIFICATIONS SCREEN ====================
@Composable
fun NotificationsDetailScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  var selectedDisplay by remember { mutableStateOf("Stack") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Notifications",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        IOSGroupedCard(
          headerText = "Display As",
          footerText = "Choose how notifications are presented on the lock screen."
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            NotificationDisplayCard(title = "Count", isSelected = selectedDisplay == "Count") { selectedDisplay = "Count" }
            NotificationDisplayCard(title = "Stack", isSelected = selectedDisplay == "Stack") { selectedDisplay = "Stack" }
            NotificationDisplayCard(title = "List", isSelected = selectedDisplay == "List") { selectedDisplay = "List" }
          }
        }
      }

      item {
        IOSGroupedCard(
          headerText = "Real Android 16 Notifications",
          footerText = "Configure notification channels, lock screen privacy, and Do Not Disturb overrides in Android."
        ) {
          IOSSettingsRow(
            title = "Open Android Notification Settings",
            icon = Icons.Default.Notifications,
            iconBackground = IosRed,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openNotificationSettings(context) }
          )
        }
      }

      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "Scheduled Summary",
            valueText = "Off",
            onClick = {}
          )
          IOSSettingsRow(
            title = "Show Previews",
            valueText = "When Unlocked",
            onClick = {}
          )
          IOSSettingsRow(
            title = "Screen Sharing",
            subtitle = "Allow notifications while sharing",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(checked = false, onCheckedChange = {})
          )
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}

@Composable
private fun NotificationDisplayCard(title: String, isSelected: Boolean, onClick: () -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .width(72.dp)
        .height(60.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color(0xFF1C1C1E))
        .border(
          width = if (isSelected) 2.dp else 1.dp,
          color = if (isSelected) IosBlue else Color(0xFF38383A),
          shape = RoundedCornerShape(10.dp)
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
        color = if (isSelected) IosBlue else IosDarkTextSecondary
      )
    }
  }
}

// ==================== ACCESSIBILITY SCREEN ====================
@Composable
fun AccessibilityDetailScreen(
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
      title = "Accessibility",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        IOSGroupedCard(
          headerText = "Official Android Accessibility",
          footerText = "Access TalkBack, font scaling, display contrast, and hearing aid pairing in Android 16."
        ) {
          IOSSettingsRow(
            title = "Open Android Accessibility Settings",
            icon = Icons.Default.Accessibility,
            iconBackground = IosBlue,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openAccessibilitySettings(context) }
          )
        }
      }

      item {
        IOSGroupedCard(headerText = "Vision") {
          IOSSettingsRow(title = "VoiceOver", valueText = "Off")
          IOSSettingsRow(title = "Zoom", valueText = "Off")
          IOSSettingsRow(title = "Display & Text Size", valueText = "Default")
          IOSSettingsRow(title = "Motion", subtitle = "Reduce motion & transition speed", showSeparator = false, valueText = "Off")
        }
      }

      item {
        IOSGroupedCard(headerText = "Physical and Motor") {
          IOSSettingsRow(title = "Touch", subtitle = "AssistiveTouch and Haptic Touch", valueText = "On")
          IOSSettingsRow(title = "Face ID & Attention", showSeparator = false, valueText = "On")
        }
      }

      item {
        IOSGroupedCard(headerText = "Hearing") {
          IOSSettingsRow(title = "Hearing Devices", subtitle = "Bluetooth audio routing", valueText = "Not Paired")
          IOSSettingsRow(title = "Subtitles & Captioning", showSeparator = false, valueText = "Off")
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}

// ==================== FOCUS SCREEN ====================
@Composable
fun FocusDetailScreen(
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
      title = "Focus",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(text = "Focus Mode Preview: Tailor notification delivery and silences on Realme C71 4G.")
      }

      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "Do Not Disturb",
            icon = Icons.Default.Bedtime,
            iconBackground = IosPurple,
            valueText = "Off"
          )
          IOSSettingsRow(
            title = "Personal",
            icon = Icons.Default.Person,
            iconBackground = IosPurple,
            valueText = "Set Up"
          )
          IOSSettingsRow(
            title = "Sleep",
            icon = Icons.Default.Bedtime,
            iconBackground = IosCyan,
            valueText = "Set Up"
          )
          IOSSettingsRow(
            title = "Work",
            icon = Icons.Default.Work,
            iconBackground = IosOrange,
            showSeparator = false,
            valueText = "Set Up"
          )
        }
      }

      item {
        IOSGroupedCard(
          footerText = "Turn on a Focus automatically at a set time, location, or while using a certain app."
        ) {
          IOSSettingsRow(
            title = "Share Across Devices",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(checked = true, onCheckedChange = {})
          )
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}

// ==================== SCREEN TIME SCREEN ====================
@Composable
fun ScreenTimeDetailScreen(
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
      title = "Screen Time",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Usage Chart Card
      item {
        IOSGroupedCard(headerText = "Daily Average") {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Text(
              text = "4h 12m",
              style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "↓ 14% from last week",
              style = MaterialTheme.typography.labelSmall,
              color = IosGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Bar Chart
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              val heights = listOf(0.45f, 0.70f, 0.60f, 0.85f, 0.50f, 0.75f, 0.35f)
              val days = listOf("M", "T", "W", "T", "F", "S", "S")

              days.forEachIndexed { i, day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Box(
                    modifier = Modifier
                      .width(18.dp)
                      .height((70 * heights[i]).dp)
                      .clip(RoundedCornerShape(4.dp))
                      .background(if (i == 3) IosCyan else IosBlue)
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(text = day, style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), color = IosDarkTextSecondary)
                }
              }
            }
          }
        }
      }

      item {
        IOSGroupedCard {
          IOSSettingsRow(title = "Downtime", subtitle = "Schedule time away from the screen", icon = Icons.Default.HourglassBottom, iconBackground = IosIndigo)
          IOSSettingsRow(title = "App Limits", subtitle = "Set time limits for apps", icon = Icons.Default.HourglassBottom, iconBackground = IosOrange)
          IOSSettingsRow(title = "Always Allowed", subtitle = "Choose apps allowed during downtime", icon = Icons.Default.Check, iconBackground = IosGreen)
          IOSSettingsRow(title = "Content & Privacy Restrictions", icon = Icons.Default.Lock, iconBackground = IosRed, showSeparator = false)
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}

// ==================== WALLPAPER SCREEN ====================
@Composable
fun WallpaperDetailScreen(
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
      title = "Wallpaper",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(text = "iOS 26 Wallpaper Simulation: Preview Lock and Home screen wallpapers.")
      }

      // Wallpaper Preview Side by Side
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.Center
        ) {
          // Lock Screen
          WallpaperPhoneFrame(label = "Lock Screen", gradient = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)))
          Spacer(modifier = Modifier.width(20.dp))
          // Home Screen
          WallpaperPhoneFrame(label = "Home Screen", gradient = listOf(Color(0xFF141E30), Color(0xFF243B55)))
        }
      }

      item {
        IOSGroupedCard(
          headerText = "Real Android 16 Wallpaper",
          footerText = "Open official Android 16 wallpaper and style settings to customize themes on your Realme C71 4G."
        ) {
          IOSSettingsRow(
            title = "Open Android Wallpaper Settings",
            icon = Icons.Default.Wallpaper,
            iconBackground = IosCyan,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openWallpaperSettings(context) }
          )
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}

@Composable
private fun WallpaperPhoneFrame(label: String, gradient: List<Color>) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      modifier = Modifier
        .width(110.dp)
        .height(180.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(Brush.verticalGradient(gradient))
        .border(1.5.dp, Color(0xFF38383A), RoundedCornerShape(18.dp)),
      contentAlignment = Alignment.TopCenter
    ) {
      Text(
        text = "09:41",
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
        color = Color.White,
        modifier = Modifier.padding(top = 16.dp)
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = IosDarkTextSecondary)
  }
}

// ==================== CONTROL CENTER SCREEN ====================
@Composable
fun ControlCenterDetailScreen(
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
      title = "Control Center",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        DemoDisclaimerBadge(text = "Control Center Customization: Shortcuts configured for iOS 26.")
      }

      item {
        IOSGroupedCard(headerText = "Included Controls") {
          IOSSettingsRow(title = "Flashlight", icon = Icons.Default.FlashlightOn, iconBackground = IosBlue)
          IOSSettingsRow(title = "Timer", icon = Icons.Default.Timer, iconBackground = IosOrange)
          IOSSettingsRow(title = "Calculator", icon = Icons.Default.Calculate, iconBackground = IosOrange)
          IOSSettingsRow(title = "Camera", icon = Icons.Default.CameraAlt, iconBackground = Color(0xFF8E8E93), showSeparator = false)
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}

// ==================== APPS DETAIL SCREEN ====================
@Composable
fun AppsDetailScreen(
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
      title = "Apps",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      item {
        IOSGroupedCard(
          headerText = "Android 16 App Management",
          footerText = "View all installed Realme applications, modify app permissions, clear caches, or configure default apps."
        ) {
          IOSSettingsRow(
            title = "Open Android Applications Settings",
            icon = Icons.Default.Apps,
            iconBackground = IosBlue,
            showSeparator = false,
            trailing = IOSTrailingContent.Chevron,
            onClick = { AndroidSettingsBridge.openApplicationSettings(context) }
          )
        }
      }

      item {
        IOSGroupedCard(headerText = "Built-in Applications") {
          IOSSettingsRow(title = "Camera", subtitle = "4G 50MP AI Camera on Realme C71", icon = Icons.Default.CameraAlt, iconBackground = Color(0xFF8E8E93))
          IOSSettingsRow(title = "Safari (Demo)", subtitle = "iOS Web Engine simulation", icon = Icons.Default.Smartphone, iconBackground = IosBlue)
          IOSSettingsRow(title = "Messages", subtitle = "SMS & RCS Messaging", icon = Icons.Default.Notifications, iconBackground = IosGreen, showSeparator = false)
        }
      }

      item { Spacer(modifier = Modifier.height(24.dp)) }
    }
  }
}
