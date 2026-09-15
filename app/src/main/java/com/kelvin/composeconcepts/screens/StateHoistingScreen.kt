package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * L-W9-2：State Hoisting 與 remember / rememberSaveable
 *
 * 這頁刻意放兩個看起來一樣的計數器，但底層存 state 的方式不同：
 * - 上面用 remember：旋轉螢幕（configuration change）後會被重設成 0
 * - 下面用 rememberSaveable：旋轉螢幕後值還在
 *
 * 實機示範方式：把手機轉橫向（或在模擬器按 Ctrl+F11 / Cmd+Fn+F11 模擬旋轉），
 * 你會親眼看到兩個計數器的差異——這是文字說明很難讓人「有感」的地方，
 * 但親手轉一次螢幕就會馬上理解為什麼要分兩種 API。
 */
@Composable
fun StateHoistingScreen(onBack: () -> Unit) {
    ScreenScaffold(title = "L-W9-2 State Hoisting", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {
            Text(
                "把手機/模擬器旋轉螢幕，觀察兩個計數器的差異：",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Text("① remember（旋轉後會重設為 0）", style = MaterialTheme.typography.titleSmall)
            RememberCounter()

            HorizontalDivider(Modifier.padding(vertical = 20.dp))

            Text("② rememberSaveable（旋轉後保留數值）", style = MaterialTheme.typography.titleSmall)
            RememberSaveableCounter()

            HorizontalDivider(Modifier.padding(vertical = 20.dp))

            Text("③ 已 Hoist 的版本（無狀態元件 + 外部持有 state）", style = MaterialTheme.typography.titleSmall)
            HoistedCounterDemo()
        }
    }
}

/** remember：值存在 Composition 的記憶體裡，config change 會讓整個 Composition 被丟棄重建。 */
@Composable
private fun RememberCounter() {
    var count by remember { mutableIntStateOf(0) }
    CounterRow(count = count, onIncrement = { count++ })
}

/**
 * rememberSaveable：除了跟 remember 一樣存進 slot table，還會額外寫進
 * SavedStateRegistry（跟傳統 View 的 onSaveInstanceState 是同一套機制），
 * 所以能撐過旋轉螢幕、甚至系統因記憶體不足殺掉 process 後又還原的情況。
 */
@Composable
private fun RememberSaveableCounter() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    CounterRow(count = count, onIncrement = { count++ })
}

@Composable
private fun CounterRow(count: Int, onIncrement: () -> Unit) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text("目前值：$count", modifier = Modifier.padding(end = 16.dp))
        Button(onClick = onIncrement) { Text("+1") }
    }
}

/**
 * 這一段示範「State Hoisting Pattern」本身：
 * - HoistedCounter 是完全無狀態（stateless）的元件，只接收 count 參數、
 *   透過 onIncrement callback 回報事件——它自己完全不持有任何 state。
 * - HoistedCounterDemo 才是真正的 state 持有者。
 *
 * 好處：HoistedCounter 現在可以被獨立 @Preview、獨立寫單元測試（傳入任意 count
 * 驗證顯示是否正確），也更容易在別的畫面重用，因為它的輸出完全由輸入決定。
 */
@Composable
private fun HoistedCounterDemo() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    HoistedCounter(count = count, onIncrement = { count++ }, onReset = { count = 0 })
}

@Composable
private fun HoistedCounter(
    count: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Text("Hoisted 版本：$count", modifier = Modifier.padding(end = 8.dp))
        Button(onClick = onIncrement) { Text("+1") }
        Button(onClick = onReset) { Text("重設") }
    }
}
