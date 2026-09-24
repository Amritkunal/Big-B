package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassCard(
  timetableClass: TimetableClass,
  isOngoing: Boolean = false,
  onEdit: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val accentColor = when (timetableClass.colorKey) {
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

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("class_card_${timetableClass.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isOngoing) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
      } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      }
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isOngoing) 3.dp else 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Top Row: Time badge, Status indicator, and Edit/Delete actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${timetableClass.startTime} – ${timetableClass.endTime}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          if (isOngoing) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.primary
            ) {
              Text(
                text = "ONGOING NOW",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              )
            }
          }
        }

        Row {
          IconButton(
            onClick = onEdit,
            modifier = Modifier
              .size(36.dp)
              .testTag("edit_button_${timetableClass.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Edit class",
              modifier = Modifier.size(18.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          IconButton(
            onClick = onDelete,
            modifier = Modifier
              .size(36.dp)
              .testTag("delete_button_${timetableClass.id}")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteOutline,
              contentDescription = "Delete class",
              modifier = Modifier.size(18.dp),
              tint = MaterialTheme.colorScheme.error.copy(alpha = 0.85f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Middle Row: Subject Title and Subject Code
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Colored dot / pill
        Box(
          modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(accentColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = timetableClass.subjectShort,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        if (timetableClass.subjectCode.isNotBlank()) {
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = accentColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = timetableClass.subjectCode,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.SemiBold,
              color = accentColor
            )
          }
        }

        if (timetableClass.isLab) {
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color(0xFFF97316).copy(alpha = 0.18f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Science,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color(0xFFC2410C)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "PRACTICAL LAB",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC2410C)
              )
            }
          }
        }
      }

      if (timetableClass.subjectName.isNotBlank() && timetableClass.subjectName != timetableClass.subjectShort) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = timetableClass.subjectName,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Bottom Row: Faculty & Room & Section badges
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Faculty Chip
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surface
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              modifier = Modifier.size(14.dp),
              tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            val facultyLabel = if (timetableClass.facultyName.isNotBlank()) {
              "${timetableClass.facultyName} (${timetableClass.facultyInitials})"
            } else {
              timetableClass.facultyInitials
            }
            Text(
              text = facultyLabel,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Section Chip
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surface
        ) {
          Text(
            text = when (timetableClass.section) {
              "ALL" -> "Sec C1 & C2"
              else -> "Sec ${timetableClass.section}"
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
          )
        }

        // Lab Group Chip if applicable
        if (timetableClass.labGroup != "ALL") {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
          ) {
            Text(
              text = "Group ${timetableClass.labGroup}",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFFD97706)
            )
          }
        }

        // Venue / Room Chip
        if (timetableClass.room.isNotBlank()) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.tertiary
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = timetableClass.room,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
  }
}
