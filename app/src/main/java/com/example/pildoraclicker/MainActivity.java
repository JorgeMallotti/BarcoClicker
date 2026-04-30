package com.example.pildoraclicker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private GameData gameData;

    private TextView tvScore;
    private TextView tvRunTimer;
    private ImageButton btnClick;
    private Button btnUpgrade, btnRemo, btnMaquina, btnEstrutura, btnBarco, btnGoToFights, btnBoatSpeedE, btnLeaderboard, btnSettings;
    private boolean sessionRequestInFlight = false;


    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable autoClickRunnable = new Runnable() {
        @Override
        public void run() {
            gameData.updateScore();
            updateUI();
            handler.postDelayed(this, 100); 
        }
    };

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            updateRunTimer();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleManager.applyLocale(this);
        setContentView(R.layout.activity_main);

        gameData = GameData.getInstance();
        MusicManager.setVolume(gameData.getMusicVolume());

        tvScore = findViewById(R.id.tvScore);
        tvRunTimer = findViewById(R.id.tvRunTimer);
        btnClick = findViewById(R.id.btnClick);
        btnUpgrade = findViewById(R.id.btnUpgrade);
        btnRemo = findViewById(R.id.btnRemo);
        btnMaquina = findViewById(R.id.btnMaquina);
        btnEstrutura = findViewById(R.id.btnEstrutura);
        btnBarco = findViewById(R.id.btnBarco);
        btnGoToFights = findViewById(R.id.btnGoToFights);
        btnBoatSpeedE = findViewById(R.id.btnBoatSpeedE);
        btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnSettings = findViewById(R.id.btnSettings);

        prefetchLeaderboard();
        ensureLeaderboardSession();

        btnClick.setOnClickListener(v -> {
            gameData.addScore(gameData.getClickValue());
            updateUI();
        });

        btnUpgrade.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getUpgradeCost()) {
                gameData.subScore(gameData.getUpgradeCost());
                if (gameData.getClickValue() < 10) gameData.setClickValue(gameData.getClickValue() + 1);
                else gameData.setClickValue(gameData.getClickValue() * 1.2);
                gameData.setUpgradeCost(gameData.getUpgradeCost() * 1.5);
                updateUI();
            }
        });
        btnUpgrade.setOnLongClickListener(view -> {
                boolean bought = false;
                while (gameData.getScore() >= gameData.getUpgradeCost()) {
                    gameData.subScore(gameData.getUpgradeCost());
                    if (gameData.getClickValue() < 10) gameData.setClickValue(gameData.getClickValue() + 1);
                    else gameData.setClickValue(gameData.getClickValue() * 1.2);
                    gameData.setUpgradeCost(gameData.getUpgradeCost() * 1.5);
                    bought = true;
                }
                if (bought) updateUI();
                return true;
        });

        btnRemo.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostRemo()) {
                gameData.subScore(gameData.getCostRemo());
                gameData.addValRemo(gameData.getIncRemo());
                if (gameData.getIncRemo() < 10) gameData.setIncRemo(gameData.getIncRemo() + 10);
                else gameData.setIncRemo(gameData.getIncRemo() * 1.3);
                gameData.multiplyCostRemo(1.4);
                updateUI();
            }
        });

        btnRemo.setOnLongClickListener(view -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostRemo()) {
                gameData.subScore(gameData.getCostRemo());
                gameData.addValRemo(gameData.getIncRemo());
                if (gameData.getIncRemo() < 10) gameData.setIncRemo(gameData.getIncRemo() + 10);
                else gameData.setIncRemo(gameData.getIncRemo() * 1.3);
                gameData.multiplyCostRemo(1.4);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        btnMaquina.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostMaquina()) {
                gameData.subScore(gameData.getCostMaquina());
                gameData.addValMaquina(gameData.getIncMaquina());
                if (gameData.getIncMaquina() < 50) gameData.setIncMaquina(gameData.getIncMaquina() + 5);
                else gameData.setIncMaquina(gameData.getIncMaquina() * 1.2);
                gameData.multiplyCostMaquina(1.2);
                updateUI();
            }
        });

        btnMaquina.setOnLongClickListener(view -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostMaquina()) {
                gameData.subScore(gameData.getCostMaquina());
                gameData.addValMaquina(gameData.getIncMaquina());
                if (gameData.getIncMaquina() < 50) gameData.setIncMaquina(gameData.getIncMaquina() + 5);
                else gameData.setIncMaquina(gameData.getIncMaquina() * 1.2);
                gameData.multiplyCostMaquina(1.2);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        btnEstrutura.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostEstrutura()) {
                gameData.subScore(gameData.getCostEstrutura());
                gameData.addValEstrutura(gameData.getIncEstrutura());
                if (gameData.getIncEstrutura() < 200) gameData.setIncEstrutura(gameData.getIncEstrutura() + 20);
                else gameData.setIncEstrutura(gameData.getIncEstrutura() * 1.2);
                gameData.multiplyCostEstrutura(1.2);
                updateUI();
            }
        });

        btnEstrutura.setOnLongClickListener(view -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostEstrutura()) {
                gameData.subScore(gameData.getCostEstrutura());
                gameData.addValEstrutura(gameData.getIncEstrutura());
                if (gameData.getIncEstrutura() < 200) gameData.setIncEstrutura(gameData.getIncEstrutura() + 20);
                else gameData.setIncEstrutura(gameData.getIncEstrutura() * 1.2);
                gameData.multiplyCostEstrutura(1.2);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        btnBarco.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostBarco()) {
                gameData.subScore(gameData.getCostBarco());
                gameData.addValBarco(gameData.getIncBarco());
                if (gameData.getIncBarco() < 1000) gameData.setIncBarco(gameData.getIncBarco() + 100);
                else gameData.setIncBarco(gameData.getIncBarco() * 1.2);
                gameData.multiplyCostBarco(1.2);
                updateUI();
            }
        });

        btnBarco.setOnLongClickListener(view -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostBarco()) {
                gameData.subScore(gameData.getCostBarco());
                gameData.addValBarco(gameData.getIncBarco());
                if (gameData.getIncBarco() < 1000) gameData.setIncBarco(gameData.getIncBarco() + 100);
                else gameData.setIncBarco(gameData.getIncBarco() * 1.2);
                gameData.multiplyCostBarco(1.2);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        btnBoatSpeedE.setOnClickListener(v -> {
            if (!gameData.isBoatSpeedEBought() && gameData.getScore() >= 1000.0) {
                gameData.subScore(1000.0);
                gameData.setBoatSpeedEBought(true);
                updateUI();
            }
        });

        btnGoToFights.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FightsActivity.class);
            startActivity(intent);
        });

        btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> showSettingsDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleManager.applyLocale(this);
        MusicManager.setVolume(gameData.getMusicVolume());
        gameData.lastUpdateTime = System.currentTimeMillis();
        ensureLeaderboardSession();
        handler.post(autoClickRunnable);
        handler.post(timerRunnable);
        startMusic();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoClickRunnable);
        handler.removeCallbacks(timerRunnable);
        // Do NOT stop music on pause to keep it playing across activities
    }

    private void prefetchLeaderboard() {
        ApiClient.fetchLeaderboard(this, new ApiClient.LeaderboardCallback() {
            @Override
            public void onSuccess(java.util.List<LeaderboardEntry> entries) {
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    private void ensureLeaderboardSession() {
        if (gameData.hasLeaderboardSession() || sessionRequestInFlight) {
            updateRunTimer();
            return;
        }

        sessionRequestInFlight = true;
        ApiClient.startSession(this, new ApiClient.SessionCallback() {
            @Override
            public void onSuccess(String sessionToken) {
                runOnUiThread(() -> {
                    sessionRequestInFlight = false;
                    if (sessionToken == null || sessionToken.trim().isEmpty()) {
                        Toast.makeText(MainActivity.this, R.string.error_start_session, Toast.LENGTH_LONG).show();
                        return;
                    }
                    gameData.startLeaderboardRun(sessionToken.trim());
                    updateRunTimer();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    sessionRequestInFlight = false;
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    updateRunTimer();
                });
            }
        });
    }

    private void updateRunTimer() {
        tvRunTimer.setText(getString(R.string.run_timer_label, gameData.getLeaderboardDisplaySeconds()));
    }

    private void startMusic() {
        if (!gameData.isMusicActive()) return;
        
        // Only play regular music if we are not currently playing boss music
        if (MusicManager.getCurrentResId() != R.raw.chefemusic) {
            MusicManager.play(this, R.raw.music);
        }
    }

    private void stopMusic() {
        MusicManager.stop();
    }

    private void updateUI() {
        tvScore.setText(getString(R.string.score_label, NumberFormatter.format(gameData.getScore())));
        updateRunTimer();
        btnUpgrade.setText(getString(R.string.upgrade_label, NumberFormatter.format(gameData.getUpgradeCost())));

        btnRemo.setText(getString(R.string.autoclicker_remo, NumberFormatter.format(gameData.getValRemo()), NumberFormatter.format(gameData.getIncRemo()), NumberFormatter.format(gameData.getCostRemo())));
        btnMaquina.setText(getString(R.string.autoclicker_maquina, NumberFormatter.format(gameData.getValMaquina()), NumberFormatter.format(gameData.getIncMaquina()), NumberFormatter.format(gameData.getCostMaquina())));
        btnEstrutura.setText(getString(R.string.autoclicker_estrutura, NumberFormatter.format(gameData.getValEstrutura()), NumberFormatter.format(gameData.getIncEstrutura()), NumberFormatter.format(gameData.getCostEstrutura())));
        btnBarco.setText(getString(R.string.autoclicker_barco, NumberFormatter.format(gameData.getValBarco()), NumberFormatter.format(gameData.getIncBarco()), NumberFormatter.format(gameData.getCostBarco())));
        
        btnUpgrade.setEnabled(gameData.getScore() >= gameData.getUpgradeCost());
        btnRemo.setEnabled(gameData.getScore() >= gameData.getCostRemo());
        btnMaquina.setEnabled(gameData.getScore() >= gameData.getCostMaquina());
        btnEstrutura.setEnabled(gameData.getScore() >= gameData.getCostEstrutura());
        btnBarco.setEnabled(gameData.getScore() >= gameData.getCostBarco());


        if (gameData.isBoatSpeedEBought()) {
            btnBoatSpeedE.setText(R.string.boat_speed_e_activated);
            btnBoatSpeedE.setEnabled(false);
            btnClick.setImageResource(R.drawable.boat_speed_e);
        } else {
            btnBoatSpeedE.setEnabled(gameData.getScore() >= 1000.0);
            btnClick.setImageResource(R.drawable.boat_row_large);
        }
    }

    private void showSettingsDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_settings, null);
        Spinner spinLanguage = dialogView.findViewById(R.id.spinLanguage);
        SeekBar sbMusicVolume = dialogView.findViewById(R.id.sbMusicVolume);
        TextView tvVolumeValue = dialogView.findViewById(R.id.tvVolumeValue);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.settings_language_entries,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLanguage.setAdapter(adapter);
        spinLanguage.setSelection(getLanguageSelectionIndex(gameData.getLanguageCode()));

        int volumeProgress = Math.round(gameData.getMusicVolume() * 100f);
        sbMusicVolume.setProgress(volumeProgress);
        tvVolumeValue.setText(getString(R.string.settings_volume_value, volumeProgress));
        sbMusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVolumeValue.setText(getString(R.string.settings_volume_value, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setNegativeButton(R.string.settings_cancel, null)
                .setPositiveButton(R.string.settings_save, null)
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String selectedLanguageCode = getLanguageCodeForSelection(spinLanguage.getSelectedItemPosition());
            int selectedVolumePercent = sbMusicVolume.getProgress();
            gameData.setLanguageCode(selectedLanguageCode);
            gameData.setMusicVolume(selectedVolumePercent / 100f);
            MusicManager.setVolume(gameData.getMusicVolume());
            LocaleManager.applyLocale(this);
            dialog.dismiss();
            recreate();
        }));

        dialog.show();
    }

    private int getLanguageSelectionIndex(String languageCode) {
        if ("es".equals(languageCode)) {
            return 1;
        }
        if ("en".equals(languageCode)) {
            return 2;
        }
        return 0;
    }

    private String getLanguageCodeForSelection(int index) {
        if (index == 1) {
            return "es";
        }
        if (index == 2) {
            return "en";
        }
        return "pt";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}