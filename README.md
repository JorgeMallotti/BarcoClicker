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

### Combat System (Fights)
Travel to the **Combat Area** to test your strength against the fungal invasion:
- **Stats:** Upgrade your Attack, Defense, Max Health, Agility (AGI), and Luck (Sorte).
- **Scaling Enemies:** Face off against the "Fungo". Each victory makes the next one stronger.
- **Boss Gauntlet:** Unique bosses appear as you progress:
    - **Fungo Velho:** Appears at 5 victories.
    - **Fungo Bravo:** Appears at 10 victories.
    - **Fungo Estrategista:** The final challenge at 15 victories.
- **Special Attack:** Bosses can charge a devastating special move: **"GOLPES REPETIDOS!"**
- **Turn-Based Strategy:** Choose between attacking or defending to mitigate damage.

### Customization & Extras
- **Boat Speed E:** A special late-game upgrade that transforms your boat and provides a massive production multiplier.
- **Music System:** Purchase and toggle background music to accompany your journey.
- **Visual Progression:** Dynamic enemy sprites and player health bars.

## 🛠️ Technical Details
- **Language:** Java
- **Architecture:** Uses a Singleton `GameData` class to persist game state across activities.
- **UI & Graphics:**
    - **Glide:** Used for efficient image loading.
    - **Custom ProgressBar:** Visual tracking of player and enemy health.
    - **Handlers:** Manage real-time production and turn-based combat sequences.
- **Sound:** Integrated `MediaPlayer` for background music, combat hits, and special effects.

## 📦 Installation
1. Clone the repository.
2. Open the project in **Android Studio**.
3. Build and run the `app` module on an emulator or physical device.

## 📝 Controls
- **Short Click:** Perform actions or buy single upgrades.
- **Long Click:** Bulk-buy the maximum possible amount of upgrades with your current balance.

---
*Developed as part of the PildoraClicker project.*
