package com.kelvin.composeconcepts.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * L-W9-8：Navigation Compose 與 Material3 Theming
 *
 * MaterialTheme 把「顏色 / 字型 / 形狀」這三件事往下傳給整棵 Compose tree。
 * 底層的原理就是 L-W9-8 提過的 CompositionLocal——這裡的
 * MaterialTheme(colorScheme = ..., typography = ...) { content() }
 * 本質上是包了一層 CompositionLocalProvider，讓子樹裡任何地方呼叫
 * MaterialTheme.colorScheme.primary 都能「隱式」拿到值，不需要每層手動往下傳參數。
 */

private val LightColors = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
)

private val DarkColors = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

@Composable
fun ComposeConceptsDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic Color：Android 12+ 可以從使用者桌布提取主色，讓 App 配色跟系統一致。
    // 這裡示範怎麼判斷、但預設關閉，讓範例的顏色在各版本裝置上都一致方便你截圖對照。
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    // SideEffect（L-W9-5 教過）：這裡示範它「同步 Compose 內部狀態給非 Compose 系統」的
    // 典型用法——把主題色同步寫進 Activity 的 status bar 顏色（這是 View 世界的 API，
    // 不是 Compose 的一部分），每次成功 recomposition 後執行一次即可，不需要 coroutine。
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window ?: return@SideEffect, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
