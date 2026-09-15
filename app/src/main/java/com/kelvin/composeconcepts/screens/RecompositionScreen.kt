package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * L-W9-3：Recomposition 效能陷阱
 *
 * 這頁用一個「肉眼可數」的方式讓你感受 recomposition 到底發生了幾次：
 * 每個會 recompose 的區塊旁邊都放一個「這個區塊重組了幾次」的計數器
 * （用 remember { mutableIntStateOf(0) } 搭配「每次呼叫就 +1」實作，
 *  這不是正式 API，只是 demo 用的簡易觀測手段——正式排查請用
 *  Android Studio 的 Layout Inspector 或 Compose compiler metrics）。
 *
 * 打字看看：
 * - 「每次都重組」那一區：字數計數器每打一個字就跳一次（因為它直接讀 text 本身）
 * - 「只有超過 20 字才重組」那一區：用 derivedStateOf 把「原始文字」轉換成
 *   「衍生的布林值」，只有這個布林值真的改變（從 false 變 true，或反過來）
 *   那一刻才會重組——即使你一直打字，這個區塊大部分時間完全不會動。
 */
@Composable
fun RecompositionScreen(onBack: () -> Unit) {
    ScreenScaffold(title = "L-W9-3 Recomposition", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {
            var text by remember { mutableStateOf("") }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("在這裡打字，觀察下面兩個區塊的重組次數") },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                "每次都重組的區塊（直接讀 text.length）",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 20.dp),
            )
            AlwaysRecomposeCounter(length = text.length)

            Text(
                "用 derivedStateOf 收斂後的區塊（只在「超過20字」這個布林值改變時才重組）",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 20.dp),
            )
            // derivedStateOf：把「變動很頻繁」的 text 轉換成「真正影響 UI」的衍生值
            // isLongText。沒有它的話，每次 text 變動（每打一個字）都會讓讀到
            // text.length 的 composable 重組，即使 isLongText 這個布林值其實沒變。
            val isLongText by remember {
                derivedStateOf { text.length > 20 }
            }
            DerivedStateCounter(isLongText = isLongText)
        }
    }
}

/**
 * 這個 composable 每次父層 recompose、且它讀到的 length 參數改變時就會重組，
 * 因為 length 是每打一個字就會變的值——示範「讀取頻繁變動的 state 會導致頻繁重組」。
 */
@Composable
private fun AlwaysRecomposeCounter(length: Int) {
    // remember 包住的計數器只在「這個 composable 真的又被執行一次」時才會 +1，
    // 拿來當作 recomposition 次數的簡易觀測工具。
    val recomposeCount = remember { mutableIntStateOf(0) }
    recomposeCount.intValue++
    Text("目前字數：$length ｜ 這個區塊重組了 ${recomposeCount.intValue} 次")
}

/**
 * 這個 composable 只讀 isLongText（衍生後的布林值），只有它真的翻轉時才會重組。
 * 打字打到超過/低於 20 字那一刻才會看到次數 +1，其他時候完全不動。
 */
@Composable
private fun DerivedStateCounter(isLongText: Boolean) {
    val recomposeCount = remember { mutableIntStateOf(0) }
    recomposeCount.intValue++
    Text(
        if (isLongText) "文字已經超過 20 字了" else "文字還沒超過 20 字",
    )
    Text("這個區塊重組了 ${recomposeCount.intValue} 次", style = MaterialTheme.typography.bodySmall)
}
