package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
  selectedSection: String = "ALL",
  selectedLabGroup: String = "ALL",
  onClassClick: (TimetableClass) -> Unit,
  modifier: Modifier = Modifier
) {
  val horizontalScroll = rememberScrollState()
  val pageVerticalScroll = rememberScrollState()

  // Matrix section filter defaults to "ALL" so both C1 and C2 classes are shown simultaneously
  var activeSectionFilter by remember(selectedSection) { mutableStateOf(selectedSection) }
  var activeLabGroupFilter by remember(selectedLabGroup) { mutableStateOf(selectedLabGroup) }

  var selectedClassForDetail by remember { mutableStateOf<TimetableClass?>(null) }

  // High performance optimization: precompute slot classes once per filter change instead of on every frame
  val slotClassesMap = remember(allClasses, activeSectionFilter, activeLabGroupFilter) {
    val map = HashMap<Pair<Int, MatrixSlot>, List<TimetableClass>>()
    for (day in MATRIX_DAYS) {
      for (slot in MATRIX_PERIODS) {
        val matching = allClasses.filter { item ->
          item.dayIndex == day.second &&
            item.startMinutes < slot.endMin && item.endMinutes > slot.startMin &&
            (activeSectionFilter == "ALL" || item.section == "ALL" || item.section.equals(activeSectionFilter, ignoreCase = true)) &&
            (activeLabGroupFilter == "ALL" || item.labGroup == "ALL" || item.labGroup.equals(activeLabGroupFilter, ignoreCase = true))
        }.sortedWith(compareBy({ it.section }, { it.subjectShort }, { it.labGroup }))
        map[Pair(day.second, slot)] = matching
      }
    }
    map
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .verticalScroll(pageVerticalScroll)
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
            text = "Big B · School of Civil Engineering",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = "3rd Semester B.Tech Timetable (Rev-3) · Ref: DU/SCE/219/26",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Interactive Master Matrix Filter Card (Shows C1 + C2 by default)
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Section selector row (All C1 + C2, Section C1, Section C2)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Layers,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "Section View:",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Active indicator pill
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (activeSectionFilter == "ALL") {
              MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            } else {
              MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
            }
          ) {
            Text(
              text = if (activeSectionFilter == "ALL") "All (C1 + C2)" else "Section $activeSectionFilter",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = if (activeSectionFilter == "ALL") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
        }

        // Section Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = activeSectionFilter == "ALL",
            onClick = {
              activeSectionFilter = "ALL"
            },
            label = { Text("All Classes (C1 + C2)", fontWeight = if (activeSectionFilter == "ALL") FontWeight.Bold else FontWeight.Normal) },
            leadingIcon = if (activeSectionFilter == "ALL") {
              { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier.testTag("filter_section_all")
          )

          FilterChip(
            selected = activeSectionFilter == "C1",
            onClick = {
              activeSectionFilter = "C1"
            },
            label = { Text("Section C1 Only", fontWeight = if (activeSectionFilter == "C1") FontWeight.Bold else FontWeight.Normal) },
            leadingIcon = if (activeSectionFilter == "C1") {
              { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null,
            modifier = Modifier.testTag("filter_section_c1")
          )

          FilterChip(
            selected = activeSectionFilter == "C2",
            onClick = {
              activeSectionFilter = "C2"
            },
            label = { Text("Section C2 Only", fontWeight = if (activeSectionFilter == "C2") FontWeight.Bold else FontWeight.Normal) },
            leadingIcon = if (activeSectionFilter == "C2") {
              { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null,
            modifier = Modifier.testTag("filter_section_c2")
          )
        }

        // Lab Group Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Labs:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          FilterChip(
            selected = activeLabGroupFilter == "ALL",
            onClick = {
              activeLabGroupFilter = "ALL"
            },
            label = { Text("All Groups", fontSize = 11.sp) },
            modifier = Modifier.testTag("filter_group_all")
          )

          listOf("GR1", "GR2", "GR3").forEach { grp ->
            FilterChip(
              selected = activeLabGroupFilter == grp,
              onClick = {
                activeLabGroupFilter = grp
              },
              label = { Text("Group ${grp.removePrefix("GR")}", fontSize = 11.sp) },
              modifier = Modifier.testTag("filter_group_$grp")
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Grid Container
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
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
                  .width(140.dp)
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
                  .height(116.dp),
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

              // Classes for each period slot (retrieved instantly from precomputed map)
              MATRIX_PERIODS.forEach { slot ->
                val matchingClasses = slotClassesMap[Pair(dayIndex, slot)].orEmpty()

                Surface(
                  modifier = Modifier
                    .width(140.dp)
                    .height(116.dp)
                    .padding(horizontal = 2.dp),
                  color = if (matchingClasses.isNotEmpty()) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                  } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f)
                  },
                  shape = RoundedCornerShape(8.dp)
                ) {
                  if (matchingClasses.isNotEmpty()) {
                    Column(
                      modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                      verticalArrangement = Arrangement.spacedBy(3.5.dp)
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

                        // Determine section badge styling for instant C1 vs C2 recognition
                        val isC1 = cls.section.equals("C1", ignoreCase = true)
                        val isC2 = cls.section.equals("C2", ignoreCase = true)

                        val badgeBg = when {
                          isC1 -> MaterialTheme.colorScheme.primaryContainer
                          isC2 -> MaterialTheme.colorScheme.tertiaryContainer
                          else -> MaterialTheme.colorScheme.secondaryContainer
                        }

                        val badgeTextColor = when {
                          isC1 -> MaterialTheme.colorScheme.onPrimaryContainer
                          isC2 -> MaterialTheme.colorScheme.onTertiaryContainer
                          else -> MaterialTheme.colorScheme.onSecondaryContainer
                        }

                        val badgeText = when {
                          cls.isLab && cls.labGroup != "ALL" -> "${cls.section}·${cls.labGroup}"
                          else -> cls.section
                        }

                        Surface(
                          modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                              selectedClassForDetail = cls
                            },
                          shape = RoundedCornerShape(6.dp),
                          color = chipColor.copy(alpha = 0.14f),
                          border = BorderStroke(0.8.dp, chipColor.copy(alpha = 0.4f))
                        ) {
                          Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.5.dp)) {
                            // Top Row: Subject short name and Section Badge (C1 / C2)
                            Row(
                              modifier = Modifier.fillMaxWidth(),
                              horizontalArrangement = Arrangement.SpaceBetween,
                              verticalAlignment = Alignment.CenterVertically
                            ) {
                              Text(
                                text = cls.subjectShort,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.5.sp,
                                color = chipColor,
                                maxLines = 1
                              )

                              Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = badgeBg
                              ) {
                                Text(
                                  text = badgeText,
                                  style = MaterialTheme.typography.labelSmall,
                                  fontWeight = FontWeight.ExtraBold,
                                  fontSize = 8.5.sp,
                                  color = badgeTextColor,
                                  modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                                )
                              }
                            }

                            Spacer(modifier = Modifier.height(1.dp))

                            // Bottom Row: Faculty initials & Venue
                            Row(
                              modifier = Modifier.fillMaxWidth(),
                              horizontalArrangement = Arrangement.SpaceBetween,
                              verticalAlignment = Alignment.CenterVertically
                            ) {
                              Text(
                                text = "(${cls.facultyInitials})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                              )
                              Text(
                                text = cls.room,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                              )
                            }
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

  // Quick Detail & Edit Dialog when tapping any matrix cell
  selectedClassForDetail?.let { cls ->
    AlertDialog(
      onDismissRequest = { selectedClassForDetail = null },
      title = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${cls.subjectName} (${cls.subjectShort})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
          )
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (cls.section.equals("C1", ignoreCase = true)) {
              MaterialTheme.colorScheme.primaryContainer
            } else if (cls.section.equals("C2", ignoreCase = true)) {
              MaterialTheme.colorScheme.tertiaryContainer
            } else {
              MaterialTheme.colorScheme.secondaryContainer
            }
          ) {
            Text(
              text = "Section ${cls.section}",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Course Code: ${cls.subjectCode}", style = MaterialTheme.typography.bodyMedium)
          Text("Time: ${cls.startTime} - ${cls.endTime} (${cls.dayOfWeek})", style = MaterialTheme.typography.bodyMedium)
          Text("Faculty: ${cls.facultyName} (${cls.facultyInitials})", style = MaterialTheme.typography.bodyMedium)
          Text("Section: ${cls.section}  |  Lab Group: ${cls.labGroup}", style = MaterialTheme.typography.bodyMedium)
          Text("Venue: ${cls.room}", style = MaterialTheme.typography.bodyMedium)
          if (cls.isLab) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFC2410C).copy(alpha = 0.12f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "Session Type: Practical Laboratory (3-Hour Session)",
                color = Color(0xFFC2410C),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      },
      dismissButton = {
        TextButton(onClick = { selectedClassForDetail = null }) {
          Text("Close")
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val target = selectedClassForDetail
            selectedClassForDetail = null
            target?.let { onClassClick(it) }
          }
        ) {
          Text("Edit Class")
        }
      }
    )
  }
}
