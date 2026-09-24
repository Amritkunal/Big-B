package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.TimetableViewModel

data class DayOption(
  val index: Int,
  val shortName: String,
  val fullName: String
)

val DAYS_LIST = listOf(
  DayOption(1, "Mon", "Monday"),
  DayOption(2, "Tue", "Tuesday"),
  DayOption(3, "Wed", "Wednesday"),
  DayOption(4, "Thu", "Thursday"),
  DayOption(5, "Fri", "Friday")
)

@Composable
fun DaySelectorRow(
  selectedDayIndex: Int,
  onDaySelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val todayIndex = TimetableViewModel.getTodayDayIndex()

  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .testTag("day_selector_row"),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
  ) {
    items(DAYS_LIST) { day ->
      val isSelected = day.index == selectedDayIndex
      val isToday = day.index == todayIndex

      Surface(
        modifier = Modifier
          .clip(RoundedCornerShape(14.dp))
          .clickable { onDaySelected(day.index) }
          .testTag("day_chip_${day.shortName}"),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) {
          MaterialTheme.colorScheme.primary
        } else {
          MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        },
        tonalElevation = if (isSelected) 4.dp else 1.dp
      ) {
        Column(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = day.shortName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
              color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
              } else {
                MaterialTheme.colorScheme.onSurface
              }
            )
            if (isToday) {
              Spacer(modifier = Modifier.padding(2.dp))
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) Color(0xFFFBBF24) else MaterialTheme.colorScheme.primary)
              )
            }
          }

          if (isToday) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "TODAY",
              style = MaterialTheme.typography.labelSmall,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = if (isSelected) {
                Color(0xFFFBBF24)
              } else {
                MaterialTheme.colorScheme.primary
              }
            )
          }
        }
      }
    }
  }
}
