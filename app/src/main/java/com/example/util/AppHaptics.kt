package com.example.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView

/**
 * Provides tactile, premium haptic feedback for app interactions like section switching,
 * group selection, and interactive element taps.
 */
class AppHaptics(
  private val view: View?,
  private val composeHaptic: HapticFeedback?,
  private val context: Context?
) {

  /**
   * Tactile feedback when switching between the 3 main sections of the app.
   * Produces a clean, subtle, and responsive tick sensation.
   */
  fun sectionSwitch() {
    var performed = false
    try {
      if (view != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          performed = view.performHapticFeedback(
            HapticFeedbackConstants.GESTURE_START,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
          )
        }
        if (!performed) {
          performed = view.performHapticFeedback(
            HapticFeedbackConstants.KEYBOARD_TAP,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
          )
        }
      }
    } catch (_: Throwable) {}

    try {
      composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    } catch (_: Throwable) {}

    if (!performed) {
      vibrateFallback(durationMs = 15, effect = VibrationEffect.EFFECT_CLICK)
    }
  }

  /**
   * Satisfying, tactile confirmation feedback when the user selects or updates their lab group.
   */
  fun groupSelect() {
    var performed = false
    try {
      if (view != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          performed = view.performHapticFeedback(
            HapticFeedbackConstants.CONFIRM,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
          )
        }
        if (!performed) {
          performed = view.performHapticFeedback(
            HapticFeedbackConstants.KEYBOARD_TAP,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
          )
        }
      }
    } catch (_: Throwable) {}

    try {
      composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    } catch (_: Throwable) {}

    if (!performed) {
      vibrateFallback(durationMs = 25, effect = VibrationEffect.EFFECT_CLICK)
    }
  }

  /**
   * Light click feedback for buttons, chips, and quick-access toggles.
   */
  fun click() {
    var performed = false
    try {
      if (view != null) {
        performed = view.performHapticFeedback(
          HapticFeedbackConstants.KEYBOARD_TAP,
          HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
      }
    } catch (_: Throwable) {}

    try {
      composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    } catch (_: Throwable) {}

    if (!performed) {
      vibrateFallback(durationMs = 12, effect = VibrationEffect.EFFECT_TICK)
    }
  }

  private fun vibrateFallback(durationMs: Long, effect: Int) {
    if (context == null) return
    try {
      val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vm?.defaultVibrator
      } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
      }

      if (vibrator != null && vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          vibrator.vibrate(VibrationEffect.createPredefined(effect))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
          @Suppress("DEPRECATION")
          vibrator.vibrate(durationMs)
        }
      }
    } catch (_: Throwable) {}
  }
}

/**
 * Creates and remembers an [AppHaptics] instance tied to the current Compose composition.
 */
@Composable
fun rememberAppHaptics(): AppHaptics {
  val view = LocalView.current
  val composeHaptic = LocalHapticFeedback.current
  val context = LocalContext.current
  return remember(view, composeHaptic, context) {
    AppHaptics(view, composeHaptic, context)
  }
}
