# Compose Concepts Demo

[English](README.md)

一個最小可跑的 Android 專案，用 Jetpack Compose 實際示範面試準備計畫（Week 9：Jetpack Compose 基礎）裡提到的每個核心概念。每個畫面對應一堂課，程式碼裡有大量中文註解，說明「為什麼要這樣寫」而不只是「這裡在做什麼」。

## 截圖

![首頁選單](docs/screenshot-1.png)

![State hoisting](docs/screenshot-2.png)

![LazyColumn keys](docs/screenshot-3.png)

## 怎麼打開

1. 用 Android Studio 開啟這個資料夾（`File > Open`）。
2. 第一次開啟會自動跑 Gradle sync。
3. 用實機或模擬器跑 `app` 這個 run configuration 即可。
4. 最低支援 Android 7.0（minSdk 24）。

也可以在專案根目錄執行：

```bash
./gradlew :app:installDebug
```

## 畫面對照表

打開 App 會看到一個首頁選單，每一項對應一堂課：

| 畫面 | 對應課程 | 示範內容 |
|---|---|---|
| `MentalModelScreen` | L-W9-1 宣告式心智模型 | 命令式 vs 宣告式對照、Composable 是「描述」不是「建立物件」 |
| `StateHoistingScreen` | L-W9-2 State Hoisting | `remember` vs `rememberSaveable`（旋轉螢幕測差異）、Hoisted 元件寫法 |
| `RecompositionScreen` | L-W9-3 Recomposition 效能陷阱 | 用肉眼可見的重組計數器示範 `derivedStateOf` 怎麼減少不必要的重組 |
| `InteropScreen` | L-W9-4 Compose/View 互操作 | `AndroidView` 把一個真正的舊 `TextView` 嵌進 Compose 畫面 |
| `SideEffectsScreen` | L-W9-5 Side-effect API | `LaunchedEffect`、`DisposableEffect`、`rememberCoroutineScope` 各自什麼時候觸發 |
| `LayoutModifierScreen` | L-W9-6 Layout 與 Modifier | **Modifier 順序陷阱**（padding 在 clickable 前後，點擊範圍完全不同）|
| `LazyListScreen` | L-W9-7 LazyColumn | 重現「沒給 key 會怎樣」的真實 bug——刪除項目後展開狀態跑到別筆資料上 |
| `AnimationGestureScreen` | L-W9-9 動畫與手勢 API | `animateDpAsState`／`animateColorAsState`、`pointerInput` 拖曳手勢 |
| `MentalModelScreenTest`（androidTest）| L-W9-8／L-W9-10 | Navigation 版面外的補充：Compose 測試示範（`createComposeRule`、語意樹查詢）|

L-W9-8（Navigation Compose 與 Material3 Theming）沒有獨立畫面，而是貫穿整個 App：`navigation/AppNavHost.kt` 定義所有畫面路由，`ui/theme/Theme.kt` 則是 Material3 主題設定，兩者都有詳細註解。

## 特別想讓你「動手看到」而不是「讀文字」的地方

- **State Hoisting 畫面**：請實際旋轉手機/模擬器螢幕，親眼看 `remember` 版本歸零、`rememberSaveable` 版本保留數值。
- **LazyColumn 畫面**：按照 `LazyListScreen.kt` 檔案開頭註解裡寫的操作步驟做一次，會直接重現「沒給 key」造成的展開狀態錯位 bug，比看文字說明有感多了。
- **Modifier 順序畫面**：點兩個看起來一樣的方塊的「留白邊緣」，感受點擊範圍的差異。

## 執行測試

```bash
./gradlew connectedAndroidTest
```

（需要有模擬器或實機連線）會執行 `MentalModelScreenTest`，驗證 `+1`／`重設` 按鈕的行為，同時示範 Compose 測試怎麼透過語意樹查詢畫面內容，而不是操作 View 物件本身。

## 這個專案沒有做的事

這是教學用的最小示範，刻意不包含：

- 真正的網路/資料庫存取（Side-effect 範例用 `delay()` 模擬）
- 完整的 MVVM/Repository 分層（所有 state 都直接放在 Composable 裡，方便你一眼看懂單一概念，不用先理解額外的架構）
- Compose Multiplatform、Wear OS 等進階平台整合

---

由 [Claude Code](https://claude.com/claude-code) 協助產生，作為 Kelvin 的 Android 面試準備筆記的一部分。
