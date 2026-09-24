package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
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

@Composable
fun MinimalClassCard(
  timetableClass: TimetableClass,
  isOngoing: Boolean = false,
  isOver: Boolean = false,
  selectedLabGroup: String,
  onEditLabGroup: () -> Unit,
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
      .testTag("minimal_class_card_${timetableClass.id}"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isOngoing) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
      } else if (isOver) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f)
      } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
      }
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isOngoing) 2.dp else 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
      // Top Row: Time and Ongoing / Over status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 1.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              modifier = Modifier.size(15.dp),
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${timetableClass.startTime} – ${timetableClass.endTime}",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        if (isOngoing) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primary
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.onPrimary)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = "ONGOING",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp
              )
            }
          }
        } else if (isOver) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "OVER",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Middle: Class Name with accent bar
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .width(4.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(accentColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = timetableClass.subjectName.ifBlank { timetableClass.subjectShort },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (timetableClass.subjectCode.isNotBlank() || timetableClass.subjectShort.isNotBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "${timetableClass.subjectShort} · ${timetableClass.subjectCode}".trim(' ', '·'),
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // If it's a lab, show lab indicator and pen to change lab group
      if (timetableClass.isLab) {
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surface,
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onEditLabGroup() }
            .testTag("lab_group_badge_${timetableClass.id}")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Science,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(6.dp))
            val groupDisplay = when (selectedLabGroup) {
              "GR1" -> "Group 1"
              "GR2" -> "Group 2"
              "GR3" -> "Group 3"
              else -> "Group 1"
            }
            Text(
              text = "Practical Lab · $groupDisplay",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Change Lab Group",
              modifier = Modifier.size(14.dp),
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      }
    }
  }
}
