package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IOSTopAppBar(
  title: String,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  backText: String = "Settings",
  trailingAction: (@Composable () -> Unit)? = null
) {
  val interactionSource = remember { MutableInteractionSource() }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(52.dp)
      .padding(horizontal = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    // Left Back Button
    Row(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .clickable(
          interactionSource = interactionSource,
          indication = null,
          onClick = onBackClick
        )
        .testTag("top_app_bar_back_button")
        .padding(horizontal = 8.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
        contentDescription = "Back",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = backText,
        style = MaterialTheme.typography.bodyLarge.copy(
          fontSize = 17.sp,
          color = MaterialTheme.colorScheme.primary
        )
      )
    }

    // Centered Title
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
      ),
      color = MaterialTheme.colorScheme.onSurface,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(horizontal = 90.dp)
        .testTag("top_app_bar_title")
    )

    // Right Action
    if (trailingAction != null) {
      Box(
        modifier = Modifier.align(Alignment.CenterEnd)
      ) {
        trailingAction()
      }
    }
  }
}
