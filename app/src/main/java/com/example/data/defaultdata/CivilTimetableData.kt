package com.example.data.defaultdata

import com.example.data.model.TimetableClass

data class CourseInfo(
  val code: String,
  val shortName: String,
  val fullName: String,
  val type: String, // "Theory" or "Practical / Lab"
  val defaultVenue: String
)

data class FacultyInfo(
  val initials: String,
  val fullName: String,
  val department: String = "School of Civil Engineering"
)

data class CourseFacultyMapping(
  val code: String,
  val shortName: String,
  val fullName: String,
  val isLab: Boolean,
  val sectionC1Faculty: String,
  val sectionC2Faculty: String,
  val labGroup1Faculty: String = "",
  val labGroup2Faculty: String = "",
  val labGroup3Faculty: String = "",
  val facultyC1Full: String = "",
  val facultyC2Full: String = ""
) {
  val summaryText: String
    get() = if (isLab) {
      "$shortName lab is taken by $labGroup1Faculty in Group 1, $labGroup2Faculty in Group 2, and $labGroup3Faculty in Group 3"
    } else {
      "$shortName class is taken by $sectionC1Faculty in C1 and $sectionC2Faculty in C2"
    }
}

object CivilTimetableData {

  const val TIMETABLE_NOTICE_TITLE = "School of Civil Engineering - 3rd Semester B.Tech"
  const val TIMETABLE_REF_NO = "DU/SCE/219/26"
  const val TIMETABLE_EFFECTIVE_DATE = "17-09-2026 (Rev-3)"

  val courseFacultyMappings = listOf(
    CourseFacultyMapping(
      code = "CE21001",
      shortName = "FM",
      fullName = "Fluid Mechanics",
      isLab = false,
      sectionC1Faculty = "JPP",
      sectionC2Faculty = "Paromita",
      facultyC1Full = "Prof. J.P. Patra (JPP)",
      facultyC2Full = "Prof. Paromita Chakraborty"
    ),
    CourseFacultyMapping(
      code = "CE20001",
      shortName = "SM",
      fullName = "Solid Mechanics",
      isLab = false,
      sectionC1Faculty = "Tribikram Mohanty",
      sectionC2Faculty = "Sunny Jaiswal",
      facultyC1Full = "Prof. Tribikram Mohanty",
      facultyC2Full = "Prof. S. Jaiswal (Sunny Jaiswal)"
    ),
    CourseFacultyMapping(
      code = "CE20003",
      shortName = "CPM",
      fullName = "Construction Project Management",
      isLab = false,
      sectionC1Faculty = "P. K. Acharya",
      sectionC2Faculty = "D. K. Bera",
      facultyC1Full = "Prof. P. K. Acharya",
      facultyC2Full = "Prof. D.K. Bera"
    ),
    CourseFacultyMapping(
      code = "CE20005",
      shortName = "S&G",
      fullName = "Surveying & Geomatic Engineering",
      isLab = false,
      sectionC1Faculty = "M. L. Patra",
      sectionC2Faculty = "P. Nanda",
      facultyC1Full = "Prof. M.L. Patra",
      facultyC2Full = "Prof. P. Nanda"
    ),
    CourseFacultyMapping(
      code = "MA21001",
      shortName = "P&S",
      fullName = "Probability & Statistics",
      isLab = false,
      sectionC1Faculty = "Narmada Behera",
      sectionC2Faculty = "Narmada Behera",
      facultyC1Full = "Prof. Narmada Behera",
      facultyC2Full = "Prof. Narmada Behera"
    ),
    CourseFacultyMapping(
      code = "EX20003",
      shortName = "S&TW",
      fullName = "Scientific & Technical Writing",
      isLab = false,
      sectionC1Faculty = "K. P. Samal",
      sectionC2Faculty = "R. R. Thakur",
      facultyC1Full = "Prof. K.P. Samal",
      facultyC2Full = "Prof. R.R. Thakur"
    ),
    CourseFacultyMapping(
      code = "CE20007",
      shortName = "GIS & GPS",
      fullName = "Geographical Information System & GPS",
      isLab = false,
      sectionC1Faculty = "R. R. Thakur",
      sectionC2Faculty = "R. R. Thakur",
      facultyC1Full = "Prof. R.R. Thakur",
      facultyC2Full = "Prof. R.R. Thakur"
    ),
    CourseFacultyMapping(
      code = "CE29001",
      shortName = "EQA Lab",
      fullName = "Environmental Quality Analysis Laboratory",
      isLab = true,
      sectionC1Faculty = "All Sections",
      sectionC2Faculty = "All Sections",
      labGroup1Faculty = "Prof. Kundan Samal",
      labGroup2Faculty = "Prof. K. P. Samal",
      labGroup3Faculty = "Prof. Kundan Samal"
    ),
    CourseFacultyMapping(
      code = "CE29003",
      shortName = "MT Lab",
      fullName = "Material Testing Laboratory",
      isLab = true,
      sectionC1Faculty = "All Sections",
      sectionC2Faculty = "All Sections",
      labGroup1Faculty = "Prof. Tribikram Mohanty",
      labGroup2Faculty = "Prof. P. K. Acharya",
      labGroup3Faculty = "Prof. I. P. Mohanty"
    ),
    CourseFacultyMapping(
      code = "CE28001",
      shortName = "BDE&C",
      fullName = "Building Drawing, Estimation & Costing",
      isLab = true,
      sectionC1Faculty = "All Sections",
      sectionC2Faculty = "All Sections",
      labGroup1Faculty = "Prof. D. R. Biswal",
      labGroup2Faculty = "Prof. R. Patel",
      labGroup3Faculty = "Prof. N. C. Moharana"
    )
  )

