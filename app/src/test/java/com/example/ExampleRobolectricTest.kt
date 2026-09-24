package com.example

import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import com.example.util.AppHaptics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("KIIT Civil Timetable", appName)
  }

  @Test
  fun `manifest has vibrate permission`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val packageInfo = context.packageManager.getPackageInfo(
      context.packageName,
      PackageManager.GET_PERMISSIONS
    )
    val permissions = packageInfo.requestedPermissions?.toList().orEmpty()
    assertTrue(permissions.contains(android.Manifest.permission.VIBRATE))
  }

  @Test
  fun `app haptics execute safely without throwing`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val haptics = AppHaptics(view = null, composeHaptic = null, context = context)
    assertNotNull(haptics)
    // Verify methods execute safely without crash
    haptics.sectionSwitch()
    haptics.groupSelect()
    haptics.click()
  }
}
