package com.kelvin.composeconcepts.legacyview

import android.content.Context
import android.graphics.Color
import android.widget.TextView

/**
 * 這是一個刻意寫成「傳統命令式風格」的 View，純粹是為了讓 L-W9-4（Compose/View 互操作）
 * 有一個真實的舊 View 可以嵌進 Compose 裡當範例——想像它是專案裡「還沒遷移成 Compose」
 * 的舊元件（例如某個第三方 SDK 提供的 View，或還沒空重寫的舊功能）。
 *
 * 注意這裡完全是命令式寫法：外部要透過呼叫 setCount() 這個方法來「改變」畫面，
 * 跟 Compose 世界「重新描述 state」的思維完全不同——這正是 L-W9-1 對比的那兩種心智模型。
 */
class LegacyCounterView(context: Context) : TextView(context) {
    private var count = 0

    init {
        setBackgroundColor(Color.parseColor("#FFE0B2"))
        setTextColor(Color.parseColor("#4A1B0C"))
        setPadding(24, 24, 24, 24)
        textSize = 16f
        renderText()
    }

    /** 命令式 API：呼叫這個方法「改變」畫面上顯示的文字，跟 Compose 的宣告式完全相反。 */
    fun setCount(newCount: Int) {
        if (count == newCount) return
        count = newCount
        renderText()
    }

    private fun renderText() {
        text = "我是傳統 View（LegacyCounterView）\n目前收到的 count：$count\n" +
            "（這段文字是靠呼叫 setCount() 手動更新的，不是靠 Compose 的 recomposition）"
    }
}
