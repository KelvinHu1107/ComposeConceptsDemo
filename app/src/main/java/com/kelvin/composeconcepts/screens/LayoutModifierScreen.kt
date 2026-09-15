package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * L-W9-6：Layout 系統與 Modifier
 *
 * 這頁的重點示範是「Modifier 順序會直接影響結果」——兩個方塊看起來幾乎一樣，
 * 但點擊的有效範圍完全不同，唯一的差異只是 padding() 跟 clickable() 的順序調換。
 * 實機操作：分別點方塊「中間」跟方塊「邊緣的留白處」，觀察兩者點擊次數是否都會增加。
 */
@Composable
fun LayoutModifierScreen(onBack: () -> Unit) {
    ScreenScaffold(title = "L-W9-6 Layout 與 Modifier", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {

            Text("① Modifier 順序陷阱：padding 在 clickable 之前 vs 之後", style = MaterialTheme.typography.titleSmall)

            var clicksOuterPadding by remember { mutableIntStateOf(0) }
            var clicksInnerPadding by remember { mutableIntStateOf(0) }

            Text(
                "A：padding 在外層 → 點擊範圍「不含」那圈 padding 留白",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 12.dp),
            )
            Box(
                // 由外往內包裹：先 padding 出一圈留白，clickable 只作用在「padding 之後剩下的內容區」，
                // 所以你點那圈留白的地方是點不到的。
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { clicksOuterPadding++ }
                    .background(Color(0xFFB5D4F4))
                    .fillMaxWidth(),
            ) {
                Text("點我（外層 padding），次數：$clicksOuterPadding", Modifier.padding(12.dp))
            }

            Text(
                "B：padding 在 clickable 之後 → 點擊範圍「包含」那圈 padding 留白",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 20.dp),
            )
            Box(
                // clickable 先套用在「還沒被 padding 縮小」的完整範圍上，
                // 之後的 padding 只是視覺上的留白，不影響可點擊區域。
                modifier = Modifier
                    .clickable { clicksInnerPadding++ }
                    .background(Color(0xFFC0DD97))
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("點我（內層 padding），次數：$clicksInnerPadding")
            }

            Text(
                "② Row / Column / Box 基本排版",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
            )
            RowColumnBoxDemo()
        }
    }
}

@Composable
private fun RowColumnBoxDemo() {
    Column {
        Text("Column：由上往下排列", style = MaterialTheme.typography.bodySmall)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("Row 項目1  ")
            Text("Row 項目2  ")
            Text("Row 項目3")
        }
        Text("Box：疊在一起、可以對齊", style = MaterialTheme.typography.bodySmall)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color(0xFFEFE7DA)),
        ) {
            Text(
                "置中對齊",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
            )
        }
    }
}
