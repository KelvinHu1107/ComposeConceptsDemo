package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * L-W9-5：Side-effect API（LaunchedEffect / DisposableEffect / rememberCoroutineScope）
 *
 * Composable function 可能被 Compose runtime 重新呼叫任意次，如果直接在 function body
 * 裡啟動 coroutine、註冊 listener，這些副作用會跟著每次呼叫重複觸發或洩漏。
 * 這頁的操作紀錄（log）就是拿來「肉眼證明」這幾個 API 各自什麼時候會被觸發。
 */
@Composable
fun SideEffectsScreen(onBack: () -> Unit) {
    // 用一個共用的操作紀錄列表，直接把每個 side-effect API 實際「什麼時候執行」印出來，
    // 比空講文字更容易在畫面上驗證。
    val log = remember { mutableStateListOf<String>() }

    ScreenScaffold(title = "L-W9-5 Side-effect API", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {

            // ---------- LaunchedEffect ----------
            Text("① LaunchedEffect：綁定 key 啟動/重啟 coroutine", style = MaterialTheme.typography.titleSmall)
            LaunchedEffectDemo(onTick = { log.add("LaunchedEffect tick：$it 秒") })

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            // ---------- DisposableEffect ----------
            Text("② DisposableEffect：進入/離開 composition 時註冊與清理", style = MaterialTheme.typography.titleSmall)
            var showListener by remember { mutableStateOf(false) }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("掛上一個假的 listener")
                Switch(
                    checked = showListener,
                    onCheckedChange = { showListener = it },
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
            if (showListener) {
                // 只有這個 composable 真的「存在」於 composition 裡時，
                // DisposableEffect 才會執行；使用者把 Switch 關掉、這個
                // composable 離開 composition 時，onDispose 就會被呼叫。
                ListenerDemo(onEvent = { log.add(it) })
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            // ---------- rememberCoroutineScope ----------
            Text("③ rememberCoroutineScope：在 onClick 事件裡手動 launch", style = MaterialTheme.typography.titleSmall)
            val scope = rememberCoroutineScope()
            Button(onClick = {
                // LaunchedEffect 不能用在這裡——onClick 不是 composable/coroutine context。
                // 這種「事件發生的當下才要做一次性非同步工作」的場景，
                // 要用 rememberCoroutineScope() 拿到一個綁定 composition 生命週期的
                // CoroutineScope，在 callback 裡手動 launch。
                scope.launch {
                    log.add("rememberCoroutineScope：使用者按下按鈕的當下 launch 了一個 coroutine")
                }
            }) {
                Text("觸發一次性事件")
            }

            Text(
                "操作紀錄（最新在最上面）：",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(log.asReversed(), key = { index, _ -> index }) { _, entry ->
                    Text(entry, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

/**
 * LaunchedEffect(key) 會在它第一次進入 composition 時啟動一個 coroutine；
 * 只有當 key 改變時才會取消舊的、重新啟動新的；composable 離開 composition 時，
 * coroutine 也會自動被取消——完全不需要你自己手動管理生命週期。
 */
@Composable
private fun LaunchedEffectDemo(onTick: (Int) -> Unit) {
    var restartKey by remember { mutableIntStateOf(0) }
    var seconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(restartKey) {
        seconds = 0
        while (true) {
            delay(1000)
            seconds++
            onTick(seconds)
        }
    }

    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("計時：$seconds 秒")
        Button(onClick = { restartKey++ }) { Text("重啟計時器（換 key）") }
    }
}

/**
 * DisposableEffect：需要「註冊」與對應「取消註冊」的副作用要用這個，
 * 一定要在 onDispose block 裡做清理，否則等同於忘記在 onDestroyView 移除 listener，
 * 會造成資源洩漏。這裡用 log 事件模擬「註冊監聽器」跟「取消監聽器」兩個動作。
 */
@Composable
private fun ListenerDemo(onEvent: (String) -> Unit) {
    DisposableEffect(Unit) {
        onEvent("DisposableEffect：掛上監聽器")
        onDispose {
            onEvent("DisposableEffect：onDispose 移除監聽器")
        }
    }
    Text("（假監聽器已掛上，把上面的開關關掉試試看 onDispose）", style = MaterialTheme.typography.bodySmall)
}
