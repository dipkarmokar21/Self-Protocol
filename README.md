# Self Protocol 🚀

**Rules Over Emotions.**

Self Protocol is a premium Android application designed to help you document your personal rules and past lessons. By tracking your past mistakes, breakthroughs, and the principles you've established for yourself, you can make logic-driven decisions in moments of high emotion.

---

## 🎨 Features & UI/UX

- **Premium Dark Theme:** Built from the ground up using Jetpack Compose, featuring a sleek, high-contrast Zinc-based color palette that looks amazing on OLED displays.
- **Micro-Interactions:** Includes bouncy touch feedback, smooth Slide + Fade transitions between screens, and premium card layouts.
- **Categorization:** Create custom categories for your Rules and Lessons to keep your insights perfectly organized.
- **Reading vs Editing Modes:** Seamlessly switch between a beautiful, distraction-free reading view and an edit form via the Floating Action Button.
- **Quick Actions & Multi-select:** Long-press on any rule or lesson to enter multi-select mode, easily deleting or managing multiple entries at once. 
- **Search:** Instantly filter your rules and lessons.

## 🛠️ Tech Stack & Architecture

This project is built entirely in **Kotlin** and adheres to modern Android Development (MAD) best practices:

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3.
- **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel).
- **Dependency Injection:** [Dagger-Hilt](https://dagger.dev/hilt/).
- **Local Database:** [Room](https://developer.android.com/training/data-storage/room) (SQLite) with fully handled database migrations.
- **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for single-activity architecture.
- **State Management:** Kotlin Coroutines & `StateFlow`.
- **Preferences & Storage:** Jetpack DataStore.

## 🚀 Getting Started

### Prerequisites
- **Android Studio:** Jellyfish | 2023.3.1 or newer.
- **JDK:** Java 17+ (Ensure Android Studio is pointing to the correct JDK).

### Building the App
1. Clone this repository or open the project folder in Android Studio.
2. Wait for Gradle sync to complete.
3. Select your device or emulator.
4. Hit **Run** (`Shift + F10`).

> [!TIP]
> **Performance Note:** When running the app in `Debug` mode, Compose is completely unoptimized. To experience the true 60fps/120fps native performance of the animations and screen transitions, switch your Build Variant to `Release` before deploying.

## 📁 Project Structure

- `data`: Contains the Room database entities, DAOs, and repository implementations.
- `domain`: Contains domain models, interfaces, and business logic.
- `presentation`: Contains all Jetpack Compose UI code.
  - `home`: The entry dashboard of the app.
  - `categories`: Dynamic categorizations for rules and lessons.
  - `rules` & `lessons`: The core lists and detailed reading/editing views.
  - `components`: Reusable UI elements (Cards, Empty States, Shimmers).
  - `theme`: Colors, typography, and Material 3 theme configurations.
  - `navigation`: The NavHost, route definitions, and screen transitions.
- `util`: Extension functions, animations (like `bounceClick`), and helper classes.

## 📝 License
This project is for personal use and self-improvement.
