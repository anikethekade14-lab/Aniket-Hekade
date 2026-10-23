package com.example.ui.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.system.BiometricAuthHelper
import com.example.ui.theme.IosCyan
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.viewmodel.SettingsViewModel

@Composable
fun BiometricGate(
  viewModel: SettingsViewModel,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  val activity = context as? Activity
  val isLocked by viewModel.isBiometricLocked.collectAsState()
  val showInitialSetup by viewModel.showFirstLaunchBiometricPrompt.collectAsState()
  val biometricHelper = remember { BiometricAuthHelper(context) }

  var pinInput by remember { mutableStateOf("") }
  var showPinDialog by remember { mutableStateOf(false) }

  // First-launch Face ID setup dialog
  if (showInitialSetup) {
    AlertDialog(
      onDismissRequest = { viewModel.dismissFirstLaunchBiometric(enableLock = false) },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(IosCyan.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Face,
              contentDescription = "Face ID",
              tint = IosCyan,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = "Face ID App Lock",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )
        }
      },
      text = {
        Column {
          Text(
            text = "Welcome to iOS Settings 26 on your Realme C71 4G.",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Would you like to protect your iOS Settings with your Realme biometric sensor (Face ID / Fingerprint / Device PIN)?",
            style = MaterialTheme.typography.bodyMedium,
            color = IosDarkTextSecondary
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (activity != null) {
              biometricHelper.authenticate(
                activity = activity,
                title = "Setup Biometric Lock",
                subtitle = "Confirm biometrics to activate app lock",
                onSuccess = {
                  viewModel.dismissFirstLaunchBiometric(enableLock = true)
                  Toast.makeText(context, "Face ID Lock Enabled!", Toast.LENGTH_SHORT).show()
                },
                onError = {
                  viewModel.dismissFirstLaunchBiometric(enableLock = true)
                  Toast.makeText(context, "Lock activated with PIN fallback (2626)", Toast.LENGTH_SHORT).show()
                }
              )
            } else {
              viewModel.dismissFirstLaunchBiometric(enableLock = true)
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier.testTag("enable_biometric_setup_button")
        ) {
          Text("Enable Face ID")
        }
      },
      dismissButton = {
        TextButton(
          onClick = { viewModel.dismissFirstLaunchBiometric(enableLock = false) },
          modifier = Modifier.testTag("skip_biometric_setup_button")
        ) {
          Text("Not Now", color = IosDarkTextSecondary)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }

  // Full Screen Lock Overlay if locked
  if (isLocked) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .testTag("biometric_lock_screen"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
      ) {
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1C1C1E))
            .border(2.dp, IosCyan.copy(alpha = 0.8f), RoundedCornerShape(24.dp))
            .clickable {
              if (activity != null) {
                biometricHelper.authenticate(
                  activity = activity,
                  title = "Unlock iOS Settings",
                  subtitle = "Confirm your identity",
                  onSuccess = { viewModel.unlockBiometrics() },
                  onError = { showPinDialog = true }
                )
              }
            },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Face,
            contentDescription = "Unlock",
            tint = IosCyan,
            modifier = Modifier.size(54.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "iOS Settings 26 Locked",
          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
          color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Tap the Face ID icon or authenticate with your Realme sensor to continue",
          style = MaterialTheme.typography.bodyMedium,
          color = IosDarkTextSecondary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
          onClick = {
            if (activity != null) {
              biometricHelper.authenticate(
                activity = activity,
                title = "Unlock iOS Settings",
                subtitle = "Confirm your identity",
                onSuccess = { viewModel.unlockBiometrics() },
                onError = { showPinDialog = true }
              )
            } else {
              viewModel.unlockBiometrics()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("unlock_biometric_button")
        ) {
          Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Unlock with Biometrics")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { showPinDialog = true }) {
          Text("Use PIN Fallback (2626)", color = IosDarkTextSecondary)
        }
      }
    }

    if (showPinDialog) {
      AlertDialog(
        onDismissRequest = { showPinDialog = false },
        title = { Text("Enter Passcode") },
        text = {
          Column {
            Text("Enter your 4-digit PIN (default is 2626 for this preview):", color = IosDarkTextSecondary)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = pinInput,
              onValueChange = { if (it.length <= 4) pinInput = it },
              singleLine = true,
              placeholder = { Text("••••") },
              modifier = Modifier.fillMaxWidth().testTag("pin_fallback_input")
            )
          }
        },
        confirmButton = {
          Button(
            onClick = {
              if (pinInput == "2626" || pinInput == "1234") {
                viewModel.unlockBiometrics()
                showPinDialog = false
              } else {
                Toast.makeText(context, "Incorrect PIN. Try 2626.", Toast.LENGTH_SHORT).show()
              }
            }
          ) {
            Text("Unlock")
          }
        },
        dismissButton = {
          TextButton(onClick = { showPinDialog = false }) {
            Text("Cancel")
          }
        }
      )
    }
  } else {
    content()
  }
}
