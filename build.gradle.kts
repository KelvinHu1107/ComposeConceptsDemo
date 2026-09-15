// 根層級 build.gradle.kts —— 只宣告會用到的 plugin 版本，實際套用留給各模組自己決定
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}
