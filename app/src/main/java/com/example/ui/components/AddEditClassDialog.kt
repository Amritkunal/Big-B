package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.defaultdata.KiitCivilTimetableData
import com.example.data.model.TimetableClass

data class PeriodSlotPreset(
  val label: String,
  val start: String,
  val end: String,
  val startMin: Int,
  val endMin: Int
)

val PERIOD_PRESETS = listOf(
  PeriodSlotPreset("9:00 - 10:00 AM", "09:00 AM", "10:00 AM", 540, 600),
  PeriodSlotPreset("10:00 - 11:00 AM", "10:00 AM", "11:00 AM", 600, 660),
  PeriodSlotPreset("11:00 AM - 12:00 PM", "11:00 AM", "12:00 PM", 660, 720),
  PeriodSlotPreset("12:00 - 01:00 PM", "12:00 PM", "01:00 PM", 720, 780),
  PeriodSlotPreset("12:00 - 02:00 PM", "12:00 PM", "02:00 PM", 720, 840),
  PeriodSlotPreset("01:00 - 02:00 PM", "01:00 PM", "02:00 PM", 780, 840),
  PeriodSlotPreset("12:00 - 03:00 PM (Lab)", "12:00 PM", "03:00 PM", 720, 900),
  PeriodSlotPreset("03:00 - 04:00 PM", "03:00 PM", "04:00 PM", 900, 960),
  PeriodSlotPreset("04:00 - 05:00 PM", "04:00 PM", "05:00 PM", 960, 1020),
  PeriodSlotPreset("03:00 - 05:00 PM", "03:00 PM", "05:00 PM", 900, 1020)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditClassDialog(
  editingClass: TimetableClass?,
  initialDayIndex: Int,
  onDismiss: () -> Unit,
  onSave: (
    id: Long,
    dayOfWeek: String,
    dayIndex: Int,
    startTime: String,
    endTime: String,
    startMinutes: Int,
    endMinutes: Int,
    subjectShort: String,
    subjectName: String,
    subjectCode: String,
    facultyInitials: String,
    facultyName: String,
    section: String,
    labGroup: String,
    room: String,
    isLab: Boolean
  ) -> Unit
) {
  var dayIndex by remember { mutableIntStateOf(editingClass?.dayIndex ?: initialDayIndex) }
  var startTime by remember { mutableStateOf(editingClass?.startTime ?: "09:00 AM") }
  var endTime by remember { mutableStateOf(editingClass?.endTime ?: "10:00 AM") }
  var startMinutes by remember { mutableIntStateOf(editingClass?.startMinutes ?: 540) }
  var endMinutes by remember { mutableIntStateOf(editingClass?.endMinutes ?: 600) }

  var subjectShort by remember { mutableStateOf(editingClass?.subjectShort ?: "FM") }
  var subjectName by remember { mutableStateOf(editingClass?.subjectName ?: "Fluid Mechanics") }
  var subjectCode by remember { mutableStateOf(editingClass?.subjectCode ?: "CE21001") }
  var facultyInitials by remember { mutableStateOf(editingClass?.facultyInitials ?: "JPP") }
  var facultyName by remember { mutableStateOf(editingClass?.facultyName ?: "Prof. J.P. Patra") }
  var section by remember { mutableStateOf(editingClass?.section ?: "C1") }
  var labGroup by remember { mutableStateOf(editingClass?.labGroup ?: "ALL") }
  var room by remember { mutableStateOf(editingClass?.room ?: "Civil Block") }
  var isLab by remember { mutableStateOf(editingClass?.isLab ?: false) }

  val dayOfWeek = when (dayIndex) {
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    6 -> "Saturday"
    else -> "Monday"
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .testTag("add_edit_class_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Dialog Title Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (editingClass == null) "Add Timetable Class" else "Edit Timetable Class",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close dialog",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Pick KIIT Civil Course Presets
        Text(
          text = "Quick Fill KIIT Subject:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          KiitCivilTimetableData.courses.forEach { course ->
            val isCurrent = subjectShort.equals(course.shortName, ignoreCase = true)
            FilterChip(
              selected = isCurrent,
              onClick = {
                subjectShort = course.shortName
                subjectName = course.fullName
                subjectCode = course.code
                room = course.defaultVenue
                isLab = course.type.contains("Lab", ignoreCase = true)
                if (isLab) {
                  startTime = "12:00 PM"
                  endTime = "03:00 PM"
                  startMinutes = 720
                  endMinutes = 900
                }
              },
              label = {
                Text(
                  text = course.shortName,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Day Selection
        Text(
          text = "Day of Week:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          DAYS_LIST.forEach { d ->
            val isSelected = d.index == dayIndex
            FilterChip(
              selected = isSelected,
              onClick = { dayIndex = d.index },
              label = {
                Text(
                  text = d.shortName,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Period Slot Preset Chips
        Text(
          text = "Timing Slot (Preset):",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          PERIOD_PRESETS.forEach { preset ->
            val isSelected = startTime == preset.start && endTime == preset.end
            FilterChip(
              selected = isSelected,
              onClick = {
                startTime = preset.start
                endTime = preset.end
                startMinutes = preset.startMin
                endMinutes = preset.endMin
              },
              label = {
                Text(
                  text = preset.label,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Subject Details Input Fields
        OutlinedTextField(
          value = subjectShort,
          onValueChange = { subjectShort = it },
          label = { Text("Subject Abbreviation (e.g. FM, CPM)") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_subject_short")
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = subjectName,
          onValueChange = { subjectName = it },
          label = { Text("Full Subject Name (e.g. Fluid Mechanics)") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_subject_name")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = subjectCode,
            onValueChange = { subjectCode = it },
            label = { Text("Course Code (e.g. CE21001)") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = room,
            onValueChange = { room = it },
            label = { Text("Room / Lab (e.g. C5)") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Faculty Info
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = facultyInitials,
            onValueChange = {
              facultyInitials = it
              if (facultyName.isBlank() || facultyName.startsWith("Prof.")) {
                facultyName = KiitCivilTimetableData.getFacultyName(it)
              }
            },
            label = { Text("Faculty Initials (e.g. JPP)") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = facultyName,
            onValueChange = { facultyName = it },
            label = { Text("Faculty Name") },
            singleLine = true,
            modifier = Modifier.weight(1.5f)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Section and Group Selection
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Section:",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              listOf("C1", "C2", "ALL").forEach { sec ->
                FilterChip(
                  selected = section == sec,
                  onClick = { section = sec },
                  label = { Text(sec) }
                )
              }
            }
          }

          Column {
            Text(
              text = "Lab Group:",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              listOf("GR1", "GR2", "GR3", "ALL").forEach { grp ->
                FilterChip(
                  selected = labGroup == grp,
                  onClick = { labGroup = grp },
                  label = { Text(grp) }
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Is Lab switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Practical / Laboratory Session",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "EQA Lab, MT Lab, BDE&C, etc.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Switch(
            checked = isLab,
            onCheckedChange = { isLab = it }
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Bottom Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Cancel")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (subjectShort.isNotBlank()) {
                onSave(
                  editingClass?.id ?: 0L,
                  dayOfWeek,
                  dayIndex,
                  startTime,
                  endTime,
                  startMinutes,
                  endMinutes,
                  subjectShort,
                  subjectName,
                  subjectCode,
                  facultyInitials,
                  facultyName,
                  section,
                  labGroup,
                  room,
                  isLab
                )
              }
            },
            modifier = Modifier.testTag("save_class_button")
          ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (editingClass == null) "Add Class" else "Save Changes")
          }
        }
      }
    }
  }
}
