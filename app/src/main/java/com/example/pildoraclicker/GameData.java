package com.example.pildoraclicker;

public class GameData {
    private static GameData instance;

    // Score e Clicker
    private double score = 0.0;
    private double clickValue = 1.0;
    private double upgradeCost = 10.0;

    // Player Stats
    private double attack = 10.0;
    private double defense = 5.0;
    private double maxHealth = 100.0;
    private double currentHealth = 100.0;
    private double agi = 10.0;
    private double lucky = 5.0;

    // Fight Upgrade Costs
    private double costAttack = 50.0;
    private double costDefense = 50.0;
    private double costHealth = 50.0;
    private double costAGI = 100.0;
    private double costLucky = 150.0;

    // Enemy Stats (Fungo)
    private double enemyMaxHealth = 100.0;
    private double enemyCurrentHealth = 100.0;
    private double enemyAttack = 8.0;
    private double enemyDefense = 2.0;
    private double enemyAGI = 8.0;
    private double enemyReward = 200.0;

    // Boss System
    private int fungoVictories = 0;
    private boolean isBossActive = false;

    // Auto-clicker Production
    private double valRemo = 0.0;
    private double valMaquina = 0.0;
    private double valEstrutura = 0.0;
    private double valBarco = 0.0;

    // Auto-clicker Increments
    private double incRemo = 5.0;
    private double incMaquina = 5.0;
    private double incEstrutura = 20.0;
    private double incBarco = 100.0;

    // Auto-clicker Costs
    private double costRemo = 15.0;
    private double costMaquina = 100.0;
    private double costEstrutura = 500.0;
    private double costBarco = 2500.0;

    // Cosmetic/Feature Data
    private boolean isBoatSpeedEBought = false;
    private boolean isMusicBought = false;
    private boolean isMusicActive = true; 
    private boolean isGritoSoundPlaying = false;

    public long lastUpdateTime;

    private GameData() {
        lastUpdateTime = System.currentTimeMillis();
    }

    public static synchronized GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public void resetGame() {
        score = 0.0;
        clickValue = 1.0;
        upgradeCost = 10.0;

        attack = 10.0;
        defense = 5.0;
        maxHealth = 100.0;
        currentHealth = 100.0;
        agi = 10.0;
        lucky = 5.0;

        costAttack = 50.0;
        costDefense = 50.0;
        costHealth = 50.0;
        costAGI = 100.0;
        costLucky = 150.0;

        enemyMaxHealth = 100.0;
        enemyCurrentHealth = 100.0;
        enemyAttack = 8.0;
        enemyDefense = 2.0;
        enemyAGI = 8.0;
        enemyReward = 200.0;

        fungoVictories = 0;
        isBossActive = false;

        valRemo = 0.0;
        valMaquina = 0.0;
        valEstrutura = 0.0;
        valBarco = 0.0;

        incRemo = 5.0;
        incMaquina = 5.0;
        incEstrutura = 20.0;
        incBarco = 100.0;

        costRemo = 15.0;
        costMaquina = 100.0;
        costEstrutura = 500.0;
        costBarco = 2500.0;

        isBoatSpeedEBought = false;
        isMusicBought = false;
        isMusicActive = true;
        isGritoSoundPlaying = false;
        
        lastUpdateTime = System.currentTimeMillis();
    }

    // Getters e Setters para Score e Clicker
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public void addScore(double amount) { this.score += amount; }
    public void subScore(double amount) { this.score -= amount; }

    public double getClickValue() { return clickValue; }
    public void setClickValue(double clickValue) { this.clickValue = clickValue; }
    
    public double getUpgradeCost() { return upgradeCost; }
    public void setUpgradeCost(double cost) { this.upgradeCost = cost; }

    // Getters e Setters para Player Stats
    public double getAttack() { return attack; }
    public void addAttack(double amount) { this.attack += amount; }
    
    public double getDefense() { return defense; }
    public void addDefense(double amount) { this.defense += amount; }
    
    public double getMaxHealth() { return maxHealth; }
    public void addMaxHealth(double amount) { this.maxHealth += amount; }
    
    public double getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(double health) { this.currentHealth = health; }
    public void subCurrentHealth(double amount) { this.currentHealth -= amount; }
    
    public double getAGI() { return agi; }
    public void addAGI(double amount) { this.agi += amount; }

    public double getLucky() { return lucky; }
    public void addLucky(double amount) { this.lucky += amount; }

    // Upgrade Costs
    public double getCostAttack() { return costAttack; }
    public void multiplyCostAttack(double multiplier) { this.costAttack *= multiplier; }

    public double getCostDefense() { return costDefense; }
    public void multiplyCostDefense(double multiplier) { this.costDefense *= multiplier; }

    public double getCostHealth() { return costHealth; }
    public void multiplyCostHealth(double multiplier) { this.costHealth *= multiplier; }

