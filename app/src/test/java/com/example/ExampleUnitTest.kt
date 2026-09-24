package com.example

import com.example.data.defaultdata.KiitCivilTimetableData
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
    val fmMappings = KiitCivilTimetableData.courseFacultyMappings.firstOrNull { it.shortName == "FM" }
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
}