  val courses = listOf(
    CourseInfo("CE21001", "FM", "Fluid Mechanics", "Theory", "Civil Block"),
    CourseInfo("CE20001", "SM", "Solid Mechanics", "Theory", "Civil Block"),
    CourseInfo("CE20003", "CPM", "Construction Project Management", "Theory", "Civil Block"),
    CourseInfo("CE20005", "S&G", "Surveying & Geomatic Engineering", "Theory", "Civil Block"),
    CourseInfo("MA21001", "P&S", "Probability & Statistics", "Theory", "Civil Block"),
    CourseInfo("EX20003", "S&TW", "Scientific & Technical Writing", "Theory", "Civil Block"),
    CourseInfo("CE29001", "EQA Lab", "Environmental Quality Analysis Laboratory", "Practical / Lab", "EQA Lab"),
    CourseInfo("CE29003", "MT Lab", "Material Testing Laboratory", "Practical / Lab", "MT Lab"),
    CourseInfo("CE28001", "BDE&C", "Building Drawing, Estimation & Costing", "Practical / Lab", "Room C5"),
    CourseInfo("CE20007", "GIS & GPS", "Geographical Information System & GPS", "Theory / Practice", "GIS Lab")
  )

  val facultyList = listOf(
    FacultyInfo("JPP", "Prof. J.P. Patra"),
    FacultyInfo("SNJ", "Prof. S. Jaiswal"),
    FacultyInfo("KS", "Prof. Kundan Samal"),
    FacultyInfo("BD", "Prof. B. Das"),
    FacultyInfo("NCM", "Prof. N. C. Moharana"),
    FacultyInfo("TBM", "Prof. Tribikram Mohanty"),
    FacultyInfo("PNN", "Prof. P. Nanda"),
    FacultyInfo("PMC", "Prof. Paromita Chakraborty"),
    FacultyInfo("DKB", "Prof. D.K. Bera"),
    FacultyInfo("PKA", "Prof. P. K. Acharya"),
    FacultyInfo("KPS", "Prof. K.P. Samal"),
    FacultyInfo("DRB", "Prof. D.R. Biswal"),
    FacultyInfo("NB", "Prof. Narmada Behera"),
    FacultyInfo("IPM", "Prof. I.P. Mohanty"),
    FacultyInfo("MLP", "Prof. M.L. Patra"),
    FacultyInfo("RP", "Prof. R. Patel"),
    FacultyInfo("RRT", "Prof. R.R. Thakur")
  )

