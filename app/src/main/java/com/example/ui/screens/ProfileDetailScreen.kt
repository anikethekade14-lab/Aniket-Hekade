package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TabletMac
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.IOSGroupedCard
import com.example.ui.components.IOSSettingsRow
import com.example.ui.components.IOSTopAppBar
import com.example.ui.theme.IosBlue
import com.example.ui.theme.IosCyan
import com.example.ui.theme.IosDarkCardBorder
import com.example.ui.theme.IosDarkTextSecondary
import com.example.ui.theme.IosGreen
import com.example.ui.theme.IosOrange
import com.example.ui.theme.IosPurple
import com.example.ui.theme.IosRed
import com.example.viewmodel.SettingsViewModel

@Composable
fun ProfileDetailScreen(
  viewModel: SettingsViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateBack() }

  val profile by viewModel.profile.collectAsState()
  var showEditDialog by remember { mutableStateOf(false) }
  val context = androidx.compose.ui.platform.LocalContext.current
  var editName by remember(profile.displayName) { mutableStateOf(profile.displayName) }
  var editEmail by remember(profile.email) { mutableStateOf(profile.email) }

  // Android Photo Picker Launcher with persistent internal storage copy
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      var savedUriString = uri.toString()
      try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val avatarFile = java.io.File(context.filesDir, "user_avatar.jpg")
        val outputStream = java.io.FileOutputStream(avatarFile)
        inputStream?.use { input ->
          outputStream.use { output ->
            input.copyTo(output)
          }
        }
        savedUriString = Uri.fromFile(avatarFile).toString()
      } catch (_: Throwable) {}

      viewModel.saveProfile(
        displayName = profile.displayName,
        email = profile.email,
        avatarUri = savedUriString
      )
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    IOSTopAppBar(
      title = "Apple Account",
      onBackClick = { viewModel.navigateBack() },
      trailingAction = {
        TextButton(
          onClick = { showEditDialog = true },
          modifier = Modifier.testTag("edit_profile_button")
        ) {
          Text("Edit", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }
      }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      // Simulation Notice Banner
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
              contentDescription = "Simulation Notice",
              tint = IosBlue,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Visual Apple ID Simulation: Offline profile for Realme C71 4G. No Apple servers or passwords are connected.",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
              color = IosDarkTextSecondary
            )
          }
        }
      }

      // Profile Avatar Header
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(92.dp)
              .clip(CircleShape)
              .clickable {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              }
              .testTag("profile_avatar_picker"),
            contentAlignment = Alignment.Center
          ) {
            if (!profile.avatarUri.isNullOrBlank()) {
              AsyncImage(
                model = profile.avatarUri,
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            } else {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(Brush.linearGradient(listOf(Color(0xFF8E8E93), Color(0xFF48484A)))),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = profile.displayName.take(1).uppercase(),
                  style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                  color = Color.White
                )
              }
            }

            // Camera badge
            Box(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(28.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Change photo",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = profile.displayName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = profile.email,
            style = MaterialTheme.typography.bodyMedium,
            color = IosDarkTextSecondary
          )
        }
      }

      // Account Options
      item {
        IOSGroupedCard {
          IOSSettingsRow(
            title = "Personal Information",
            icon = Icons.Default.Edit,
            iconBackground = IosBlue,
            valueText = profile.displayName,
            onClick = { showEditDialog = true }
          )

          IOSSettingsRow(
            title = "Sign-In & Security",
            icon = Icons.Default.Lock,
            iconBackground = IosGreen,
            valueText = "Two-Factor On (Simulated)",
            onClick = { showEditDialog = true }
          )

          IOSSettingsRow(
            title = "Payment & Shipping",
            icon = Icons.Default.CreditCard,
            iconBackground = IosOrange,
            valueText = "Apple Pay Demo",
            onClick = {}
          )

          IOSSettingsRow(
            title = "Media & Purchases",
            icon = Icons.Default.ShoppingBag,
            iconBackground = IosPurple,
            valueText = "Subscribed",
            showSeparator = false,
            onClick = {}
          )
        }
      }

      // iCloud Storage Section
      item {
        IOSGroupedCard(
          headerText = "iCloud Simulation",
          footerText = "${profile.icloudStorageUsedGb} GB used of ${profile.icloudStorageTotalGb} GB (iCloud+ 50 GB plan)"
        ) {
          IOSSettingsRow(
            title = "iCloud+",
            icon = Icons.Default.Cloud,
            iconBackground = IosBlue,
            valueText = "${profile.icloudStorageUsedGb} GB / ${profile.icloudStorageTotalGb} GB"
          )

          // Storage Bar
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF38383A))
            ) {
              // Photos (Yellow)
              Box(
                modifier = Modifier
                  .weight(0.4f)
                  .height(8.dp)
                  .background(Color(0xFFFFD60A))
              )
              // Backups (Blue)
              Box(
                modifier = Modifier
                  .weight(0.25f)
                  .height(8.dp)
                  .background(IosBlue)
              )
              // Docs (Green)
              Box(
                modifier = Modifier
                  .weight(0.15f)
                  .height(8.dp)
                  .background(IosGreen)
              )
              // Free
              Spacer(modifier = Modifier.weight(0.2f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              LegendDot(color = Color(0xFFFFD60A), label = "Photos")
              Spacer(modifier = Modifier.width(12.dp))
              LegendDot(color = IosBlue, label = "Backups")
              Spacer(modifier = Modifier.width(12.dp))
              LegendDot(color = IosGreen, label = "Documents")
              Spacer(modifier = Modifier.width(12.dp))
              LegendDot(color = Color(0xFF38383A), label = "Free")
            }
          }
        }
      }

      // Linked Devices Section
      item {
        IOSGroupedCard(headerText = "Devices") {
          // Realme C71 4G
          IOSSettingsRow(
            title = "Realme C71 4G",
            subtitle = "This phone • Android 16",
            icon = Icons.Default.PhoneAndroid,
            iconBackground = IosGreen,
            valueText = "Online",
            onClick = {}
          )

          // MacBook Pro
          IOSSettingsRow(
            title = "MacBook Pro 16\"",
            subtitle = "macOS Tahoe 26.2",
            icon = Icons.Default.LaptopMac,
            iconBackground = Color(0xFF8E8E93),
            onClick = {}
          )

          // iPad Pro
          IOSSettingsRow(
            title = "iPad Pro 13\"",
            subtitle = "iPadOS 26.1",
            icon = Icons.Default.TabletMac,
            iconBackground = Color(0xFF8E8E93),
            showSeparator = false,
            onClick = {}
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }

  // Edit Profile Dialog
  if (showEditDialog) {
    AlertDialog(
      onDismissRequest = { showEditDialog = false },
      title = {
        Text("Edit Simulated Profile", style = MaterialTheme.typography.titleLarge)
      },
      text = {
        Column {
          Text(
            text = "Personalize your display name and email address. Changes are stored safely in local database.",
            style = MaterialTheme.typography.bodyMedium,
            color = IosDarkTextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
          )

          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("Display Name") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_profile_name_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = editEmail,
            onValueChange = { editEmail = it },
            label = { Text("Apple ID-Style Email") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("edit_profile_email_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.saveProfile(
              displayName = editName.ifBlank { "User" },
              email = editEmail.ifBlank { "user@icloud.com" },
              avatarUri = profile.avatarUri
            )
            showEditDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier.testTag("save_profile_button")
        ) {
          Text("Save Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditDialog = false }) {
          Text("Cancel", color = IosDarkTextSecondary)
        }
      },
      containerColor = MaterialTheme.colorScheme.surface
    )
  }
}

@Composable
private fun LegendDot(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(6.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
      color = IosDarkTextSecondary
    )
  }
}
