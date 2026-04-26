package com.example.pildoraclicker;

import java.util.ArrayList;
import java.util.List;

public final class LeaderboardCache {
    private static final List<LeaderboardEntry> ENTRIES = new ArrayList<>();

    private LeaderboardCache() {
    }

    public static synchronized void setEntries(List<LeaderboardEntry> entries) {
        ENTRIES.clear();
        ENTRIES.addAll(entries);
    }

    public static synchronized List<LeaderboardEntry> getEntries() {
        return new ArrayList<>(ENTRIES);
    }
}