package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = IosBlue,
  onPrimary = Color.White,
  primaryContainer = IosBlue.copy(alpha = 0.2f),
  onPrimaryContainer = Color.White,
  secondary = IosPurple,
  onSecondary = Color.White,
  background = IosDarkBackground,
  onBackground = IosDarkTextPrimary,
  surface = IosDarkGroupBackground,
  onSurface = IosDarkTextPrimary,
  surfaceVariant = IosDarkSearchBar,
  onSurfaceVariant = IosDarkTextSecondary,
  outline = IosDarkCardBorder,
  outlineVariant = IosDarkSeparator
)

private val LightColorScheme = lightColorScheme(
  primary = IosBlue,
  onPrimary = Color.White,
  primaryContainer = IosBlue.copy(alpha = 0.15f),
  onPrimaryContainer = IosBlue,
  secondary = IosPurple,
  onSecondary = Color.White,
  background = IosLightBackground,
  onBackground = IosLightTextPrimary,
  surface = IosLightGroupBackground,
  onSurface = IosLightTextPrimary,
  surfaceVariant = IosLightSearchBar,
  onSurfaceVariant = IosLightTextSecondary,
  outline = IosLightCardBorder,
  outlineVariant = IosLightSeparator
)

@Composable
fun IOSSettingsTheme(
  darkTheme: Boolean = true, // Default to iOS Dark Mode as requested
  accentColor: Color = IosBlue,
  content: @Composable () -> Unit
) {
  val baseScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val colorScheme = baseScheme.copy(
    primary = accentColor
  )

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
