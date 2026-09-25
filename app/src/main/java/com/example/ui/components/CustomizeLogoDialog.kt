package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import java.io.File
import java.io.FileOutputStream

@Composable
fun CustomizeLogoDialog(
  customLogoFile: File?,
  onLogoUpdated: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var hasCustomLogo by remember { mutableStateOf(customLogoFile != null && customLogoFile.exists()) }
  var cacheBuster by remember { mutableStateOf(System.currentTimeMillis()) }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      try {
        val targetFile = File(context.filesDir, "custom_app_logo.png")
        context.contentResolver.openInputStream(uri)?.use { input ->
          FileOutputStream(targetFile).use { output ->
            input.copyTo(output)
          }
        }
        hasCustomLogo = true
        cacheBuster = System.currentTimeMillis()
        onLogoUpdated()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("customize_logo_dialog"),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          painter = painterResource(id = R.drawable.app_icon),
          contentDescription = null,
          modifier = Modifier.size(28.dp),
          tint = androidx.compose.ui.graphics.Color.Unspecified
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "App Main Logo",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Logo Preview Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          modifier = Modifier.padding(top = 4.dp)
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(22.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface),
              contentAlignment = Alignment.Center
            ) {
              val currentFile = File(context.filesDir, "custom_app_logo.png")
              if (hasCustomLogo && currentFile.exists()) {
                AsyncImage(
                  model = "$currentFile?t=$cacheBuster",
                  contentDescription = "Custom Main Logo",
                  modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(22.dp)),
                  contentScale = ContentScale.Crop
                )
              } else {
                Image(
                  painter = painterResource(id = R.drawable.app_icon),
                  contentDescription = "Project App Logo",
                  modifier = Modifier.size(76.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = CircleShape,
              color = if (hasCustomLogo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            ) {
              Text(
                text = if (hasCustomLogo) "Active: Selected Storage Image" else "Active: Project Logo (app_icon.png)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (hasCustomLogo) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }

        // Action Buttons
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("btn_pick_custom_logo")
          ) {
            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Select Image Manually")
          }

          if (hasCustomLogo) {
            OutlinedButton(
              onClick = {
                val file = File(context.filesDir, "custom_app_logo.png")
                if (file.exists()) {
                  file.delete()
                }
                hasCustomLogo = false
                cacheBuster = System.currentTimeMillis()
                onLogoUpdated()
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_reset_default_logo"),
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
              )
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Reset to Project Logo (app_icon.png)")
            }
          }
        }

        // Developer instructions card for file tree
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Code,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Manual Code / Project File Paths",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "You can also place your own PNG directly in the project files:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.surface,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "app/src/main/res/drawable/app_icon.png\napp/src/main/res/drawable/ic_big_b_logo.xml",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("btn_close_logo_dialog")
      ) {
        Text("Done")
      }
    }
  )
}
