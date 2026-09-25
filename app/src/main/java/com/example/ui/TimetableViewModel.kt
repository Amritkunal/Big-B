package com.example.ui

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.defaultdata.KiitCivilTimetableData
import com.example.data.local.AppDatabase
import com.example.data.model.TimetableClass
import com.example.data.repository.TimetableRepository
import com.example.util.PdfTimetableParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

data class TimetableUiState(
  val selectedDayIndex: Int = 1,
  val selectedSection: String = "C1", // "C1", "C2", "ALL"
  val selectedLabGroup: String = "GR1", // "GR1", "GR2", "GR3"
  val activeTab: Int = 0, // 0: Classes, 1: Faculty, 2: Week Matrix
  val searchQuery: String = "",
  val isAddEditDialogOpen: Boolean = false,
  val editingClass: TimetableClass? = null,
  val isPdfPreviewOpen: Boolean = false,
  val isResetConfirmOpen: Boolean = false,
  val isLabGroupPickerOpen: Boolean = false,
  val snackbarMessage: String? = null
)

class TimetableViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: TimetableRepository
  private val prefs = application.getSharedPreferences("kiit_timetable_prefs", Context.MODE_PRIVATE)

  private val _uiState = MutableStateFlow(
    TimetableUiState(
      selectedDayIndex = getTodayDayIndex(),
      selectedLabGroup = prefs.getString("selected_lab_group", "GR1") ?: "GR1"
    )
  )
  val uiState: StateFlow<TimetableUiState> = _uiState.asStateFlow()

  private val _uploadedPdfBitmap = MutableStateFlow<Bitmap?>(null)
  val uploadedPdfBitmap: StateFlow<Bitmap?> = _uploadedPdfBitmap.asStateFlow()

  private val _uploadedFileName = MutableStateFlow<String?>("KIIT_Civil_3rdSem_Rev3.pdf")
  val uploadedFileName: StateFlow<String?> = _uploadedFileName.asStateFlow()

  private val _isProcessingUpload = MutableStateFlow(false)
  val isProcessingUpload: StateFlow<Boolean> = _isProcessingUpload.asStateFlow()

  init {
    val db = AppDatabase.getDatabase(application)
    repository = TimetableRepository(db.timetableDao())
    viewModelScope.launch {
      repository.ensureDefaultData()
      // Check if previously stored PDF exists
      val savedPdf = File(application.filesDir, "uploaded_timetable.pdf")
      if (savedPdf.exists()) {
        val bitmap = PdfTimetableParser.renderPdfToBitmap(savedPdf)
        if (bitmap != null) {
          _uploadedPdfBitmap.value = bitmap
          _uploadedFileName.value = "uploaded_timetable.pdf"
        }
      }
    }
  }

  val allClasses: StateFlow<List<TimetableClass>> = repository.allClasses
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  // Filtered classes for the currently active day, section, and lab group
  val dayClasses: StateFlow<List<TimetableClass>> = combine(
    allClasses,
    _uiState
  ) { classes, state ->
    classes.filter { item ->
      val matchesDay = item.dayIndex == state.selectedDayIndex
      val matchesSection = state.selectedSection == "ALL" || item.section == "ALL" || item.section.equals(state.selectedSection, ignoreCase = true)
      val matchesGroup = state.selectedLabGroup == "ALL" || item.labGroup == "ALL" || item.labGroup.equals(state.selectedLabGroup, ignoreCase = true)
      val matchesSearch = state.searchQuery.isBlank() ||
        item.subjectName.contains(state.searchQuery, ignoreCase = true) ||
        item.subjectShort.contains(state.searchQuery, ignoreCase = true) ||
        item.subjectCode.contains(state.searchQuery, ignoreCase = true) ||
        item.facultyName.contains(state.searchQuery, ignoreCase = true)

      matchesDay && matchesSection && matchesGroup && matchesSearch
    }.sortedBy { it.startMinutes }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  // Minimal classes for Section 1:
  // Shows classes only (C1 and C2 have same classes, faculties differ).
  // Lab classes show the user's saved lab group (one-time choice, persisted).
  // Uses distinctUntilChanged on day & group to avoid recomputing on unrelated state changes (like tab or dialog changes).
  private val minimalFilterFlow = _uiState
    .map { Pair(it.selectedDayIndex, it.selectedLabGroup) }
    .distinctUntilChanged()

  val minimalDayClasses: StateFlow<List<TimetableClass>> = combine(
    allClasses,
    minimalFilterFlow
  ) { classes, (selectedDayIndex, selectedLabGroup) ->
    val dayItems = classes.filter { it.dayIndex == selectedDayIndex }
    val theoryClasses = dayItems
      .filter { !it.isLab }
      .distinctBy { "${it.startMinutes}_${it.subjectShort}" }
    val labClasses = dayItems
      .filter { it.isLab }
      .filter { it.labGroup == "ALL" || it.labGroup.equals(selectedLabGroup, ignoreCase = true) }
      .distinctBy { "${it.startMinutes}_${it.subjectShort}" }
    (theoryClasses + labClasses).sortedBy { it.startMinutes }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  fun selectDay(dayIndex: Int) {
    _uiState.value = _uiState.value.copy(selectedDayIndex = dayIndex)
  }

  fun selectSection(section: String) {
    _uiState.value = _uiState.value.copy(selectedSection = section)
  }

  fun selectLabGroup(group: String) {
    prefs.edit().putString("selected_lab_group", group).apply()
    _uiState.value = _uiState.value.copy(
      selectedLabGroup = group,
      isLabGroupPickerOpen = false,
      snackbarMessage = "Lab group set to Group ${group.removePrefix("GR")}. Saved permanently!"
    )
  }

  fun openLabGroupPicker() {
    _uiState.value = _uiState.value.copy(isLabGroupPickerOpen = true)
  }

  fun closeLabGroupPicker() {
    _uiState.value = _uiState.value.copy(isLabGroupPickerOpen = false)
  }

  fun setActiveTab(tab: Int) {
    _uiState.value = _uiState.value.copy(activeTab = tab)
  }

  fun setSearchQuery(query: String) {
    _uiState.value = _uiState.value.copy(searchQuery = query)
  }

  fun openAddDialog() {
    _uiState.value = _uiState.value.copy(
      isAddEditDialogOpen = true,
      editingClass = null
    )
  }

  fun openEditDialog(timetableClass: TimetableClass) {
    _uiState.value = _uiState.value.copy(
      isAddEditDialogOpen = true,
      editingClass = timetableClass
    )
  }

  fun closeAddEditDialog() {
    _uiState.value = _uiState.value.copy(
      isAddEditDialogOpen = false,
      editingClass = null
    )
  }

  fun openPdfPreview() {
    _uiState.value = _uiState.value.copy(isPdfPreviewOpen = true)
  }

  fun closePdfPreview() {
    _uiState.value = _uiState.value.copy(isPdfPreviewOpen = false)
  }

  fun setResetConfirmOpen(open: Boolean) {
    _uiState.value = _uiState.value.copy(isResetConfirmOpen = open)
  }

  fun clearSnackbar() {
    _uiState.value = _uiState.value.copy(snackbarMessage = null)
  }

  fun saveClass(
    id: Long,
    dayOfWeek: String,
    dayIndex: Int,
    startTime: String,
    endTime: String,
    startMinutes: Int,
    endMinutes: Int,
    subjectShort: String,
    subjectName: String,
    subjectCode: String,
    facultyInitials: String,
    facultyName: String,
    section: String,
    labGroup: String,
    room: String,
    isLab: Boolean
  ) {
    viewModelScope.launch {
      val item = TimetableClass(
        id = id,
        dayOfWeek = dayOfWeek,
        dayIndex = dayIndex,
        startTime = startTime,
        endTime = endTime,
        startMinutes = startMinutes,
        endMinutes = endMinutes,
        subjectShort = subjectShort.trim(),
        subjectName = subjectName.trim(),
        subjectCode = subjectCode.trim(),
        facultyInitials = facultyInitials.trim(),
        facultyName = facultyName.trim().ifEmpty { KiitCivilTimetableData.getFacultyName(facultyInitials) },
        section = section,
        labGroup = labGroup,
        room = room.trim().ifEmpty { "Civil Block" },
        isLab = isLab,
        colorKey = when {
          isLab -> "LAB"
          subjectShort.contains("FM", ignoreCase = true) -> "FM"
          subjectShort.contains("SM", ignoreCase = true) -> "SM"
          subjectShort.contains("CPM", ignoreCase = true) -> "CPM"
          subjectShort.contains("S&G", ignoreCase = true) -> "SG"
          subjectShort.contains("P&S", ignoreCase = true) -> "PS"
          subjectShort.contains("S&TW", ignoreCase = true) -> "STW"
          subjectShort.contains("GIS", ignoreCase = true) -> "GIS"
          else -> "GENERAL"
        }
      )
      if (id == 0L) {
        repository.insertClass(item)
        _uiState.value = _uiState.value.copy(
          isAddEditDialogOpen = false,
          snackbarMessage = "Added ${item.subjectShort} for $dayOfWeek"
        )
      } else {
        repository.updateClass(item)
        _uiState.value = _uiState.value.copy(
          isAddEditDialogOpen = false,
          snackbarMessage = "Updated ${item.subjectShort}"
        )
      }
    }
  }

  fun deleteClass(timetableClass: TimetableClass) {
    viewModelScope.launch {
      repository.deleteClass(timetableClass)
      _uiState.value = _uiState.value.copy(
        snackbarMessage = "Deleted ${timetableClass.subjectShort} (${timetableClass.dayOfWeek})"
      )
    }
  }

  fun resetToDefaultKiitSchedule() {
    viewModelScope.launch {
      repository.resetToDefaultKiitSchedule()
      _uiState.value = _uiState.value.copy(
        isResetConfirmOpen = false,
        snackbarMessage = "Reset to official KIIT Civil Engineering 3rd Sem Timetable!"
      )
    }
  }

  fun handleUploadedFile(uri: Uri, displayName: String?) {
    viewModelScope.launch(Dispatchers.IO) {
      _isProcessingUpload.value = true
      val app = getApplication<Application>()
      val savedFile = PdfTimetableParser.copyUriToLocalStorage(app, uri, "uploaded_timetable.pdf")
      if (savedFile != null) {
        val bitmap = PdfTimetableParser.renderPdfToBitmap(savedFile)
        _uploadedPdfBitmap.value = bitmap
        _uploadedFileName.value = displayName ?: "uploaded_timetable.pdf"
        _uiState.value = _uiState.value.copy(
          isPdfPreviewOpen = true,
          snackbarMessage = "Uploaded timetable: ${displayName ?: "PDF"}! Document preview ready."
        )
      } else {
        _uiState.value = _uiState.value.copy(
          snackbarMessage = "Unable to read uploaded file. You can still manually manage classes."
        )
      }
      _isProcessingUpload.value = false
    }
  }

  companion object {
    fun getTodayDayIndex(): Int {
      return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        else -> 1 // Saturday and Sunday default to Monday (Mon-Fri only)
      }
    }
  }
}
