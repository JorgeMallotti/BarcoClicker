# Barco Clicker

**Barco Clicker** is an addictive Android incremental (clicker) game where your goal is to build the ultimate naval empire. Start by rowing manually and progress to automated structures and epic battles against fungal enemies.

## 🚀 Features

### Core Gameplay
- **Click to Row:** Tap the main boat to generate Barcos (Boats).
- **Upgrades:** Spend your boats to increase your click value and rowing efficiency.
- **Automatic Generators:** Unlock and upgrade automated production:
    - **Remo (Oar):** Basic manual assistance.
    - **Máquina (Machine):** Industrial rowing power.
    - **Estrutura (Structure):** Large scale production.
    - **Barco (Boat):** High-tier automated vessel production.

### Combat System (Caçador de Fungos)
Travel to the **Fights** screen to test your strength:
- **Stats:** Upgrade your Attack, Defense, Max Health, and Attack Speed (APS).
- **Scaling Enemies:** Face off against the "Fungo". Each victory makes the next one stronger.
- **Boss Fights:** Every 5 victories, the **Fungo Velho (Old Fungus)** appears. It is 10x stronger than regular enemies and offers massive rewards!
- **Special Loot:** Defeating enemies grants large amounts of Barcos to fuel your upgrades.

### Customization & Extras
- **Boat Speed E:** A special upgrade that transforms your boat and provides a massive production multiplier.
- **Music System:** Purchase and toggle background music to accompany your journey.
- **Dynamic Visuals:** Experience visual changes as you progress and face bosses.

## 🛠️ Technical Details
- **Language:** Java
- **Architecture:** Uses a Singleton `GameData` class to manage game state across activities.
- **UI & Graphics:**
    - **Glide:** Used for efficient image loading and animated transitions.
    - **Handlers:** Manage the main game loops for production and combat.
- **Sound:** Integrated `MediaPlayer` for background music and combat sound effects.

## 📦 Installation
1. Clone the repository.
2. Open the project in **Android Studio**.
3. Build and run the `app` module on an emulator or physical device (Android 7.0+ recommended).

## 📝 Controls
- **Short Click:** Perform actions or buy single upgrades.
- **Long Click:** Buy the maximum possible amount of upgrades with your current balance.

---
*Developed as part of the PildoraClicker(Barco Clicker) project.*