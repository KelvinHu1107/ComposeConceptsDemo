package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

/**
 * L-W9-1：Compose 宣告式 UI 心智模型
 *
 * ============================================================
 * 命令式（傳統 View system）大概會這樣寫，你會需要手動「改」畫面：
 * ------------------------------------------------------------
 * // Kotlin + View system 的假想寫法（這裡沒有真的用到，純粹拿來對照）
 * // val countText: TextView = findViewById(R.id.count_text)
 * // var count = 0
 * // findViewById<Button>(R.id.increment_btn).setOnClickListener {
 * //     count++
 * //     countText.text = "目前數字：$count"   // ← 要記得手動同步 UI，這行忘了寫就會 bug
 * // }
 * ============================================================
 *
 * 宣告式（Compose）長這樣：你只描述「count 是這個值的時候，畫面該長怎樣」，
 * 從來不會寫「把文字改成...」這種指令，Compose 自己會處理。
 */
@Composable
fun MentalModelScreen(onBack: () -> Unit) {
    ScreenScaffold(title = "L-W9-1 宣告式心智模型", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {

            // remember { mutableIntStateOf(0) }：這行本身就是「state」，下一堂課
            // (L-W9-2 State Hoisting) 會深入講它為什麼要用 remember 包住。
            // 這裡先只專注在「宣告式」這件事本身。
            var count by remember { mutableIntStateOf(0) }

            Text(
                "這是「描述」，不是「指令」",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                "下面這行 Text 永遠只是在描述：「count 現在是這個值的時候，畫面上該顯示什麼字」。" +
                    "我們完全沒有寫任何一行「把文字改成 XXX」的程式碼——" +
                    "每次 count 改變，Compose 自己重新執行這個 function、自己決定畫面要更新成什麼樣子。",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            )

            // 這一行 Text，本質上就是 Composable(state) 的具體示範：
            // 「給定 count 這個 state，畫面應該長怎樣」——描述完就結束了。
            Text("目前數字：$count", style = MaterialTheme.typography.headlineSmall)

            Row(
                Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(onClick = { count++ }) { Text("+1") }
                Button(onClick = { count = 0 }) { Text("重設") }
            }

            Text(
                "試著點 +1：Compose 只是重新執行了 MentalModelScreen 這個 function 一次，" +
                    "不是你手動去改了某個 TextView 的內容。",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp),
            )

            // 下面示範「Composable Function 是 UI 建構單元」：
            // Greeting 不是在建立一個 TextView 物件，而是在向 Compose runtime
            // emit（發出）一段 UI 描述。它可能被呼叫 0 次、1 次或多次，
            // 所以它必須是「沒有 side effect、可以重複執行」的純函式。
            Greeting(name = "Kelvin")
        }
    }
}

/**
 * 一個最小的 Composable function 範例——把它想成「把 state 翻譯成 UI 描述」的
 * 純函式：輸入 name，輸出「這段畫面該長怎樣」，除此之外什麼都不做。
 * 絕對不要在這裡面寫資料庫存取、啟動 coroutine 之類的 side effect——
 * 這個 function 隨時可能被 Compose 重新呼叫，副作用會跟著重複觸發、完全失控。
 * 真正需要副作用的地方要用 L-W9-5 的 LaunchedEffect / DisposableEffect。
 */
@Composable
private fun Greeting(name: String) {
    Text(
        "Hello, $name! （這是一個 pure 的 Composable function）",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 20.dp),
    )
}
