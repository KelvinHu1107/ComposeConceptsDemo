package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kelvin.composeconcepts.navigation.Routes

/**
 * 這個資料類別描述「一個選單項目該長怎樣」——本身不含任何 UI，純資料。
 * 是 data class，符合 L-W9-3 提到的 stable 型別條件（所有欄位是 val、型別也 stable），
 * Compose compiler 才能放心地在它沒變時 skip 用到它的 composable。
 */
private data class ConceptEntry(
    val route: String,
    val title: String,
    val subtitle: String,
)

private val concepts = listOf(
    ConceptEntry(Routes.MENTAL_MODEL, "L-W9-1 宣告式心智模型", "Composable 是「描述」不是「建立物件」"),
    ConceptEntry(Routes.STATE_HOISTING, "L-W9-2 State Hoisting", "remember / rememberSaveable 差異"),
    ConceptEntry(Routes.RECOMPOSITION, "L-W9-3 Recomposition", "stability、derivedStateOf、skip"),
    ConceptEntry(Routes.INTEROP, "L-W9-4 Compose/View 互操作", "AndroidView 嵌入傳統 View"),
    ConceptEntry(Routes.SIDE_EFFECTS, "L-W9-5 Side-effect API", "LaunchedEffect / DisposableEffect"),
    ConceptEntry(Routes.LAYOUT_MODIFIER, "L-W9-6 Layout 與 Modifier", "Modifier 順序陷阱示範"),
    ConceptEntry(Routes.LAZY_LIST, "L-W9-7 LazyColumn", "key 對狀態正確性的影響"),
    ConceptEntry(Routes.ANIMATION_GESTURE, "L-W9-9 動畫與手勢", "animateDpAsState、拖曳手勢"),
)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    // 這個畫面本身就是 LazyColumn 的第一個實戰範例：
    // 用 items(concepts, key = { it.route }) 而不是單純 items(concepts)，
    // 讓每個選單項目有穩定的身分（route 字串本身就是唯一值），
    // 對應 L-W9-7 教的「一定要給 key」。
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Compose 面試準備 Demo",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            "點一個項目進去看對應的概念示範，程式碼裡都有詳細中文註解。",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )
        LazyColumn {
            items(concepts, key = { it.route }) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onNavigate(entry.route) },
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(entry.title, style = MaterialTheme.typography.titleMedium)
                        Text(entry.subtitle, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
