package com.example.data.repository

import com.example.data.defaultdata.CivilTimetableData
import com.example.data.local.TimetableDao
import com.example.data.model.TimetableClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TimetableRepository(private val timetableDao: TimetableDao) {

  val allClasses: Flow<List<TimetableClass>> = timetableDao.getAllClasses()

  fun getClassesForDay(dayIndex: Int): Flow<List<TimetableClass>> =
    timetableDao.getClassesForDay(dayIndex)

  suspend fun ensureDefaultData() = withContext(Dispatchers.IO) {
    if (timetableDao.getClassCount() == 0) {
      timetableDao.insertClasses(CivilTimetableData.getDefaultClasses())
    } else {
      timetableDao.syncFmC1Faculty()
    }
  }

  suspend fun resetToDefaultSchedule() = withContext(Dispatchers.IO) {
    timetableDao.clearAllClasses()
    timetableDao.insertClasses(CivilTimetableData.getDefaultClasses())
  }

  suspend fun insertClass(timetableClass: TimetableClass) = withContext(Dispatchers.IO) {
    timetableDao.insertClass(timetableClass)
  }

  suspend fun insertClasses(classes: List<TimetableClass>) = withContext(Dispatchers.IO) {
    timetableDao.insertClasses(classes)
  }

  suspend fun updateClass(timetableClass: TimetableClass) = withContext(Dispatchers.IO) {
    timetableDao.updateClass(timetableClass)
  }

  suspend fun deleteClass(timetableClass: TimetableClass) = withContext(Dispatchers.IO) {
    timetableDao.deleteClass(timetableClass)
  }

  suspend fun deleteClassById(id: Long) = withContext(Dispatchers.IO) {
    timetableDao.deleteClassById(id)
  }

  suspend fun clearAll() = withContext(Dispatchers.IO) {
    timetableDao.clearAllClasses()
  }
}
