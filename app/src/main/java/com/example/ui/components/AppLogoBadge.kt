package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.R
import java.io.File

@Composable
fun AppLogoBadge(
  modifier: Modifier = Modifier,
  size: Dp = 34.dp,
  cacheBuster: Long = 0L,
  onClick: (() -> Unit)? = null
) {
  val context = LocalContext.current
  val customLogoFile = File(context.filesDir, "custom_app_logo.png")
  val hasCustomLogo = customLogoFile.exists()

  val shape = RoundedCornerShape(9.dp)

  Box(
    modifier = modifier
      .size(size)
      .clip(shape)
      .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), shape)
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
      .testTag("app_logo_badge"),
    contentAlignment = Alignment.Center
  ) {
    if (hasCustomLogo) {
      AsyncImage(
        model = "$customLogoFile?t=$cacheBuster",
        contentDescription = "App Main Logo",
        modifier = Modifier
          .size(size)
          .clip(shape),
        contentScale = ContentScale.Crop
      )
    } else {
      Image(
        painter = painterResource(id = R.drawable.app_icon),
        contentDescription = "Big B Logo",
        modifier = Modifier
          .size(size)
          .clip(shape),
        contentScale = ContentScale.Fit
      )
    }
  }
}
