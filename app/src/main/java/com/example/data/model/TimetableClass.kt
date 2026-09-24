package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable_classes")
data class TimetableClass(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val dayOfWeek: String,       // "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
  val dayIndex: Int,          // 1: Monday, 2: Tuesday, 3: Wednesday, 4: Thursday, 5: Friday, 6: Saturday
  val startTime: String,      // "09:00 AM"
  val endTime: String,        // "10:00 AM"
  val startMinutes: Int,      // Minutes from midnight, e.g., 9 * 60 = 540
  val endMinutes: Int,        // Minutes from midnight, e.g., 10 * 60 = 600
  val subjectShort: String,   // "FM", "CPM", "SM", "S&G", "P&S", "S&TW", "EQA Lab", etc.
  val subjectName: String,    // Full name e.g. "Fluid Mechanics"
  val subjectCode: String,    // "CE21001", "CE20001", etc.
  val facultyInitials: String,// "JPP", "PMC", "PKA", "DKB", etc.
  val facultyName: String,    // "Prof. Paromita Chakraborty"
  val section: String,        // "C1", "C2", or "ALL"
  val labGroup: String,       // "GR1", "GR2", "GR3", or "ALL"
  val room: String,           // "C5", "Fluid Lab", "Survey Lab", "Main Hall"
  val isLab: Boolean,
  val colorKey: String = "GENERAL"
)
