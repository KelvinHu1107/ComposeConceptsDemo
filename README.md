# Compose Concepts Demo

[中文說明](README.zh-TW.md)

A small, runnable Android app that demonstrates the core Jetpack Compose ideas from my interview prep (Week 9). Each screen maps to one lesson. Comments in the code explain *why* a snippet is written this way, not only *what* it does.

This is a teaching demo, not a production architecture. There is no network or database layer, and state lives in the composables so each concept can be read in isolation.

## Screenshots

![Home menu](docs/screenshot-1.png)

![State hoisting](docs/screenshot-2.png)

![LazyColumn keys](docs/screenshot-3.png)

## Stack

- Kotlin 1.9.24
- Jetpack Compose BOM 2024.09.00 (Kotlin compiler extension 1.5.14)
- Material 3
- Navigation Compose 2.8.0
- Coroutines 1.8.1
- Android Gradle Plugin 8.5.2 / Gradle 8.7
- `minSdk` 24 · `compileSdk` / `targetSdk` 34
- `applicationId`: `com.kelvin.composeconcepts`

## Run it

1. Open this folder in Android Studio (`File > Open`).
2. Wait for Gradle sync, then run the `app` configuration on an emulator or a device (Android 7.0 or later).
3. From the command line:

```bash
./gradlew :app:installDebug
```

## Screens

The home screen is a menu. Each item opens one lesson:

| Screen | Lesson | What it shows |
|---|---|---|
| `MentalModelScreen` | L-W9-1 Declarative mental model | Imperative vs declarative contrast; a composable describes UI, it does not “construct objects” the way a View hierarchy does |
| `StateHoistingScreen` | L-W9-2 State hoisting | `remember` vs `rememberSaveable` (rotate the device to see the difference), and a hoisted composable |
| `RecompositionScreen` | L-W9-3 Recomposition | A visible recomposition counter that shows how `derivedStateOf` avoids extra work |
| `InteropScreen` | L-W9-4 Compose / View interop | `AndroidView` embedding a real legacy `TextView` (`LegacyCounterView`) |
| `SideEffectsScreen` | L-W9-5 Side-effect APIs | When `LaunchedEffect`, `DisposableEffect`, and `rememberCoroutineScope` run |
| `LayoutModifierScreen` | L-W9-6 Layout and modifiers | Modifier order: padding before vs after `clickable` changes the hit area |
| `LazyListScreen` | L-W9-7 LazyColumn | Reproduces the missing-`key` bug: after a deletion, expanded state jumps to the wrong item |
| `AnimationGestureScreen` | L-W9-9 Animation and gestures | `animateDpAsState` / `animateColorAsState`, plus a `pointerInput` drag |

L-W9-8 (Navigation Compose and Material 3 theming) has no separate screen. Routes live in `navigation/AppNavHost.kt` (simple string routes, which the comments note are not type-safe). The theme is in `ui/theme/Theme.kt`.

The in-code comments are in Traditional Chinese.

## What to try

- **State hoisting:** rotate the device or emulator. The `remember` counter resets; the `rememberSaveable` counter keeps its value.
- **LazyColumn:** in both lists, expand the second row (`Bob`), then tap **刪除第一項** (Delete the first item) at the top. In the list without keys, the expanded state sticks to the position and appears on `Carol`. In the list with keys (`person.id`), it stays on `Bob`.
- **Modifier order:** tap the padding around the two similar boxes and compare the clickable area.

## Tests

There is one instrumented Compose UI test, `MentalModelScreenTest`, which uses `createComposeRule`, finds nodes by text in the semantics tree, taps **+1** and **重設**, and asserts that the counter goes from `目前數字：0` to `目前數字：1` and back.

With an emulator or device connected:

```bash
./gradlew connectedAndroidTest
```

## What this project does not include

This is a minimal teaching app, on purpose:

- No real network or database access (the side-effect sample uses `delay()`)
- No MVVM / Repository layer (state stays in the composables so one concept can be read at a time)
- No Hilt, no Room, no Compose Multiplatform, no Wear OS, no AAOS

---

Written as part of my Android interview prep notes, with assistance from [Claude Code](https://claude.com/claude-code).
