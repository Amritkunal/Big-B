package com.example

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TimetableClass
import com.example.ui.TimetableViewModel
import com.example.ui.components.AddEditClassDialog
import com.example.ui.components.BatchFilterBar
import com.example.ui.components.ClassCard
import com.example.ui.components.CourseFacultyDirectoryView
import com.example.ui.components.CoursesDirectorySheet
import com.example.ui.components.AppLogoBadge
import com.example.ui.components.CustomizeLogoDialog
import com.example.ui.components.DAYS_LIST
import com.example.ui.components.DaySelectorRow
import com.example.ui.components.LabGroupPickerDialog
import com.example.ui.components.LiveStatusHero
import com.example.ui.components.MinimalClassCard
import com.example.ui.components.PdfPreviewDialog
import com.example.ui.components.TimetableMatrixView
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Edit
import java.io.File
import java.util.Calendar
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val viewModel: TimetableViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val systemInDark = isSystemInDarkTheme()
      var dynamicColor by remember { mutableStateOf(true) }
      var darkTheme by remember { mutableStateOf(systemInDark) }

      MyApplicationTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        TimetableAppScreen(
          viewModel = viewModel,
          dynamicColor = dynamicColor,
          onToggleDynamicColor = { dynamicColor = it },
          darkTheme = darkTheme,
          onToggleDarkTheme = { darkTheme = it }
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableAppScreen(
  viewModel: TimetableViewModel,
  dynamicColor: Boolean,
  onToggleDynamicColor: (Boolean) -> Unit,
  darkTheme: Boolean,
  onToggleDarkTheme: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val minimalClasses by viewModel.minimalDayClasses.collectAsStateWithLifecycle()
  val allClasses by viewModel.allClasses.collectAsStateWithLifecycle()
  val pdfBitmap by viewModel.uploadedPdfBitmap.collectAsStateWithLifecycle()
  val pdfFileName by viewModel.uploadedFileName.collectAsStateWithLifecycle()
  val isProcessingUpload by viewModel.isProcessingUpload.collectAsStateWithLifecycle()

  var isThemeDialogOpen by remember { mutableStateOf(false) }
  var isLogoDialogOpen by remember { mutableStateOf(false) }
  var logoCacheBuster by remember { mutableStateOf(System.currentTimeMillis()) }
  val snackbarHostState = remember { SnackbarHostState() }

  // PDF or Document file picker launcher
  val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    uri?.let {
      val fileName = queryFileName(context, it)
      viewModel.handleUploadedFile(it, fileName)
    }
  }

  // Handle snackbar messages
  LaunchedEffect(uiState.snackbarMessage) {
    uiState.snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearSnackbar()
    }
  }

  val currentMinutes by produceState(initialValue = getNowMinutes()) {
    while (true) {
      delay(30_000)
      value = getNowMinutes()
    }
  }

  val todayIndex = remember { TimetableViewModel.getTodayDayIndex() }
  val activeDayOption = remember(uiState.selectedDayIndex) {
    DAYS_LIST.firstOrNull { it.index == uiState.selectedDayIndex } ?: DAYS_LIST.first()
  }
  val isToday = uiState.selectedDayIndex == todayIndex
  val areAllClassesOver = isToday && minimalClasses.isNotEmpty() && minimalClasses.all { currentMinutes >= it.endMinutes }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .testTag("timetable_main_scaffold"),
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { isLogoDialogOpen = true }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("header_logo_title_button")
          ) {
            AppLogoBadge(
              size = 32.dp,
              cacheBuster = logoCacheBuster,
              onClick = { isLogoDialogOpen = true }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Big B",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.testTag("header_title_text")
            )
          }
        },
        actions = {
          IconButton(
            onClick = { isLogoDialogOpen = true },
            modifier = Modifier.testTag("topbar_logo_button")
          ) {
            Icon(
              imageVector = Icons.Default.Image,
              contentDescription = "Change App Logo",
              tint = MaterialTheme.colorScheme.primary
            )
          }
          IconButton(
            onClick = { isThemeDialogOpen = true },
            modifier = Modifier.testTag("topbar_theme_button")
          ) {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = "Theme Settings",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("bottom_nav_bar")
      ) {
        NavigationBarItem(
          selected = uiState.activeTab == 0,
          onClick = {
            if (uiState.activeTab != 0) {
              viewModel.setActiveTab(0)
            }
          },
          icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Classes") },
          label = { Text("Classes") },
          modifier = Modifier.testTag("nav_item_daily")
        )
        NavigationBarItem(
          selected = uiState.activeTab == 1,
          onClick = {
            if (uiState.activeTab != 1) {
              viewModel.setActiveTab(1)
            }
          },
          icon = { Icon(Icons.Default.School, contentDescription = "Faculty & Courses") },
          label = { Text("Faculty") },
          modifier = Modifier.testTag("nav_item_directory")
        )
        NavigationBarItem(
          selected = uiState.activeTab == 2,
          onClick = {
            if (uiState.activeTab != 2) {
              viewModel.setActiveTab(2)
            }
          },
          icon = { Icon(Icons.Default.GridOn, contentDescription = "Week Matrix") },
          label = { Text("Week Matrix") },
          modifier = Modifier.testTag("nav_item_matrix")
        )
      }
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      AnimatedContent(
        targetState = uiState.activeTab,
        transitionSpec = {
          fadeIn(animationSpec = tween(durationMillis = 140))
            .togetherWith(fadeOut(animationSpec = tween(durationMillis = 100)))
        },
        label = "SectionTransition",
        modifier = Modifier.fillMaxSize()
      ) { activeTab ->
        when (activeTab) {
        0 -> {
          // Section 1: Daily Classes (Mon - Fri only, classes only, time & subject, lab group with pen)
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .testTag("daily_schedule_list"),
            contentPadding = PaddingValues(bottom = 80.dp)
          ) {
            // Day Selector Chips (Mon - Fri)
            item {
              Spacer(modifier = Modifier.height(6.dp))
              DaySelectorRow(
                selectedDayIndex = uiState.selectedDayIndex,
                onDaySelected = {
                  viewModel.selectDay(it)
                }
              )
            }

            // Minimal Sub-header with day title & persistent Lab Group Switcher
            item {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${activeDayOption.fullName}'s Schedule",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                      viewModel.openLabGroupPicker()
                    }
                    .testTag("lab_group_quick_switch")
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    val groupNum = uiState.selectedLabGroup.removePrefix("GR")
                    Text(
                      text = "Lab: Group $groupNum",
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                      imageVector = Icons.Default.Edit,
                      contentDescription = "Edit Lab Group",
                      modifier = Modifier.size(13.dp),
                      tint = MaterialTheme.colorScheme.primary
                    )
                  }
                }
              }
            }

            // Notice when all classes for today are over
            if (areAllClassesOver) {
              item {
                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("all_classes_over_banner"),
                  shape = RoundedCornerShape(14.dp),
                  color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.85f),
                  tonalElevation = 2.dp
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Completed",
                      tint = MaterialTheme.colorScheme.onTertiaryContainer,
                      modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                      Text(
                        text = "All classes are over",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                      )
                      Text(
                        text = "All scheduled lectures and labs for today have completed.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                      )
                    }
                  }
                }
              }
            }

            // Class items (clean, time & class only, no faculty, no room)
            if (minimalClasses.isEmpty()) {
              item {
                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                  shape = RoundedCornerShape(16.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ) {
                  Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Icon(
                      imageVector = Icons.Default.CalendarToday,
                      contentDescription = null,
                      modifier = Modifier.size(44.dp),
                      tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                      text = "No Classes on ${activeDayOption.fullName}",
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            } else {
              items(
                items = minimalClasses,
                key = { "${it.dayIndex}_${it.startMinutes}_${it.subjectShort}" }
              ) { cls ->
                val isOngoing = isToday && isClassOngoing(cls, currentMinutes)
                val isOver = isToday && currentMinutes >= cls.endMinutes
                MinimalClassCard(
                  timetableClass = cls,
                  isOngoing = isOngoing,
                  isOver = isOver,
                  selectedLabGroup = uiState.selectedLabGroup,
                  onEditLabGroup = {
                    viewModel.openLabGroupPicker()
                  },
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
              }
            }
          }
        }

        1 -> {
          // Section 2: Course & Faculty Allocation Directory (C1 vs C2)
          CourseFacultyDirectoryView()
        }

        2 -> {
          // Section 3: Week Matrix showing all classes (both C1 + C2)
          TimetableMatrixView(
            allClasses = allClasses,
            selectedSection = "ALL",
            selectedLabGroup = "ALL",
            onClassClick = { cls ->
              viewModel.openEditDialog(cls)
            }
          )
        }
      }
      }
    }
  }

  // Theme Settings Dialog (Material You controls)
  if (isThemeDialogOpen) {
    AlertDialog(
      onDismissRequest = { isThemeDialogOpen = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Palette,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Material You Theme", fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
          Text(
            text = "Material You dynamically extracts personalized color harmonies from your Android wallpaper and system palette.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          // Color palette preview chips
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Text("Primary", color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Box(
              modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary),
              contentAlignment = Alignment.Center
            ) {
              Text("Secondary", color = MaterialTheme.colorScheme.onSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Box(
              modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.tertiary),
              contentAlignment = Alignment.Center
            ) {
              Text("Tertiary", color = MaterialTheme.colorScheme.onTertiary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }

          // Dynamic Color Switch
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Dynamic Color",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                  "Monet Wallpaper Palette (Android 12+)"
                } else {
                  "Requires Android 12+ (Fallback enabled)"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = dynamicColor,
              onCheckedChange = { onToggleDynamicColor(it) },
              modifier = Modifier.testTag("switch_dynamic_color")
            )
          }

          // Dark Theme Switch
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Tonal surface dark palette",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Switch(
              checked = darkTheme,
              onCheckedChange = { onToggleDarkTheme(it) },
              modifier = Modifier.testTag("switch_dark_theme")
            )
          }

          // App Logo Customization Action
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "App Main Logo",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Select custom image or reset to Big B logo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            TextButton(
              onClick = {
                isThemeDialogOpen = false
                isLogoDialogOpen = true
              },
              modifier = Modifier.testTag("btn_theme_change_logo")
            ) {
              Text("Customize")
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { isThemeDialogOpen = false }) {
          Text("Done")
        }
      }
    )
  }

  // Add / Edit Class Dialog
  if (uiState.isAddEditDialogOpen) {
    AddEditClassDialog(
      editingClass = uiState.editingClass,
      initialDayIndex = uiState.selectedDayIndex,
      onDismiss = { viewModel.closeAddEditDialog() },
      onSave = { id, dayOfWeek, dayIndex, startTime, endTime, startMinutes, endMinutes,
                 subjectShort, subjectName, subjectCode, facultyInitials, facultyName,
                 section, labGroup, room, isLab ->
        viewModel.saveClass(
          id, dayOfWeek, dayIndex, startTime, endTime, startMinutes, endMinutes,
          subjectShort, subjectName, subjectCode, facultyInitials, facultyName,
          section, labGroup, room, isLab
        )
      }
    )
  }

  // PDF Preview Dialog
  if (uiState.isPdfPreviewOpen) {
    PdfPreviewDialog(
      bitmap = pdfBitmap,
      fileName = pdfFileName,
      isProcessing = isProcessingUpload,
      onDismiss = { viewModel.closePdfPreview() },
      onPickNewPdf = { filePickerLauncher.launch("application/pdf") },
      onResetToDefaults = { viewModel.setResetConfirmOpen(true) }
    )
  }

  // Reset to default schedule confirmation dialog
  if (uiState.isResetConfirmOpen) {
    AlertDialog(
      onDismissRequest = { viewModel.setResetConfirmOpen(false) },
      icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
      title = { Text("Reset to Official Timetable?") },
      text = {
        Text("This will restore the standard School of Civil Engineering (SCE) 3rd Semester B.Tech schedule (Rev-3).")
      },
      confirmButton = {
        TextButton(
          onClick = {
            viewModel.resetToDefaultSchedule()
            viewModel.closePdfPreview()
          }
        ) {
          Text("Reset Timetable", color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        TextButton(onClick = { viewModel.setResetConfirmOpen(false) }) {
          Text("Cancel")
        }
      }
    )
  }

  // Persistent Lab Group Picker Dialog
  if (uiState.isLabGroupPickerOpen) {
    LabGroupPickerDialog(
      currentGroup = uiState.selectedLabGroup,
      onGroupSelected = {
        viewModel.selectLabGroup(it)
      },
      onDismiss = { viewModel.closeLabGroupPicker() }
    )
  }

  // Manual Logo Customization Dialog
  if (isLogoDialogOpen) {
    val customFile = remember(logoCacheBuster) { File(context.filesDir, "custom_app_logo.png") }
    CustomizeLogoDialog(
      customLogoFile = customFile,
      onLogoUpdated = {
        logoCacheBuster = System.currentTimeMillis()
      },
      onDismiss = { isLogoDialogOpen = false }
    )
  }
}

private fun getNowMinutes(): Int {
  val cal = Calendar.getInstance()
  return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
}

private fun isClassOngoing(cls: TimetableClass, currentMin: Int): Boolean {
  return currentMin in cls.startMinutes until cls.endMinutes
}

private fun queryFileName(context: Context, uri: Uri): String? {
  var name: String? = null
  try {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
      if (it.moveToFirst()) {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1) {
          name = it.getString(nameIndex)
        }
      }
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return name ?: uri.lastPathSegment
}
