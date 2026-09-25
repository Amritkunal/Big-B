package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

@Composable
fun AppLogoBadge(
  modifier: Modifier = Modifier,
  size: Dp = 34.dp
) {
  val shape = RoundedCornerShape(9.dp)

  Box(
    modifier = modifier
      .size(size)
      .clip(shape)
      .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), shape)
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .testTag("app_logo_badge"),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.app_icon),
      contentDescription = "App Logo",
      modifier = Modifier
        .size(size)
        .clip(shape),
      contentScale = ContentScale.Fit
    )
  }
}