    public double getCostAGI() { return costAGI; }
    public void multiplyCostAGI(double multiplier) { this.costAGI *= multiplier; }

    public double getCostLucky() { return costLucky; }
    public void multiplyCostLucky(double multiplier) { this.costLucky *= multiplier; }

    // Enemy Getters/Setters
    public double getEnemyMaxHealth() { return enemyMaxHealth; }
    public void setEnemyMaxHealth(double h) { this.enemyMaxHealth = h; }
    public void multiplyEnemyMaxHealth(double m) { this.enemyMaxHealth *= m; }
    
    public double getEnemyCurrentHealth() { return enemyCurrentHealth; }
    public void setEnemyCurrentHealth(double h) { this.enemyCurrentHealth = h; }
    public void subEnemyCurrentHealth(double amount) { this.enemyCurrentHealth -= amount; }
    
    public double getEnemyAttack() { return enemyAttack; }
    public void setEnemyAttack(double a) { this.enemyAttack = a; }
    public void multiplyEnemyAttack(double m) { this.enemyAttack *= m; }
    
    public double getEnemyDefense() { return enemyDefense; }
    public void setEnemyDefense(double d) { this.enemyDefense = d; }
    
    public double getEnemyAGI() { return enemyAGI; }
    public void setEnemyAGI(double a) { this.enemyAGI = a; }
    public void multiplyEnemyAGI(double m) { this.enemyAGI *= m; }
    
    public double getEnemyReward() { return enemyReward; }
    public void setEnemyReward(double r) { this.enemyReward = r; }
    public void multiplyEnemyReward(double m) { this.enemyReward *= m; }

    // Boss System Getters/Setters
    public int getFungoVictories() { return fungoVictories; }
    public void addFungoVictory() { this.fungoVictories++; }
    public void resetFungoVictories() { this.fungoVictories = 0; }

    public boolean isBossActive() { return isBossActive; }
    public void setBossActive(boolean active) { isBossActive = active; }

    // Auto-clicker Production
    public double getValRemo() { return valRemo; }
    public void addValRemo(double amount) { this.valRemo += amount; }
    public double getIncRemo() { return incRemo; }
    public void setIncRemo(double inc) { this.incRemo = inc; }
    public double getCostRemo() { return costRemo; }
    public void multiplyCostRemo(double m) { this.costRemo *= m; }

    public double getValMaquina() { return valMaquina; }
    public void addValMaquina(double amount) { this.valMaquina += amount; }
    public double getIncMaquina() { return incMaquina; }
    public void setIncMaquina(double inc) { this.incMaquina = inc; }
    public double getCostMaquina() { return costMaquina; }
    public void multiplyCostMaquina(double m) { this.costMaquina *= m; }

    public double getValEstrutura() { return valEstrutura; }
    public void addValEstrutura(double amount) { this.valEstrutura += amount; }
    public double getIncEstrutura() { return incEstrutura; }
    public void setIncEstrutura(double inc) { this.incEstrutura = inc; }
    public double getCostEstrutura() { return costEstrutura; }
    public void multiplyCostEstrutura(double m) { this.costEstrutura *= m; }

    public double getValBarco() { return valBarco; }
    public void addValBarco(double amount) { this.valBarco += amount; }
    public double getIncBarco() { return incBarco; }
    public void setIncBarco(double inc) { this.incBarco = inc; }
    public double getCostBarco() { return costBarco; }
    public void multiplyCostBarco(double m) { this.costBarco *= m; }

    // Booleans
    public boolean isBoatSpeedEBought() { return isBoatSpeedEBought; }
    public void setBoatSpeedEBought(boolean bought) { isBoatSpeedEBought = bought; }
    
    public boolean isMusicBought() { return isMusicBought; }
    public void setMusicBought(boolean bought) { isMusicBought = bought; }

    public boolean isMusicActive() { return isMusicActive; }
    public void setMusicActive(boolean active) { isMusicActive = active; }
    
    public boolean isGritoSoundPlaying() { return isGritoSoundPlaying; }
    public void setGritoSoundPlaying(boolean playing) { isGritoSoundPlaying = playing; }

    // Lógica Central
    public double getTotalProductionPerSecond() {
        double baseProduction = valRemo + valMaquina + valEstrutura + valBarco;
        if (isBoatSpeedEBought && isMusicBought) {
            return baseProduction * 1000000.0;
        } else if (isBoatSpeedEBought || isMusicBought) {
            return baseProduction * 1000.0;
        } else {
            return baseProduction;
        }
    }

    public void updateScore() {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastUpdateTime;
        if (timeDiff > 0) {
            double secondsElapsed = timeDiff / 1000.0;
            score += getTotalProductionPerSecond() * secondsElapsed;
            lastUpdateTime = currentTime;
        }
    }

    public void resetEnemy() {
        enemyCurrentHealth = enemyMaxHealth;
    }
}