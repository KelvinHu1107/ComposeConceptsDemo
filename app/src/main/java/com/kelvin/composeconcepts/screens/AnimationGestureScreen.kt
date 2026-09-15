package com.kelvin.composeconcepts.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * L-W9-9：動畫與手勢 API
 *
 * ① animateDpAsState / animateColorAsState：只要給目標值，剩下的過渡動畫
 *    完全交給 Compose 處理，你不需要自己管理動畫中間的每一個 frame。
 * ② pointerInput + detectDragGestures：手勢處理的入口，用來實作拖曳。
 */
@Composable
fun AnimationGestureScreen(onBack: () -> Unit) {
    ScreenScaffold(title = "L-W9-9 動畫與手勢", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {

            Text("① animate*AsState：點按鈕看方塊大小/顏色平滑過渡", style = MaterialTheme.typography.titleSmall)
            AnimateAsStateDemo()

            Text(
                "② pointerInput 拖曳手勢：直接拖動下面的圓點",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 28.dp),
            )
            DragGestureDemo()
        }
    }
}

@Composable
private fun AnimateAsStateDemo() {
    var expanded by remember { mutableStateOf(false) }

    // 給目標值就好，animateDpAsState / animateColorAsState 自己處理「怎麼從目前值
    // 平滑過渡到新目標值」，底層預設用 spring（彈簧）動畫。
    val size by animateDpAsState(targetValue = if (expanded) 160.dp else 80.dp, label = "size")
    val color by animateColorAsState(
        targetValue = if (expanded) Color(0xFFD85A30) else Color(0xFF378ADD),
        label = "color",
    )

    Column {
        Box(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .size(size)
                .background(color),
        )
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "縮小" else "放大")
        }
    }
}

@Composable
private fun DragGestureDemo() {
    // offsetX / offsetY 是「圓點目前被拖到哪裡」的 state，手勢移動時直接更新它，
    // Compose 會自動把新的 offset 反映到畫面上——概念上跟其他章節的「宣告式」是一致的：
    // 我們只是持續更新 state，從來沒有手動去「移動一個 View」。
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(Color(0xFFEFE7DA))) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                .size(56.dp)
                .background(Color(0xFF7F77DD))
                // pointerInput 是所有手勢處理的入口：detectDragGestures 偵測拖曳，
                // change.consume() 標記這個手勢事件已經被處理，避免繼續往外層
                // 可滾動容器傳遞造成手勢衝突。
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                },
        )
    }
    Text(
        "座標：x=${offsetX.toInt()}, y=${offsetY.toInt()}",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 8.dp),
    )
}
