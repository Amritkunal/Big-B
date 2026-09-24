package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.TimetableClass
import com.example.ui.components.ClassCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleClass = TimetableClass(
      id = 1,
      dayOfWeek = "Monday",
      dayIndex = 1,
      startTime = "09:00 AM",
      endTime = "10:00 AM",
      startMinutes = 540,
      endMinutes = 600,
      subjectShort = "CPM",
      subjectName = "Construction Project Management",
      subjectCode = "CE20003",
      facultyInitials = "PKA",
      facultyName = "Prof. P. K. Acharya",
      section = "C1",
      labGroup = "ALL",
      room = "Civil Block",
      isLab = false,
      colorKey = "CPM"
    )

    composeTestRule.setContent {
      MyApplicationTheme {
        ClassCard(
          timetableClass = sampleClass,
          isOngoing = true,
          onEdit = {},
          onDelete = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}

