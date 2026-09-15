package com.kelvin.composeconcepts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelvin.composeconcepts.screens.AnimationGestureScreen
import com.kelvin.composeconcepts.screens.HomeScreen
import com.kelvin.composeconcepts.screens.InteropScreen
import com.kelvin.composeconcepts.screens.LayoutModifierScreen
import com.kelvin.composeconcepts.screens.LazyListScreen
import com.kelvin.composeconcepts.screens.MentalModelScreen
import com.kelvin.composeconcepts.screens.RecompositionScreen
import com.kelvin.composeconcepts.screens.SideEffectsScreen
import com.kelvin.composeconcepts.screens.StateHoistingScreen

/**
 * L-W9-8：Navigation Compose 與 Material3 Theming
 *
 * 這裡用字串當 route，是官方範例最基本的寫法——簡單、但沒有編譯期型別檢查，
 * 打錯字或參數型別不對只會在「執行期」爆炸。這是課程裡特別提到的已知取捨，
 * 真實產品建議改用可序列化的型別安全 route（Navigation 的 type-safe routes）。
 * 這個 demo 為了讓你一眼看懂結構，刻意保留最簡單的字串版本。
 */
object Routes {
    const val HOME = "home"
    const val MENTAL_MODEL = "mental_model"
    const val STATE_HOISTING = "state_hoisting"
    const val RECOMPOSITION = "recomposition"
    const val INTEROP = "interop"
    const val SIDE_EFFECTS = "side_effects"
    const val LAYOUT_MODIFIER = "layout_modifier"
    const val LAZY_LIST = "lazy_list"
    const val ANIMATION_GESTURE = "animation_gesture"
}

@Composable
fun AppNavHost() {
    // rememberNavController：NavController 本身也是一個需要跨 recomposition 存活的物件，
    // 所以要用 remember 包住，否則每次 recompose 都會拿到一個全新的、忘記 back stack 的 controller。
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onNavigate = { route ->
                navController.navigate(route) {
                    // launchSingleTop：避免使用者手滑連點兩下，同一頁疊了兩次在 back stack 上。
                    launchSingleTop = true
                }
            })
        }
        composable(Routes.MENTAL_MODEL) { MentalModelScreen(onBack = navController::popBackStack) }
        composable(Routes.STATE_HOISTING) { StateHoistingScreen(onBack = navController::popBackStack) }
        composable(Routes.RECOMPOSITION) { RecompositionScreen(onBack = navController::popBackStack) }
        composable(Routes.INTEROP) { InteropScreen(onBack = navController::popBackStack) }
        composable(Routes.SIDE_EFFECTS) { SideEffectsScreen(onBack = navController::popBackStack) }
        composable(Routes.LAYOUT_MODIFIER) { LayoutModifierScreen(onBack = navController::popBackStack) }
        composable(Routes.LAZY_LIST) { LazyListScreen(onBack = navController::popBackStack) }
        composable(Routes.ANIMATION_GESTURE) { AnimationGestureScreen(onBack = navController::popBackStack) }
    }
}
