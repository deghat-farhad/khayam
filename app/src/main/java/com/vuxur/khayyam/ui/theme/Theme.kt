package com.vuxur.khayyam.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = mutedReddishBrown,
    onPrimary = creamyBeige,
    primaryContainer = mutedReddishBrown,
    onPrimaryContainer = creamyBeige,
    inversePrimary = creamyBeige,
    secondary = earthyBrown,
    onSecondary = creamyBeige,
    secondaryContainer = earthyBrown,
    onSecondaryContainer = creamyBeige,
    tertiary = earthyBrown,
    onTertiary = creamyBeige,
    tertiaryContainer = earthyBrown,
    onTertiaryContainer = creamyBeige,
    background = creamyBeige,
    onBackground = mutedGrayishBrown,
    surface = mutedGrayishBrown,
    onSurface = earthyBrown,
    surfaceVariant = mutedGrayishBrown,
    onSurfaceVariant = earthyBrown,
    surfaceTint = mutedReddishBrown,
    inverseSurface = earthyBrown,
    inverseOnSurface = mutedGrayishBrown,
    outline = earthyBrown,
    outlineVariant = mutedGrayishBrown,
    scrim = Color.Gray,
    error = deepPlum,
)

private val DarkColorScheme = darkColorScheme(
    primary = dustyRose,
    onPrimary = lightLavender,
    primaryContainer = dustyRose,
    onPrimaryContainer = lightLavender,
    inversePrimary = lightLavender,
    secondary = mutedRose,
    onSecondary = dustyRose,
    secondaryContainer = mutedRose,
    onSecondaryContainer = dustyRose,
    tertiary = mutedRose,
    onTertiary = dustyRose,
    tertiaryContainer = mutedRose,
    onTertiaryContainer = dustyRose,
    background = mutedMidnightBlue,
    onBackground = deepIndigo,
    surface = deepIndigo,
    onSurface = lightLavender,
    surfaceVariant = deepIndigo,
    onSurfaceVariant = lightLavender,
    surfaceTint = dustyRose,
    inverseSurface = lightLavender,
    inverseOnSurface = deepIndigo,
    outline = lightLavender,
    outlineVariant = deepIndigo,
    scrim = Color.Gray,
    error = softBlush,
)

@Composable
fun KhayyamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}