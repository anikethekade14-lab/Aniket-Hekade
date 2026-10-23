package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IosDarkCardBorder
import com.example.ui.theme.IosDarkGroupBackground
import com.example.ui.theme.IosDarkTextSecondary

@Composable
fun IOSGroupedCard(
  modifier: Modifier = Modifier,
  headerText: String? = null,
  footerText: String? = null,
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    if (!headerText.isNullOrBlank()) {
      Text(
        text = headerText.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 12.sp,
          letterSpacing = 0.5.sp
        ),
        color = IosDarkTextSecondary,
        modifier = Modifier.padding(start = 16.dp, bottom = 6.dp, top = 6.dp)
      )
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(MaterialTheme.colorScheme.surface)
        .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
    ) {
      content()
    }

    if (!footerText.isNullOrBlank()) {
      Text(
        text = footerText,
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 12.sp,
          lineHeight = 16.sp
        ),
        color = IosDarkTextSecondary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 4.dp)
      )
    } else {
      Spacer(modifier = Modifier.height(6.dp))
    }
  }
}
