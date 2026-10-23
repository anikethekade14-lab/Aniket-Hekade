package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.IosGreen

@Composable
fun IOSSwitch(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  activeColor: Color = IosGreen,
  inactiveColor: Color = Color(0xFF39393D),
  testTag: String = "ios_switch"
) {
  val context = LocalContext.current
  val interactionSource = remember { MutableInteractionSource() }

  val trackColor by animateColorAsState(
    targetValue = if (checked) activeColor else inactiveColor,
    animationSpec = spring(stiffness = 500f),
    label = "trackColor"
  )

  val thumbOffset by animateDpAsState(
    targetValue = if (checked) 20.dp else 0.dp,
    animationSpec = spring(dampingRatio = 0.8f, stiffness = 600f),
    label = "thumbOffset"
  )

  Box(
    modifier = modifier
      .testTag(testTag)
      .width(51.dp)
      .height(31.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(trackColor)
      .clickable(
        interactionSource = interactionSource,
        indication = null
      ) {
        // Trigger soft haptic feedback
        try {
          val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE))
          }
        } catch (_: Exception) {}
        onCheckedChange(!checked)
      }
      .padding(2.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Box(
      modifier = Modifier
        .offset(x = thumbOffset)
        .size(27.dp)
        .shadow(3.dp, CircleShape)
        .clip(CircleShape)
        .background(Color.White)
    )
  }
}
