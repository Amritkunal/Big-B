package com.example

import com.example.data.defaultdata.CivilTimetableData
import com.example.ui.components.DAYS_LIST
import com.example.ui.components.LAB_GROUPS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun daysList_isMondayToFridayOnly() {
    assertEquals(5, DAYS_LIST.size)
    val dayNames = DAYS_LIST.map { it.fullName }
    assertEquals(listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"), dayNames)
  }

  @Test
  fun courseFacultyMappings_hasFmWithJppAndParomita() {
    val fmMappings = CivilTimetableData.courseFacultyMappings.firstOrNull { it.shortName == "FM" }
    assertNotNull(fmMappings)
    assertEquals("JPP", fmMappings?.sectionC1Faculty)
    assertEquals("Paromita", fmMappings?.sectionC2Faculty)
    assertEquals("FM class is taken by JPP in C1 and Paromita in C2", fmMappings?.summaryText)
  }

  @Test
  fun labGroups_hasThreeGroups() {
    assertEquals(3, LAB_GROUPS.size)
    assertEquals(listOf("GR1", "GR2", "GR3"), LAB_GROUPS.map { it.id })
  }

  @Test
  fun weekMatrix_showsBothC1AndC2Classes() {
    val allClasses = CivilTimetableData.getDefaultClasses()
    val c1Count = allClasses.count { it.section.equals("C1", ignoreCase = true) }
    val c2Count = allClasses.count { it.section.equals("C2", ignoreCase = true) }
    assertTrue("Should have C1 classes", c1Count > 10)
    assertTrue("Should have C2 classes", c2Count > 10)

    // For Monday 09:00 - 10:00 slot (540 to 600), both C1 and C2 classes must be present
    val mondaySlot1Classes = allClasses.filter {
      it.dayIndex == 1 && it.startMinutes < 600 && it.endMinutes > 540
    }
    val sectionsInSlot1 = mondaySlot1Classes.map { it.section }.toSet()
    assertTrue(sectionsInSlot1.contains("C1"))
    assertTrue(sectionsInSlot1.contains("C2"))
  }
}

