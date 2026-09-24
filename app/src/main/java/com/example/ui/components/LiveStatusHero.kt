package com.example.ui.components

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TimetableClass
import java.util.Calendar

@Composable
fun LiveStatusHero(
  todayClasses: List<TimetableClass>,
  isToday: Boolean,
  dayName: String,
  modifier: Modifier = Modifier
) {
  val currentTime = remember {
    val cal = Calendar.getInstance()
    cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
  }

  val ongoingClass = if (isToday) {
    todayClasses.firstOrNull { currentTime in it.startMinutes..it.endMinutes }
  } else null

  val nextClass = if (isToday) {
    todayClasses.firstOrNull { it.startMinutes > currentTime }
  } else null

  // Material You dynamic container and content colors
  val (cardColor, contentColor, badgeColor, onBadgeColor) = when {
    ongoingClass != null -> Quad(
      MaterialTheme.colorScheme.primaryContainer,
      MaterialTheme.colorScheme.onPrimaryContainer,
      MaterialTheme.colorScheme.primary,
      MaterialTheme.colorScheme.onPrimary
    )
    nextClass != null -> Quad(
      MaterialTheme.colorScheme.secondaryContainer,
      MaterialTheme.colorScheme.onSecondaryContainer,
      MaterialTheme.colorScheme.secondary,
      MaterialTheme.colorScheme.onSecondary
    )
    else -> Quad(
      MaterialTheme.colorScheme.surfaceVariant,
      MaterialTheme.colorScheme.onSurfaceVariant,
      MaterialTheme.colorScheme.outline,
      MaterialTheme.colorScheme.surface
    )
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("live_status_hero"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = cardColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = badgeColor,
            modifier = Modifier.size(32.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = if (ongoingClass != null) Icons.Default.School else Icons.Default.Engineering,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = onBadgeColor
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = when {
              ongoingClass != null -> "CURRENT CLASS STATUS"
              nextClass != null -> "UPCOMING NEXT CLASS"
              isToday && todayClasses.isNotEmpty() -> "CLASSES COMPLETED"
              else -> "SCHEDULE FOR $dayName"
            },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            color = contentColor.copy(alpha = 0.8f)
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = badgeColor.copy(alpha = 0.15f)
        ) {
          Text(
            text = "${todayClasses.size} Classes",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      when {
        ongoingClass != null -> {
          Text(
            text = ongoingClass.subjectName.ifEmpty { ongoingClass.subjectShort },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = contentColor
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${ongoingClass.startTime} – ${ongoingClass.endTime} · Room: ${ongoingClass.room}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = contentColor.copy(alpha = 0.9f)
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Faculty: ${ongoingClass.facultyName} (${ongoingClass.facultyInitials})",
            style = MaterialTheme.typography.bodySmall,
            color = contentColor.copy(alpha = 0.8f)
          )
        }
        nextClass != null -> {
          Text(
            text = nextClass.subjectName.ifEmpty { nextClass.subjectShort },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = contentColor
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Starts at ${nextClass.startTime} (${nextClass.room})",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Faculty: ${nextClass.facultyName} (${nextClass.facultyInitials})",
            style = MaterialTheme.typography.bodySmall,
            color = contentColor.copy(alpha = 0.8f)
          )
        }
        else -> {
          Text(
            text = if (todayClasses.isEmpty()) {
              "No classes scheduled for $dayName"
            } else {
              "All classes done for today!"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (todayClasses.isEmpty()) {
              "Take time to revise Civil Engineering lectures or collaborate on projects."
            } else {
              "Great job today! Review Fluid Mechanics & Solid Mechanics notes for tomorrow."
            },
            style = MaterialTheme.typography.bodySmall,
            color = contentColor.copy(alpha = 0.75f)
          )
        }
      }
    }
  }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
