package com.kelvin.composeconcepts.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

// Material3 的字型規範（Typography）。內建元件（Text 預設樣式、Button 文字等）
// 會自動讀取這裡的設定，不需要每個 Text 都手動指定 fontSize。
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )
)
