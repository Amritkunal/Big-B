package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimetableClass
import com.example.ui.theme.SubjectCPMColor
import com.example.ui.theme.SubjectFluidColor
import com.example.ui.theme.SubjectGISColor
import com.example.ui.theme.SubjectLabColor
import com.example.ui.theme.SubjectProbColor
import com.example.ui.theme.SubjectSTWColor
import com.example.ui.theme.SubjectSolidColor
import com.example.ui.theme.SubjectSurveyColor

data class MatrixSlot(
  val label: String,
  val startMin: Int,
  val endMin: Int
)

val MATRIX_PERIODS = listOf(
  MatrixSlot("9:00am -\n10:00am", 540, 600),
  MatrixSlot("10:00am -\n11:00am", 600, 660),
  MatrixSlot("11:00am -\n12:00 Noon", 660, 720),
  MatrixSlot("12:00 Noon -\n01:00pm", 720, 780),
  MatrixSlot("01:00pm -\n02:00pm", 780, 840),
  MatrixSlot("03:00pm -\n04:00pm", 900, 960),
  MatrixSlot("04:00pm -\n05:00pm", 960, 1020)
)

val MATRIX_DAYS = listOf(
  Pair("Monday", 1),
  Pair("Tuesday", 2),
  Pair("Wednesday", 3),
  Pair("Thursday", 4),
  Pair("Friday", 5)
)

@Composable
fun TimetableMatrixView(
  allClasses: List<TimetableClass>,
  selectedSection: String,
  selectedLabGroup: String,
  onClassClick: (TimetableClass) -> Unit,
  modifier: Modifier = Modifier
) {
  val horizontalScroll = rememberScrollState()
  val verticalScroll = rememberScrollState()

  var selectedClassForDetail by remember { mutableStateOf<TimetableClass?>(null) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("timetable_matrix_view")
  ) {
    // Notice Board Header Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "KIIT School of Civil Engineering",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = "3rd Semester B.Tech Timetable (Rev-3) · Ref: KIIT/DU/SCE/219/26",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Grid Container
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.surface
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(horizontalScroll)
      ) {
        Column(modifier = Modifier.padding(8.dp)) {
          // Table Header Row: Days column + 7 time slots
          Row(
            modifier = Modifier.background(
              MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
              RoundedCornerShape(8.dp)
            )
          ) {
            Box(
              modifier = Modifier
                .width(100.dp)
                .height(56.dp)
                .padding(6.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "Day / Time",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }

            MATRIX_PERIODS.forEach { slot ->
              Box(
                modifier = Modifier
                  .width(130.dp)
                  .height(56.dp)
                  .padding(4.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = slot.label,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Rows for each Day
          MATRIX_DAYS.forEach { (dayName, dayIndex) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Day label cell
              Surface(
                modifier = Modifier
                  .width(100.dp)
                  .height(84.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = dayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }

              // Classes for each period slot
              MATRIX_PERIODS.forEach { slot ->
                val matchingClasses = allClasses.filter { item ->
                  item.dayIndex == dayIndex &&
                    // Overlaps with this slot
                    item.startMinutes < slot.endMin && item.endMinutes > slot.startMin &&
                    // Section filter
                    (selectedSection == "ALL" || item.section == "ALL" || item.section.equals(selectedSection, ignoreCase = true)) &&
                    // Lab group filter
                    (selectedLabGroup == "ALL" || item.labGroup == "ALL" || item.labGroup.equals(selectedLabGroup, ignoreCase = true))
                }

                Surface(
                  modifier = Modifier
                    .width(130.dp)
                    .height(84.dp)
                    .padding(horizontal = 2.dp),
                  color = if (matchingClasses.isNotEmpty()) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                  } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f)
                  },
                  shape = RoundedCornerShape(8.dp),
                  border = if (matchingClasses.isNotEmpty()) {
                    null
                  } else {
                    null
                  }
                ) {
                  if (matchingClasses.isNotEmpty()) {
                    Column(
                      modifier = Modifier
                        .padding(4.dp)
                        .verticalScroll(rememberScrollState()),
                      verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                      matchingClasses.forEach { cls ->
                        val chipColor = when (cls.colorKey) {
                          "FM" -> SubjectFluidColor
                          "SM" -> SubjectSolidColor
                          "CPM" -> SubjectCPMColor
                          "SG" -> SubjectSurveyColor
                          "PS" -> SubjectProbColor
                          "STW" -> SubjectSTWColor
                          "LAB" -> SubjectLabColor
                          "GIS" -> SubjectGISColor
                          else -> MaterialTheme.colorScheme.primary
                        }

                        Surface(
                          modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                              selectedClassForDetail = cls
                              onClassClick(cls)
                            },
                          shape = RoundedCornerShape(6.dp),
                          color = chipColor.copy(alpha = 0.16f)
                        ) {
                          Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp)) {
                            Text(
                              text = "${cls.subjectShort} (${cls.facultyInitials})",
                              style = MaterialTheme.typography.labelSmall,
                              fontWeight = FontWeight.ExtraBold,
                              fontSize = 11.sp,
                              color = chipColor
                            )
                            Text(
                              text = when {
                                cls.isLab && cls.labGroup != "ALL" -> "${cls.section} · ${cls.labGroup}"
                                else -> cls.section
                              },
                              style = MaterialTheme.typography.labelSmall,
                              fontSize = 9.sp,
                              color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                          }
                        }
                      }
                    }
                  } else {
                    Box(contentAlignment = Alignment.Center) {
                      Text(
                        text = "—",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Quick Detail Dialog when tapping any matrix cell
  selectedClassForDetail?.let { cls ->
    AlertDialog(
      onDismissRequest = { selectedClassForDetail = null },
      title = {
        Text(
          text = "${cls.subjectName} (${cls.subjectShort})",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text("Course Code: ${cls.subjectCode}")
          Text("Time: ${cls.startTime} - ${cls.endTime} (${cls.dayOfWeek})")
          Text("Faculty: ${cls.facultyName} (${cls.facultyInitials})")
          Text("Section: ${cls.section}  |  Lab Group: ${cls.labGroup}")
          Text("Venue: ${cls.room}")
          if (cls.isLab) {
            Text("Session Type: Practical Laboratory (3 Hours)", color = Color(0xFFC2410C), fontWeight = FontWeight.Bold)
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { selectedClassForDetail = null }) {
          Text("Close")
        }
      }
    )
  }
}
