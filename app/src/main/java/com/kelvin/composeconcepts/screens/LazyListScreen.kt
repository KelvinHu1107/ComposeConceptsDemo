package com.kelvin.composeconcepts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private data class Person(val id: Int, val name: String)

/**
 * L-W9-7：LazyColumn 與列表效能——這頁重現「沒給 key 會怎麼樣」的真實 bug，
 * 不是空講理論。
 *
 * 操作步驟（請照這個順序做才能看到差異）：
 * 1. 在「沒有 key」跟「有 key」兩個列表裡，都點一下第二項「Bob」，把它展開。
 * 2. 按最上面的「刪除第一項（Alice）」按鈕。
 * 3. 觀察兩個列表：
 *    - 「沒有 key」的列表：Bob 被刪除後的位置被 Carol 頂上去，
 *      你會看到 Carol 莫名其妙變成展開狀態——因為 Compose 只認「第幾個位置」，
 *      不知道資料實際上換了一筆，就把原本位置1的展開狀態誤套用到現在排在
 *      位置1的 Carol 身上。
 *    - 「有 key」的列表：Bob 的展開狀態跟著 Bob 的 id 走，刪除 Alice 之後
 *      Bob 依然是展開的，Carol 依然是收合的——資料跟 UI 狀態正確對應。
 */
@Composable
fun LazyListScreen(onBack: () -> Unit) {
    // 用同一份資料餵給兩個列表，確保是公平比較，唯一差異只在有沒有給 key。
    val people = remember {
        mutableStateListOf(
            Person(1, "Alice"),
            Person(2, "Bob"),
            Person(3, "Carol"),
            Person(4, "Dave"),
        )
    }

    ScreenScaffold(title = "L-W9-7 LazyColumn", onBack = onBack) { padding ->
        Column(padding.padding(16.dp)) {
            Button(
                onClick = { if (people.isNotEmpty()) people.removeAt(0) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("刪除第一項")
            }

            Text(
                "沒有 key（用預設的位置索引當身分）",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 20.dp, bottom = 4.dp),
            )
            LazyColumn(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                // 故意不給 key —— Compose 預設用「第幾個位置」當作身分。
                items(people) { person -> ExpandableRow(person) }
            }

            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            Text(
                "有 key（用 person.id 當身分）",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            LazyColumn(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                items(people, key = { it.id }) { person -> ExpandableRow(person) }
            }
        }
    }
}

/**
 * 每一列自己用 remember 記住「展開/收合」這個暫態 UI 狀態。
 * 這個 remember 是綁在「slot table 裡的這個呼叫位置」——如果外層 LazyColumn
 * 沒給 key，這個位置在資料增刪後可能被换成別筆資料，展開狀態就會跟著錯位。
 */
@Composable
private fun ExpandableRow(person: Person) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .background(if (expanded) Color(0xFFF0997B) else Color(0xFFF1EFE8))
            .padding(12.dp),
    ) {
        Text(person.name, style = MaterialTheme.typography.bodyLarge)
        if (expanded) {
            Text("（展開狀態）id=${person.id}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
