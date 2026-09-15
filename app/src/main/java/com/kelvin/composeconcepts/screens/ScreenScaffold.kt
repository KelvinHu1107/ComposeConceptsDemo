package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 共用的畫面外框：每個示範畫面都需要「標題 + 返回鍵 + 內容區塊」，
 * 抽成一個共用的 Composable，是很典型的「把重複的排版邏輯抽成可重用元件」實踐。
 *
 * 注意這個 Composable 本身是無狀態（stateless）的——它不持有任何 state，
 * 只接收參數（title、content）跟一個 callback（onBack），完全符合
 * L-W9-1／L-W9-2 提到的「State Down, Event Up」單向資料流原則。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
