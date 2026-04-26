package com.example.pildoraclicker;

public class LeaderboardEntry {
    private final int id;
    private final String playerName;
    private final double timeSeconds;

    public LeaderboardEntry(int id, String playerName, double timeSeconds) {
        this.id = id;
        this.playerName = playerName;
        this.timeSeconds = timeSeconds;
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }
}