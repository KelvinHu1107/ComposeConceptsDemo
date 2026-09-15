package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kelvin.composeconcepts.legacyview.LegacyCounterView

/**
 * L-W9-4：Compose 與傳統 View 互操作
 *
 * 大型 legacy app 不可能一次把所有畫面重寫成 Compose，實務上是逐畫面漸進遷移。
 * 這頁示範 AndroidView：在 Compose tree 裡「嵌入」一個傳統 View（LegacyCounterView），
 * 讓 Compose 世界的 state（count）可以同步進去這個舊 View。
 */
@Composable
fun InteropScreen(onBack: () -> Unit) {
    ScreenScaffold(title = "L-W9-4 Compose/View 互操作", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {
            var count by remember { mutableIntStateOf(0) }

            Text(
                "下面那一塊橘色背景不是 Compose 畫的，是一個真正的 Android View " +
                    "（LegacyCounterView，繼承 TextView）。按按鈕會改變 Compose 這邊的 " +
                    "count（Compose state），透過 AndroidView 的 update 區塊同步進舊 View。",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Button(onClick = { count++ }) { Text("count++（目前 $count）") }

            // AndroidView：factory 只會呼叫一次，用來「建立」這個 View 的實例；
            // update 則會在每次 recomposition（這裡是 count 改變時）被呼叫，
            // 負責把最新的 Compose state 手動同步進這個舊 View——
            // 注意 update 裡面用的是命令式的 setCount()，這一行正是
            // Compose 世界跟 View 世界的「橋接點」。
            AndroidView(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                factory = { context -> LegacyCounterView(context) },
                update = { legacyView -> legacyView.setCount(count) },
            )

            Text(
                "反過來想：如果專案裡有一個 Fragment/Activity 還沒遷移成 Compose，" +
                    "可以用 ComposeView 把一小塊 Compose UI 嵌進那個舊畫面裡——" +
                    "這頁的 AndroidView 跟那個方向恰好相反，兩者是同一個互操作機制的兩個方向。",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}
