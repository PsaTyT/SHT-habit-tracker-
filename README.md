# SHT (habit-tracker)
creating my own habit tracker while studying in university

## About

A small app for tracking daily habits: a list of habits, a "done today" toggle,
and (eventually) streaks and statistics.

This is a learning project, built from scratch, step by step. Each iteration
is a working version of the app.

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Build:** Gradle (Kotlin DSL)
- **minSdk:** 34
- **targetSdk / compileSdk:** current versions at the time of project creation

Planned minimum additions:
- Room — local database
- ViewModel + StateFlow — state management
- DataStore — preferences
- Reminders (notifications)

### Current state - Creating an initial version that is far from the desired result. Also figuring out how to become a competent programmer

# Daily news

uh.. this is more difficult than I thought
Thats my fist time using Kotlin but i`v got some progress

### Iteration 1 — skeleton and static list

- Android Studio project created (Empty Activity, Compose)
- Environment set up: Android Studio, physical phone via USB debugging
- `MainActivity` rewritten: instead of the welcome screen — a habit list screen
- `Habit` data model (id, title, doneToday) — as a data class in code for now
- Habit list hardcoded (4 items)
- List rendered via `LazyColumn` + `Card` (Material 3)
- Top bar `TopAppBar` with the title "SHT study"
- Floating action button `FloatingActionButton` with "+" (no action yet)
- `@Preview` added to view the screen without running on a device
- Habit list switched to `mutableStateListOf` — Compose tracks changes
- "+" button opens an add-habit dialog (`AlertDialog` + `OutlinedTextField`)
- Adding a habit: enter a title → validate (not empty) → add to the list
- Tapping a card toggles `doneToday`
- ✕ button on a card removes the habit
- `nextId` counter to generate identifiers for new habits

### **Important:** data is currently stored only in RAM. On app restart the list
resets to the initial 4 habits. This is expected behavior at this stage.
