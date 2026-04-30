# Barco Clicker

**Barco Clicker** is an addictive Android incremental (clicker) game where your goal is to build the ultimate naval empire. Start by rowing manually, progress through automated structures, battle fungal enemies, and compete on a global leaderboard.

## 🚀 Features

### Core Gameplay

- **Click to Row:** Tap the main boat to generate Barcos (Boats).
- **Upgrades:** Spend your boats to increase your click value and rowing efficiency.
- **Automatic Generators:** Unlock and upgrade automated production:
  - **Remo (Oar):** Basic manual assistance.
  - **Máquina (Machine):** Industrial rowing power.
  - **Estrutura (Structure):** Large-scale production.
  - **Barco (Boat):** High-tier automated vessel production.
- **Long Press:** Hold any upgrade button to buy the maximum affordable quantity at once.

### Combat System (Caçador de Fungos)

Travel to the **Fights** screen to test your strength:

- **Stats:** Upgrade Attack, Defense, Max Health, AGI, and Lucky.
- **Scaling Enemies:** Face off against the "Fungo" — each victory makes the next stronger.
- **Boss Fights:** A powerful **Fungo Velho (Old Fungus)** boss appears at milestone victories, offering massive rewards.
- **Special Loot:** Defeating enemies grants large amounts of Barcos to fuel your upgrades.
- **Warrior Purchase:** Unlock the music warrior in combat for an audio boost.

### Leaderboard

- **Global Rankings:** Submit your score and run time at the end of a run.
- **Session Tokens:** Secure submission via server-issued session tokens.
- **In-Memory Cache:** Leaderboard scores are cached locally for instant display.

### Settings

- **Volume Control:** Adjust background music volume via a slider.
- **Language:** Switch between **Portuguese**, **Spanish**, and **English** at any time.
- **Accessible Everywhere:** The settings button is available on both the main screen and the combat screen.

### Customization & Extras

- **Boat Speed E:** One-time special upgrade for a massive production multiplier.
- **Music System:** Two background tracks — normal gameplay music and a boss battle theme.
- **Dynamic Visuals:** Visual changes as you progress and encounter bosses.

## 🛠️ Technical Details

- **Language:** Java (no Kotlin)
- **Architecture:** Activity-based; no Fragments, ViewModels, or dependency injection.
- **State Management:** `GameData` singleton centralizes all game state across Activities.
- **Music:** Static `MusicManager` for cross-Activity music persistence and volume control.
- **Localization:** `LocaleManager` applies runtime locale switching (PT / ES / EN).
- **Number Display:** `NumberFormatter` abbreviates large numbers (K, M, B, T … up to Tt / 10¹⁰²).
- **API:** `ApiClient` handles leaderboard HTTP communication; `LeaderboardCache` stores results in memory.
- **Sound Effects:** `MediaPlayer` for combat sound effects (combo, critical hits).
- **Game Loops:** `Handler` + `Runnable` — 100ms tick for score production, 1000ms for the run timer.

### Project Structure

```
app/src/main/
├── java/com/example/pildoraclicker/
│   ├── MainActivity.java              # Main clicker screen
│   ├── FightsActivity.java            # Combat screen
│   ├── LeaderboardActivity.java       # Leaderboard display
│   ├── GameData.java                  # Singleton game state
│   ├── MusicManager.java              # Static music playback
│   ├── NumberFormatter.java           # Number abbreviation utility
│   ├── ApiClient.java                 # HTTP leaderboard client
│   ├── LeaderboardCache.java          # In-memory leaderboard cache
│   ├── LeaderboardEntry.java          # Leaderboard data model
│   └── LocaleManager.java             # Runtime locale switching
└── res/
    ├── layout/                        # One XML per Activity + custom dialogs
    ├── values/                        # strings.xml, colors.xml, themes.xml
    ├── values-es/                     # Spanish string overrides
    ├── values-en/                     # English string overrides
    ├── drawable/                      # Images and icons
    └── raw/                           # music.mp3, chefemusic.mp3
```

## 📦 Installation

1. Clone the repository.
2. Open the project in **Android Studio**.
3. Build and run the `app` module on an emulator or physical device (**Android 7.0+ recommended**).

```bash
./gradlew :app:assembleDebug       # Build debug APK
./gradlew :app:installDebug        # Install on connected device
```

## 📝 Controls

- **Short Click:** Perform actions or buy a single upgrade.
- **Long Click:** Buy the maximum affordable quantity of an upgrade.
- **Settings Button:** Opens the settings dialog (volume, language) from any screen.
- **Leaderboard Button:** Opens the global ranking screen from the main screen.

## 🌍 Supported Languages

| Code | Language             |
| ---- | -------------------- |
| pt   | Portuguese (default) |
| es   | Spanish              |
| en   | English              |

Language can be changed at any time from the settings menu. The app restarts the current screen to apply the new locale immediately.

---

_Developed as part of the PildoraClicker (Barco Clicker) project. — April 2026_
