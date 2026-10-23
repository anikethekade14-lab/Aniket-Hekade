package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BiometricGate
import com.example.ui.screens.AboutPhoneScreen
import com.example.ui.screens.AirDropDemoScreen
import com.example.ui.screens.AlwaysOnDisplayDemoScreen
import com.example.ui.screens.AppleIntelligenceDemoScreen
import com.example.ui.screens.BatteryScreen
import com.example.ui.screens.ConnectivityScreen
import com.example.ui.screens.DisplayScreen
import com.example.ui.screens.FaceIdScreen
import com.example.ui.screens.FindMyDemoScreen
import com.example.ui.screens.GeneralScreen
import com.example.ui.screens.GoogleServicesScreen
import com.example.ui.screens.ICloudDemoScreen
import com.example.ui.screens.MainSettingsScreen
import com.example.ui.screens.PrivacyAndSecurityScreen
import com.example.ui.screens.ProfileDetailScreen
import com.example.ui.screens.SiriDemoScreen
import com.example.ui.screens.SoftwareUpdateScreen
import com.example.ui.screens.SoundsScreen
import com.example.ui.screens.StorageDetailScreen
import com.example.ui.screens.AccessibilityDetailScreen
import com.example.ui.screens.AppsDetailScreen
import com.example.ui.screens.ControlCenterDetailScreen
import com.example.ui.screens.FocusDetailScreen
import com.example.ui.screens.NotificationsDetailScreen
import com.example.ui.screens.ScreenTimeDetailScreen
import com.example.ui.screens.WallpaperDetailScreen
import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ui.theme.IOSSettingsTheme
import com.example.viewmodel.SettingsScreen
import com.example.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val context = LocalContext.current
      val application = context.applicationContext as Application
      val viewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(application) as T
          }
        }
      )
      val settings by viewModel.appSettings.collectAsState()
      val screenStack by viewModel.screenStack.collectAsState()
      val currentScreen = screenStack.lastOrNull() ?: SettingsScreen.Main

      IOSSettingsTheme(
        darkTheme = settings.isDarkMode,
        accentColor = viewModel.getAccentColor(settings.accentStyle)
      ) {
        BiometricGate(viewModel = viewModel) {
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.statusBars
          ) { innerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(WindowInsets.navigationBars.asPaddingValues())
            ) {
              Crossfade(
                targetState = currentScreen,
                animationSpec = tween(220),
                label = "screenTransition"
              ) { screen ->
                when (screen) {
                  is SettingsScreen.Main -> MainSettingsScreen(viewModel)
                  is SettingsScreen.ProfileDetail -> ProfileDetailScreen(viewModel)
                  is SettingsScreen.General -> GeneralScreen(viewModel)
                  is SettingsScreen.AboutPhone -> AboutPhoneScreen(viewModel)
                  is SettingsScreen.SoftwareUpdate -> SoftwareUpdateScreen(viewModel)
                  is SettingsScreen.StorageDetail -> StorageDetailScreen(viewModel)
                  is SettingsScreen.Connectivity -> ConnectivityScreen(viewModel)
                  is SettingsScreen.DisplayAndBrightness -> DisplayScreen(viewModel)
                  is SettingsScreen.SoundsAndHaptics -> SoundsScreen(viewModel)
                  is SettingsScreen.BatteryDetail -> BatteryScreen(viewModel)
                  is SettingsScreen.FaceIdAndPasscode -> FaceIdScreen(viewModel)
                  is SettingsScreen.PrivacyAndSecurity -> PrivacyAndSecurityScreen(viewModel)
                  is SettingsScreen.GoogleServices -> GoogleServicesScreen(viewModel)
                  is SettingsScreen.SiriDemo -> SiriDemoScreen(viewModel)
                  is SettingsScreen.AppleIntelligenceDemo -> AppleIntelligenceDemoScreen(viewModel)
                  is SettingsScreen.AirDropDemo -> AirDropDemoScreen(viewModel)
                  is SettingsScreen.FindMyDemo -> FindMyDemoScreen(viewModel)
                  is SettingsScreen.ICloudDemo -> ICloudDemoScreen(viewModel)
                  is SettingsScreen.AlwaysOnDisplayDemo -> AlwaysOnDisplayDemoScreen(viewModel)
                  is SettingsScreen.NotificationsDetail -> NotificationsDetailScreen(viewModel)
                  is SettingsScreen.AccessibilityDetail -> AccessibilityDetailScreen(viewModel)
                  is SettingsScreen.FocusDetail -> FocusDetailScreen(viewModel)
                  is SettingsScreen.ScreenTimeDetail -> ScreenTimeDetailScreen(viewModel)
                  is SettingsScreen.WallpaperDetail -> WallpaperDetailScreen(viewModel)
                  is SettingsScreen.ControlCenterDetail -> ControlCenterDetailScreen(viewModel)
                  is SettingsScreen.AppsDetail -> AppsDetailScreen(viewModel)
                }
              }
            }
          }
        }
      }
    }
  }
}
