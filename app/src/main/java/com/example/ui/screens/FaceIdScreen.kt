package com.example.ui.screens

import android.app.Activity
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.system.BiometricAuthHelper
import com.example.ui.components.IOSGroupedCard
import com.example.ui.components.IOSSettingsRow
import com.example.ui.components.IOSTopAppBar
import com.example.ui.components.IOSTrailingContent
import com.example.ui.theme.IosBlue
import com.example.ui.theme.IosCyan
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosIndigo
import com.example.viewmodel.SettingsViewModel

@Composable
fun FaceIdScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }
  val context = LocalContext.current
  val activity = context as? Activity
  val settings by viewModel.appSettings.collectAsState()
  val biometricHelper = remember { BiometricAuthHelper(context) }
  var isScanningDemo by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "scanning")
  val scanLineProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scanLine"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Face ID & Passcode",
      onBackClick = { viewModel.navigateBack() }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Transparency Notice
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
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
              text = "Biometric Security: Backed by Android 16 BiometricPrompt. Facial recognition or fingerprints are verified entirely on-device by Realme hardware; no facial images are recorded or stored.",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
              color = IosDarkTextSecondary
            )
          }
        }
      }

      // Animated Face Scan Reticle
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(130.dp)
              .clip(RoundedCornerShape(32.dp))
              .background(Color(0xFF1C1C1E))
              .border(2.dp, IosCyan.copy(alpha = 0.6f), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
          ) {
            // Animated Scanning Line
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
              val y = size.height * scanLineProgress
              drawLine(
                color = IosCyan,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 3.dp.toPx()
              )
            }

            Icon(
              imageVector = Icons.Default.Face,
              contentDescription = "Face ID Reticle",
              tint = IosCyan,
              modifier = Modifier.size(76.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = if (settings.biometricAppLockEnabled) "Face ID App Lock Active" else "Face ID Setup",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )

          Text(
            text = "Authenticate with your Realme C71 biometric sensors",
            style = MaterialTheme.typography.bodyMedium,
            color = IosDarkTextSecondary,
            modifier = Modifier.padding(top = 4.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Test Authenticate Button
          Button(
            onClick = {
              if (activity != null) {
                biometricHelper.authenticate(
                  activity = activity,
                  title = "Face ID Verification",
                  subtitle = "Verify identity to confirm biometric credentials",
                  onSuccess = {
                    Toast.makeText(context, "Face ID Authenticated Successfully!", Toast.LENGTH_SHORT).show()
                  },
                  onError = { err ->
                    Toast.makeText(context, "Authentication notice: $err", Toast.LENGTH_SHORT).show()
                  }
                )
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .padding(horizontal = 32.dp)
              .height(44.dp)
              .testTag("test_face_id_button")
          ) {
            Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Test Biometric Sensor")
          }
        }
      }

      // App Lock Controls
      item {
        IOSGroupedCard(
          headerText = "Use Face ID For",
          footerText = "When enabled, returning to iOS Settings will require biometric confirmation."
        ) {
          IOSSettingsRow(
            title = "Unlock iOS Settings App",
            trailing = IOSTrailingContent.Switch(
              checked = settings.biometricAppLockEnabled,
              onCheckedChange = { enable ->
                if (enable && activity != null) {
                  biometricHelper.authenticate(
                    activity = activity,
                    title = "Enable App Lock",
                    subtitle = "Confirm biometrics to protect iOS Settings",
                    onSuccess = {
                      viewModel.toggleBiometricAppLock(true)
                      Toast.makeText(context, "App lock enabled", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                      // Fallback: enable with PIN fallback
                      viewModel.toggleBiometricAppLock(true)
                    }
                  )
                } else {
                  viewModel.toggleBiometricAppLock(false)
                }
              }
            )
          )

          IOSSettingsRow(
            title = "Protected Private Screens",
            subtitle = "Apple ID profile & security settings",
            valueText = "Protected",
            trailing = IOSTrailingContent.None
          )

          IOSSettingsRow(
            title = "Require Attention for Face ID",
            showSeparator = false,
            trailing = IOSTrailingContent.Switch(
              checked = true,
              onCheckedChange = {}
            )
          )
        }
      }

      // Passcode section
      item {
        IOSGroupedCard(headerText = "Passcode Fallback") {
          IOSSettingsRow(
            title = "Passcode",
            valueText = "4-Digit PIN (${settings.lockPasscode.replace(Regex("."), "•")})",
            onClick = {
              Toast.makeText(context, "Passcode is ${settings.lockPasscode} for this demo", Toast.LENGTH_LONG).show()
            }
          )

          IOSSettingsRow(
            title = "Require Passcode",
            valueText = "Immediately",
            showSeparator = false,
            onClick = {}
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
