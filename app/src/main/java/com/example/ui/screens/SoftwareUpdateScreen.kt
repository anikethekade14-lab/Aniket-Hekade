package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SoftwareUpdateScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var isChecking by remember { mutableStateOf(false) }
  var lastChecked by remember { mutableStateOf("Just now") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Software Update",
      backText = "General",
      onBackClick = { viewModel.navigateBack() }
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Demonstration Disclaimer
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF1C1C1E))
          .border(0.5.dp, IosBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Demo Notice",
            tint = IosBlue,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Demonstration Screen: Shows simulated iOS 26 preview status. To perform real Android 16 firmware updates on your Realme C71 4G, use the official Android settings below.",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
            color = IosDarkTextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Update Hero Status
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(IosGreen.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Up to date",
          tint = IosGreen,
          modifier = Modifier.size(42.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "iOS 26.1 Preview",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Your Realme C71 4G has the latest iOS 26 experience.",
        style = MaterialTheme.typography.bodyMedium,
        color = IosDarkTextSecondary
      )

      Text(
        text = "Checked: $lastChecked",
        style = MaterialTheme.typography.labelSmall,
        color = IosDarkTextSecondary.copy(alpha = 0.7f),
        modifier = Modifier.padding(top = 4.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Check Button
      Button(
        onClick = {
          if (!isChecking) {
            isChecking = true
            coroutineScope.launch {
              delay(1500)
              isChecking = false
              lastChecked = "Just now"
            }
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp)
      ) {
        if (isChecking) {
          CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 2.dp,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text("Checking for Updates...")
        } else {
          Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Check for Updates")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Real Android 16 Firmware Settings
      IOSGroupedCard(
        headerText = "Official Android System Updates",
        footerText = "Tap to open real Realme UI / Android system software update settings."
      ) {
        IOSSettingsRow(
          title = "Check Realme C71 Android Updates",
          showSeparator = false,
          trailing = IOSTrailingContent.Chevron,
          onClick = { AndroidSettingsBridge.openAboutDeviceSettings(context) }
        )
      }
    }
  }
}
