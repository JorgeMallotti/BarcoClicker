package com.example.pildoraclicker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private ListView listLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        progressBar = findViewById(R.id.progressLeaderboard);
        tvEmptyState = findViewById(R.id.tvLeaderboardEmpty);
        listLeaderboard = findViewById(R.id.listLeaderboard);
        Button btnBack = findViewById(R.id.btnLeaderboardBack);

        btnBack.setOnClickListener(view -> finish());

        List<LeaderboardEntry> cachedEntries = LeaderboardCache.getEntries();
        if (!cachedEntries.isEmpty()) {
            renderLeaderboard(cachedEntries);
        }

        loadLeaderboard();
    }

    private void loadLeaderboard() {
        progressBar.setVisibility(View.VISIBLE);

        ApiClient.fetchLeaderboard(this, new ApiClient.LeaderboardCallback() {
            @Override
            public void onSuccess(List<LeaderboardEntry> entries) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    renderLeaderboard(entries);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (LeaderboardCache.getEntries().isEmpty()) {
                        listLeaderboard.setVisibility(View.GONE);
                        tvEmptyState.setVisibility(View.VISIBLE);
                        tvEmptyState.setText(errorMessage);
                    }
                    Toast.makeText(LeaderboardActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void renderLeaderboard(List<LeaderboardEntry> entries) {
        if (entries.isEmpty()) {
            listLeaderboard.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText(R.string.leaderboard_empty);
            return;
        }

        ArrayList<String> rows = new ArrayList<>();
        for (int index = 0; index < entries.size(); index++) {
            LeaderboardEntry entry = entries.get(index);
            rows.add(getString(
                    R.string.leaderboard_row_format,
                    index + 1,
                    entry.getPlayerName(),
                    entry.getTimeSeconds()
            ));
        }

        listLeaderboard.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        listLeaderboard.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                rows
        ));
    }
}