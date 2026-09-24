package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TimetableClass
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

  @Query("SELECT * FROM timetable_classes ORDER BY dayIndex ASC, startMinutes ASC")
  fun getAllClasses(): Flow<List<TimetableClass>>

  @Query("SELECT * FROM timetable_classes WHERE dayIndex = :dayIndex ORDER BY startMinutes ASC")
  fun getClassesForDay(dayIndex: Int): Flow<List<TimetableClass>>

  @Query("SELECT COUNT(*) FROM timetable_classes")
  suspend fun getClassCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClass(timetableClass: TimetableClass): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClasses(classes: List<TimetableClass>)

  @Update
  suspend fun updateClass(timetableClass: TimetableClass)

  @Delete
  suspend fun deleteClass(timetableClass: TimetableClass)

  @Query("DELETE FROM timetable_classes WHERE id = :id")
  suspend fun deleteClassById(id: Long)

  @Query("DELETE FROM timetable_classes")
  suspend fun clearAllClasses()

  @Query("UPDATE timetable_classes SET facultyInitials = 'JPP', facultyName = 'Prof. J.P. Patra' WHERE subjectShort = 'FM' AND section = 'C1'")
  suspend fun syncFmC1Faculty()
}
