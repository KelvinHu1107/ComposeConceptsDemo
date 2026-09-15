plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.kelvin.composeconcepts"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kelvin.composeconcepts"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        // 開啟 Compose 支援——這一行是「這個模組要用 Jetpack Compose」最關鍵的開關
        compose = true
    }

    composeOptions {
        // 對應下面 Compose BOM 版本的 Kotlin Compiler Extension 版本
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Compose BOM（Bill of Materials）：統一管理所有 Compose 相關函式庫的版本，
    // 底下引用 Compose 函式庫時就不需要各自寫版本號，BOM 幫你對齊成互相相容的一組版本。
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // ---- Compose 核心 ----
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview") // @Preview 需要這個
    implementation("androidx.compose.foundation:foundation")  // Row/Column/Box、LazyColumn、clickable、手勢 API 都在這裡
    implementation("androidx.compose.animation:animation")    // animateDpAsState/animateColorAsState 等動畫 API
    implementation("androidx.compose.material3:material3")   // Material3：MaterialTheme、Button、Text...
    implementation("androidx.compose.material:material-icons-core") // Icons.AutoMirrored.Filled.ArrowBack 需要這個

    // ---- Activity 與 Compose 的橋接 ----
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // ---- Navigation Compose（L-W9-8 用到）----
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // ---- Coroutines（Side-effect API 範例會用到）----
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // ---- Debug 專用：Compose 預覽與 UI 檢查工具 ----
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ---- 一般 JVM 單元測試 ----
    testImplementation("junit:junit:4.13.2")

    // ---- Compose 測試（L-W9-10 用到）----
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