  fun getFacultyName(initials: String): String {
    return facultyList.firstOrNull { it.initials.equals(initials.trim(), ignoreCase = true) }?.fullName
      ?: "Prof. $initials"
  }

  fun getDefaultClasses(): List<TimetableClass> {
    return listOf(
      // ==================== MONDAY (dayIndex = 1) ====================
      // 09:00am - 10:00am
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "CPM", subjectName = "Construction Project Management", subjectCode = "CE20003",
        facultyInitials = "PKA", facultyName = "Prof. P. K. Acharya",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "CPM"
      ),
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "CPM", subjectName = "Construction Project Management", subjectCode = "CE20003",
        facultyInitials = "DKB", facultyName = "Prof. D.K. Bera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "CPM"
      ),

      // 10:00am - 11:00am
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "JPP", facultyName = "Prof. J.P. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "PMC", facultyName = "Prof. Paromita Chakraborty",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),

      // 11:00am - 12:00 Noon
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "JPP", facultyName = "Prof. J.P. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "PMC", facultyName = "Prof. Paromita Chakraborty",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),

      // 12:00 Noon - 01:00pm
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "12:00 PM", endTime = "01:00 PM", startMinutes = 720, endMinutes = 780,
        subjectShort = "SM", subjectName = "Solid Mechanics", subjectCode = "CE20001",
        facultyInitials = "TBM", facultyName = "Prof. Tribikram Mohanty",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SM"
      ),
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "12:00 PM", endTime = "01:00 PM", startMinutes = 720, endMinutes = 780,
        subjectShort = "SM", subjectName = "Solid Mechanics", subjectCode = "CE20001",
        facultyInitials = "SNJ", facultyName = "Prof. S. Jaiswal",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SM"
      ),

      // 01:00pm - 02:00pm
      TimetableClass(
        dayOfWeek = "Monday", dayIndex = 1,
        startTime = "01:00 PM", endTime = "02:00 PM", startMinutes = 780, endMinutes = 840,
        subjectShort = "P&S", subjectName = "Probability & Statistics", subjectCode = "MA21001",
        facultyInitials = "NB", facultyName = "Prof. Narmada Behera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "PS"
      ),

      // ==================== TUESDAY (dayIndex = 2) ====================
      // 09:00am - 10:00am
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "JPP", facultyName = "Prof. J.P. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "PMC", facultyName = "Prof. Paromita Chakraborty",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),

      // 10:00am - 11:00am
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "P&S", subjectName = "Probability & Statistics", subjectCode = "MA21001",
        facultyInitials = "NB", facultyName = "Prof. Narmada Behera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "PS"
      ),

      // 11:00am - 12:00 Noon
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "S&TW", subjectName = "Scientific & Technical Writing", subjectCode = "EX20003",
        facultyInitials = "KPS", facultyName = "Prof. K.P. Samal",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "STW"
      ),
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "S&TW", subjectName = "Scientific & Technical Writing", subjectCode = "EX20003",
        facultyInitials = "RRT", facultyName = "Prof. R.R. Thakur",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "STW"
      ),

      // 12:00 Noon - 03:00pm Labs (3-hr block)
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "EQA Lab", subjectName = "Environmental Quality Analysis Laboratory", subjectCode = "CE29001",
        facultyInitials = "KS", facultyName = "Prof. Kundan Samal",
        section = "ALL", labGroup = "GR3", room = "EQA Lab", isLab = true, colorKey = "LAB"
      ),
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "MT Lab", subjectName = "Material Testing Laboratory", subjectCode = "CE29003",
        facultyInitials = "PKA", facultyName = "Prof. P. K. Acharya",
        section = "ALL", labGroup = "GR2", room = "MT Lab", isLab = true, colorKey = "LAB"
      ),
      TimetableClass(
        dayOfWeek = "Tuesday", dayIndex = 2,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "BDE&C", subjectName = "Building Drawing, Estimation & Costing", subjectCode = "CE28001",
        facultyInitials = "DRB", facultyName = "Prof. D.R. Biswal",
        section = "ALL", labGroup = "GR1", room = "Room C5", isLab = true, colorKey = "LAB"
      ),

      // ==================== WEDNESDAY (dayIndex = 3) ====================
      // 09:00am - 10:00am
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "P&S", subjectName = "Probability & Statistics", subjectCode = "MA21001",
        facultyInitials = "NB", facultyName = "Prof. Narmada Behera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "PS"
      ),

      // 10:00am - 11:00am
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "SM", subjectName = "Solid Mechanics", subjectCode = "CE20001",
        facultyInitials = "TBM", facultyName = "Prof. Tribikram Mohanty",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SM"
      ),
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "SM", subjectName = "Solid Mechanics", subjectCode = "CE20001",
        facultyInitials = "SNJ", facultyName = "Prof. S. Jaiswal",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SM"
      ),

      // 11:00am - 12:00 Noon
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "CPM", subjectName = "Construction Project Management", subjectCode = "CE20003",
        facultyInitials = "PKA", facultyName = "Prof. P. K. Acharya",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "CPM"
      ),
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "CPM", subjectName = "Construction Project Management", subjectCode = "CE20003",
        facultyInitials = "DKB", facultyName = "Prof. D.K. Bera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "CPM"
      ),

      // 12:00 Noon - 03:00pm Labs (3-hr block)
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "EQA Lab", subjectName = "Environmental Quality Analysis Laboratory", subjectCode = "CE29001",
        facultyInitials = "KS", facultyName = "Prof. Kundan Samal",
        section = "ALL", labGroup = "GR1", room = "EQA Lab", isLab = true, colorKey = "LAB"
      ),
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "MT Lab", subjectName = "Material Testing Laboratory", subjectCode = "CE29003",
        facultyInitials = "IPM", facultyName = "Prof. I.P. Mohanty",
        section = "ALL", labGroup = "GR3", room = "MT Lab", isLab = true, colorKey = "LAB"
      ),
      TimetableClass(
        dayOfWeek = "Wednesday", dayIndex = 3,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "BDE&C", subjectName = "Building Drawing, Estimation & Costing", subjectCode = "CE28001",
        facultyInitials = "RP", facultyName = "Prof. R. Patel",
        section = "ALL", labGroup = "GR2", room = "Room C5", isLab = true, colorKey = "LAB"
      ),

      // ==================== THURSDAY (dayIndex = 4) ====================
      // 09:00am - 10:00am
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "S&G", subjectName = "Surveying & Geomatic Engineering", subjectCode = "CE20005",
        facultyInitials = "MLP", facultyName = "Prof. M.L. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SG"
      ),
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "S&G", subjectName = "Surveying & Geomatic Engineering", subjectCode = "CE20005",
        facultyInitials = "PNN", facultyName = "Prof. P. Nanda",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SG"
      ),

      // 10:00am - 11:00am
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "CPM", subjectName = "Construction Project Management", subjectCode = "CE20003",
        facultyInitials = "PKA", facultyName = "Prof. P. K. Acharya",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "CPM"
      ),
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "CPM", subjectName = "Construction Project Management", subjectCode = "CE20003",
        facultyInitials = "DKB", facultyName = "Prof. D.K. Bera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "CPM"
      ),

      // 11:00am - 12:00 Noon
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "S&TW", subjectName = "Scientific & Technical Writing", subjectCode = "EX20003",
        facultyInitials = "KPS", facultyName = "Prof. K.P. Samal",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "STW"
      ),
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "S&TW", subjectName = "Scientific & Technical Writing", subjectCode = "EX20003",
        facultyInitials = "RRT", facultyName = "Prof. R.R. Thakur",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "STW"
      ),

      // 12:00 Noon - 01:00pm
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "12:00 PM", endTime = "01:00 PM", startMinutes = 720, endMinutes = 780,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "JPP", facultyName = "Prof. J.P. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "12:00 PM", endTime = "01:00 PM", startMinutes = 720, endMinutes = 780,
        subjectShort = "FM", subjectName = "Fluid Mechanics", subjectCode = "CE21001",
        facultyInitials = "PMC", facultyName = "Prof. Paromita Chakraborty",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "FM"
      ),

      // 01:00pm - 02:00pm
      TimetableClass(
        dayOfWeek = "Thursday", dayIndex = 4,
        startTime = "01:00 PM", endTime = "02:00 PM", startMinutes = 780, endMinutes = 840,
        subjectShort = "P&S", subjectName = "Probability & Statistics", subjectCode = "MA21001",
        facultyInitials = "NB", facultyName = "Prof. Narmada Behera",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "PS"
      ),

      // ==================== FRIDAY (dayIndex = 5) ====================
      // 09:00am - 10:00am
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "S&G", subjectName = "Surveying & Geomatic Engineering", subjectCode = "CE20005",
        facultyInitials = "MLP", facultyName = "Prof. M.L. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SG"
      ),
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "09:00 AM", endTime = "10:00 AM", startMinutes = 540, endMinutes = 600,
        subjectShort = "S&G", subjectName = "Surveying & Geomatic Engineering", subjectCode = "CE20005",
        facultyInitials = "PNN", facultyName = "Prof. P. Nanda",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SG"
      ),

      // 10:00am - 11:00am
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "S&G", subjectName = "Surveying & Geomatic Engineering", subjectCode = "CE20005",
        facultyInitials = "MLP", facultyName = "Prof. M.L. Patra",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SG"
      ),
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "10:00 AM", endTime = "11:00 AM", startMinutes = 600, endMinutes = 660,
        subjectShort = "S&G", subjectName = "Surveying & Geomatic Engineering", subjectCode = "CE20005",
        facultyInitials = "PNN", facultyName = "Prof. P. Nanda",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SG"
      ),

      // 11:00am - 12:00 Noon
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "SM", subjectName = "Solid Mechanics", subjectCode = "CE20001",
        facultyInitials = "TBM", facultyName = "Prof. Tribikram Mohanty",
        section = "C1", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SM"
      ),
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "11:00 AM", endTime = "12:00 PM", startMinutes = 660, endMinutes = 720,
        subjectShort = "SM", subjectName = "Solid Mechanics", subjectCode = "CE20001",
        facultyInitials = "SNJ", facultyName = "Prof. S. Jaiswal",
        section = "C2", labGroup = "ALL", room = "Civil Block", isLab = false, colorKey = "SM"
      ),

      // 12:00 Noon - 03:00pm Labs (3-hr block)
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "EQA Lab", subjectName = "Environmental Quality Analysis Laboratory", subjectCode = "CE29001",
        facultyInitials = "KPS", facultyName = "Prof. K.P. Samal",
        section = "ALL", labGroup = "GR2", room = "EQA Lab", isLab = true, colorKey = "LAB"
      ),
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "MT Lab", subjectName = "Material Testing Laboratory", subjectCode = "CE29003",
        facultyInitials = "TBM", facultyName = "Prof. Tribikram Mohanty",
        section = "ALL", labGroup = "GR1", room = "MT Lab", isLab = true, colorKey = "LAB"
      ),
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "12:00 PM", endTime = "03:00 PM", startMinutes = 720, endMinutes = 900,
        subjectShort = "BDE&C", subjectName = "Building Drawing, Estimation & Costing", subjectCode = "CE28001",
        facultyInitials = "NCM", facultyName = "Prof. N. C. Moharana",
        section = "ALL", labGroup = "GR3", room = "Room C5", isLab = true, colorKey = "LAB"
      ),

      // 03:00pm - 05:00pm
      TimetableClass(
        dayOfWeek = "Friday", dayIndex = 5,
        startTime = "03:00 PM", endTime = "05:00 PM", startMinutes = 900, endMinutes = 1020,
        subjectShort = "GIS & GPS", subjectName = "Geographical Information System & GPS", subjectCode = "CE20007",
        facultyInitials = "RRT", facultyName = "Prof. R.R. Thakur",
        section = "C1", labGroup = "ALL", room = "GIS Lab", isLab = false, colorKey = "GIS"
      )
    )
  }
}
