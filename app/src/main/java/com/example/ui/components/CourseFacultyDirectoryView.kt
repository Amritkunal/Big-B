package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.defaultdata.KiitCivilTimetableData

@Composable
fun CourseFacultyDirectoryView(
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  val mappings = remember(searchQuery) {
    KiitCivilTimetableData.courseFacultyMappings.filter {
      searchQuery.isBlank() ||
        it.fullName.contains(searchQuery, ignoreCase = true) ||
        it.shortName.contains(searchQuery, ignoreCase = true) ||
        it.code.contains(searchQuery, ignoreCase = true) ||
        it.sectionC1Faculty.contains(searchQuery, ignoreCase = true) ||
        it.sectionC2Faculty.contains(searchQuery, ignoreCase = true) ||
        it.labGroup1Faculty.contains(searchQuery, ignoreCase = true) ||
        it.labGroup2Faculty.contains(searchQuery, ignoreCase = true) ||
        it.labGroup3Faculty.contains(searchQuery, ignoreCase = true)
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("course_faculty_directory_view")
  ) {
    // Section Header
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp)) {
      Text(
        text = "Course & Faculty Directory",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Section C1 & C2 faculty allocations for 3rd Sem Civil",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      },
      trailingIcon = {
        if (searchQuery.isNotBlank()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear search"
            )
          }
        }
      },
      placeholder = { Text("Search course or faculty name...") },
      singleLine = true,
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("faculty_directory_search_input")
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Course List
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(
        items = mappings,
        key = { it.code }
      ) { course ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("course_mapping_card_${course.shortName}")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            // Course header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = course.fullName,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surface
                  ) {
                    Text(
                      text = course.shortName,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = course.code,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (course.isLab) {
                  MaterialTheme.colorScheme.secondaryContainer
                } else {
                  MaterialTheme.colorScheme.primaryContainer
                }
              ) {
                Text(
                  text = if (course.isLab) "Practical Lab" else "Theory",
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.SemiBold,
                  color = if (course.isLab) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                  } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                  }
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))

            if (!course.isLab) {
              // Section C1 & Section C2
              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.width(42.dp)
                  ) {
                    Text(
                      text = "C1",
                      modifier = Modifier.padding(vertical = 3.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.primary,
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  val c1Display = course.facultyC1Full.ifBlank { course.sectionC1Faculty }
                  Text(
                    text = c1Display,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                    modifier = Modifier.width(42.dp)
                  ) {
                    Text(
                      text = "C2",
                      modifier = Modifier.padding(vertical = 3.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.secondary,
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  val c2Display = course.facultyC2Full.ifBlank { course.sectionC2Faculty }
                  Text(
                    text = c2Display,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            } else {
              // Labs with Group allocations
              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                    modifier = Modifier.width(42.dp)
                  ) {
                    Text(
                      text = "G1",
                      modifier = Modifier.padding(vertical = 3.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.tertiary,
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = course.labGroup1Faculty,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                    modifier = Modifier.width(42.dp)
                  ) {
                    Text(
                      text = "G2",
                      modifier = Modifier.padding(vertical = 3.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.tertiary,
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = course.labGroup2Faculty,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                    modifier = Modifier.width(42.dp)
                  ) {
                    Text(
                      text = "G3",
                      modifier = Modifier.padding(vertical = 3.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.tertiary,
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = course.labGroup3Faculty,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
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
