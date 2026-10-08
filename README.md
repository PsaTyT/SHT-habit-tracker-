# SHT (habit-tracker)
creating my own habit tracker for studying in urfu

# Warm up

## Stack

Language: Kotlin (currently the standard for Android)
UI: Jetpack Compose or classic XML+View? Compose is more modern and faster to develop; I recommend it.
Architecture: MVVM (ViewModel + StateFlow) — optimal for an app of this size
Storage: Room (SQLite) for habits and notes; DataStore for settings
DI: Hilt (or you can do without it at the start — for a small project, you can manage it manually)
Build: Gradle KTS

## Minimum functionality (MVP)

Habit list (create / edit / delete)
“Completed” mark for today
Streak (current series of days)
Simple statistics (calendar or heatmap for the month)
Local notification reminder (optional, can be included in the second iteration)

## Questions for myself

Android Studio configured, including an emulator/device?
Compose or XML?
Reminders, themes, data export — or is minimalism fine for now?
App name?

## Time limit - before 2027
