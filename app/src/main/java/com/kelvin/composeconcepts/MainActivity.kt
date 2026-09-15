package com.kelvin.composeconcepts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kelvin.composeconcepts.navigation.AppNavHost
import com.kelvin.composeconcepts.ui.theme.ComposeConceptsDemoTheme

/**
 * MainActivity —— 傳統 View system 時代這裡會塞很多 findViewById、setContentView(R.layout.xxx)。
 * Compose 時代的 Activity 變得非常「薄」：只做一件事——呼叫 setContent { ... }，
 * 把「畫面長怎樣」的決定權整個交給 Compose。
 *
 * 對應 L-W9-1（Compose 宣告式 UI 心智模型）：
 * 這裡不是「建立畫面元件」，而是向 Compose runtime 描述「這個 Activity 該顯示什麼」，
 * 之後畫面怎麼變、什麼時候變，都交給 Compose runtime 自己決定。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeConceptsDemoTheme {
                // AppNavHost 是整個 App 唯一的進入點，底下用 Navigation Compose
                // 切換不同的示範畫面（見 L-W9-8）。
                AppNavHost()
            }
        }
    }
}
