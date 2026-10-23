package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IosDarkSeparator
import com.example.ui.theme.IosDarkTextSecondary

sealed class IOSTrailingContent {
  object Chevron : IOSTrailingContent()
  object None : IOSTrailingContent()
  data class Switch(val checked: Boolean, val onCheckedChange: (Boolean) -> Unit) : IOSTrailingContent()
  data class Badge(val count: String, val badgeColor: Color = Color(0xFFFF3B30)) : IOSTrailingContent()
}

@Composable
fun IOSSettingsRow(
  title: String,
  modifier: Modifier = Modifier,
  subtitle: String? = null,
  valueText: String? = null,
  icon: ImageVector? = null,
  iconBackground: Color = MaterialTheme.colorScheme.primary,
  iconBrush: Brush? = null,
  iconTint: Color = Color.White,
  trailing: IOSTrailingContent = IOSTrailingContent.Chevron,
  showSeparator: Boolean = true,
  testTag: String = "setting_row_${title.lowercase().replace(" ", "_")}",
  onClick: (() -> Unit)? = null
) {
  val clickableModifier = if (onClick != null) {
    Modifier.clickable(onClick = onClick)
  } else {
    Modifier
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
      .then(clickableModifier)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 11.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (icon != null) {
        val bgModifier = if (iconBrush != null) {
          Modifier.background(iconBrush)
        } else {
          Modifier.background(iconBackground)
        }

        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(bgModifier),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(19.dp)
          )
        }

        Spacer(modifier = Modifier.width(14.dp))
      }

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        if (!subtitle.isNullOrBlank()) {
          Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = IosDarkTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      if (!valueText.isNullOrBlank()) {
        Text(
          text = valueText,
          style = MaterialTheme.typography.bodyMedium,
          color = IosDarkTextSecondary,
          modifier = Modifier.padding(end = 6.dp),
          maxLines = 1
        )
      }

      when (trailing) {
        is IOSTrailingContent.Chevron -> {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = Color(0xFF636366),
            modifier = Modifier.size(13.dp)
          )
        }
        is IOSTrailingContent.Switch -> {
          IOSSwitch(
            checked = trailing.checked,
            onCheckedChange = trailing.onCheckedChange,
            testTag = "${testTag}_switch"
          )
        }
        is IOSTrailingContent.Badge -> {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(trailing.badgeColor)
              .padding(horizontal = 7.dp, vertical = 2.dp)
          ) {
            Text(
              text = trailing.count,
              style = MaterialTheme.typography.labelSmall,
              color = Color.White
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = Color(0xFF636366),
            modifier = Modifier.size(13.dp)
          )
        }
        is IOSTrailingContent.None -> {}
      }
    }

    if (showSeparator) {
      val insetStart = if (icon != null) 60.dp else 16.dp
      HorizontalDivider(
        modifier = Modifier.padding(start = insetStart),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant
      )
    }
  }
}
