package com.kelvin.composeconcepts

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kelvin.composeconcepts.screens.MentalModelScreen
import com.kelvin.composeconcepts.ui.theme.ComposeConceptsDemoTheme
import org.junit.Rule
import org.junit.Test

/**
 * L-W9-10：Compose 測試
 *
 * Compose 測試不是直接操作 View 物件，而是透過 semantics tree（語意樹）查詢畫面——
 * onNodeWithText 等 API 就是在查這棵樹，不是在比對畫面像素。
 *
 * 這裡也示範了 L-W9-10 提到的「Compose 測試框架大多不需要手動同步（IdlingResource）」：
 * performClick() 之後直接呼叫 assertExists()，Compose 測試框架會自動等 recomposition
 * 穩定下來才讓斷言執行，不需要我們自己寫等待邏輯。
 */
class MentalModelScreenTest {

    // createComposeRule()：純 Compose 內容的測試起點，不需要真正的 Activity。
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickingPlusOneButton_incrementsCounter() {
        // setContent 把要測試的畫面塞進一個測試用的 composition。
        composeTestRule.setContent {
            ComposeConceptsDemoTheme {
                MentalModelScreen(onBack = {})
            }
        }

        // 一開始應該顯示「目前數字：0」。
        composeTestRule.onNodeWithText("目前數字：0").assertExists()

        // 用文字找到 "+1" 按鈕並點擊——這裡是靠語意樹上的文字內容定位，
        // 正式專案建議額外加上 Modifier.testTag("increment_button")，
        // 避免之後文案調整（例如改成「加一」）讓測試失效。
        composeTestRule.onNodeWithText("+1").performClick()

        // 點擊後應該變成「目前數字：1」——不需要自己 sleep 等待，
        // Compose 測試框架已經確保 recomposition 完成才會走到這一行。
        composeTestRule.onNodeWithText("目前數字：1").assertExists()

        // 點「重設」按鈕，驗證能回到 0。
        composeTestRule.onNodeWithText("重設").performClick()
        composeTestRule.onNodeWithText("目前數字：0").assertExists()
    }
}
