# SHT study

A personal habit tracker. Android app built with Kotlin + Jetpack Compose.

## About

A small app for tracking daily habits: a list of habits, a "done today" toggle,
and (eventually) streaks and statistics.

This is a learning project, built from scratch, step by step. Each iteration
is a working version of the app. Don’t throw tomatoes at me.

## Why

Built for personal use — no accounts, no sync, no ads. Just a local
database on your phone with your habits. All data stays on the device.

The goal is a small, focused tool: mark habits daily, see your streaks,
and don't think about anything else.

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Build:** Gradle (Kotlin DSL) with AGP 9.x and built-in Kotlin
- **Local database:** Room (SQLite)
- **Async:** Kotlin Coroutines + Flow
- **Architecture:** UI → ViewModel → DAO → Room
- **minSdk:** 34
- **targetSdk / compileSdk:** 37

Planned additions:
- Completion history (per-day records instead of a single boolean)
- Streaks
- Reminders (notifications)
- Dark theme and appearance settings

## Requirements

- **Android Studio** Meerkat (2024.3) or newer
- **JDK** 17+
- **Android SDK** with platform API 37 installed
- A physical device running Android 14 (API 34) or newer, or an emulator

# Current state

### Iteration 1 — skeleton and static list

- Android Studio project created (Empty Activity, Compose)
- Environment set up: Android Studio, physical phone via USB debugging
- `MainActivity` rewritten: instead of the welcome screen — a habit list screen
- `Habit` data model (id, title, doneToday) — as a data class in code for now
- Habit list hardcoded (4 items)
- List rendered via `LazyColumn` + `Card` (Material 3)
- Top bar `TopAppBar` with the title "SHT study"
- Floating action button with "+" (no action yet)
- `@Preview` added to view the screen without running on a device

### Iteration 2 — in-memory interactivity

- Habit list switched to `mutableStateListOf` — Compose tracks changes
- "+" button opens an add-habit dialog (`AlertDialog` + `OutlinedTextField`)
- Adding a habit: enter a title → validate (not empty) → add to the list
- Tapping a card toggles `doneToday`
- ✕ button on a card removes the habit
- `nextId` counter to generate identifiers for new habits

**Limitation:** data was stored only in RAM, reset on every app restart.

### Iteration 3 — persistent storage with Room

- Room dependencies added (runtime, ktx, compiler) and KSP plugin configured
- `HabitEntity`, `HabitDao`, `HabitDatabase` created in `data` package
- Database implemented as a singleton via `HabitDatabase.getInstance(context)`
- `HabitListScreen` rewritten: reads from the database via `Flow`,
  writes through coroutines
- Data now survives app restarts

**Notes on the build setup:** the project uses AGP 9.x with built-in Kotlin
enabled. To let KSP register its generated sources, `gradle.properties`
contains:
**android.disallowKotlinSourceSets=false**

### Iteration 4 — ViewModel and cleaner architecture

- Dependencies added: `lifecycle-viewmodel-compose`, `lifecycle-runtime-compose`
- `HabitViewModel` created (extends `AndroidViewModel`):
    - holds the DAO
    - exposes `habits: StateFlow<List<HabitEntity>>` via `stateIn`
    - provides `addHabit`, `toggleHabit`, `deleteHabit`
    - runs database operations in `viewModelScope`
- `HabitListScreen` is now thin: gets the ViewModel via `viewModel()`,
  subscribes with `collectAsStateWithLifecycle()`, delegates actions
- Validation (non-empty title) moved from UI into the ViewModel
- Coroutine scope no longer created in the composable

### Iteration 5 — completion history and data model fix

- New table `completions` (id, habitId, date) — one row per completed day
- `HabitEntity` no longer stores `doneToday`; the flag is computed via JOIN
- `HabitWithStatus` data class — result of the JOIN query
- `CompletionDao` — insert / delete / check-by-date operations
- `Converters` — TypeConverter for `LocalDate` stored as epoch day
- `HabitDatabase` version bumped from 1 to 2
- **`MIGRATION_1_2` written** — old data preserved:
    - creates `completions` table
    - moves `doneToday = 1` entries into `completions` with today's date
    - recreates `habits` table without the `doneToday` column
- `HabitViewModel.toggleHabit` now creates / deletes a completion record

**Fixes:** the "midnight bug" is now solvable — habit status is derived from
completion records, so it naturally resets when the date changes.

## Roadmap

- [x] In-memory habit list with add / toggle / delete
- [x] Persistent storage with Room
- [x] ViewModel layer
- [x] Completion history (per-day records instead of `doneToday` boolean)
- [x] Fix the "midnight" problem: `doneToday` currently never resets
- [ ] Streaks — counter of consecutive completed days (some bugs and troubles)
- [ ] History and simple statistics (monthly heatmap)
- [ ] Reminders via WorkManager / AlarmManager
- [ ] Real icons instead of text symbols (●, ○, +, ✕)
- [ ] Dark theme and appearance settings
- [ ] Final design and app name

## Notes

- Colors come from the theme (`MaterialTheme.colorScheme.primary`, etc.)
  rather than being hardcoded — this makes it easy to change the look
  and add a dark/light theme later.
- Database operations run on coroutines; the UI never touches Room on the
  main thread.
- Iterations move in small steps: each one is a working version of the app
  that can be launched and used.
