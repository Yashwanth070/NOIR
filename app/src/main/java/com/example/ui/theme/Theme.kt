package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = TelegramDarkBluePrimary,
    secondary = TelegramDarkBlueActive,
    background = TelegramDarkBg,
    surface = TelegramDarkSurface,
    onPrimary = TelegramDarkTextPrimary,
    onSecondary = TelegramDarkTextPrimary,
    onBackground = TelegramDarkTextPrimary,
    onSurface = TelegramDarkTextPrimary,
    surfaceVariant = TelegramDarkBubbleOther
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TelegramLightBluePrimary,
    secondary = TelegramLightBlueActive,
    background = TelegramLightBg,
    surface = TelegramLightSurface,
    onPrimary = TelegramLightTextPrimary,
    onSecondary = TelegramLightTextPrimary,
    onBackground = TelegramLightTextPrimary,
    onSurface = TelegramLightTextPrimary,
    surfaceVariant = TelegramLightBubbleOther
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disabling dynamic colors to guarantee custom Telegram aesthetics
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
