package com.example.pildoraclicker;

import android.content.Intent;
import android.provider.Settings;
import android.text.InputFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class FightsActivity extends AppCompatActivity {

    private GameData gameData;
    private TextView tvScore, tvAttack, tvDefense, tvHealth, tvAGI, tvLucky;
    private TextView tvPlayerHealthLabel, tvEnemyHealthLabel, tvBossWarning;
    private TextView tvRunTimer;
    private ProgressBar pbPlayerHealth, pbEnemyHealth;
    private Button btnBack, btnUpgradeAttack, btnUpgradeDefense, btnUpgradeHealth, btnUpgradeAGI, btnUpgradeLucky;
    private Button btnMusic, btnSettings;
    private Button btnActionAttack, btnActionDefend;
    private LinearLayout layoutCombatMenu;
    private ImageView ivPlayer, ivEnemy;
    private MediaPlayer gritoPlayer;
    private MediaPlayer effectPlayer;
    private MediaPlayer comboPlayer;
    private MediaPlayer musicPlayer;
    private int currentMusicResId = -1;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    private boolean isPlayerTurn = false;
    private boolean isPlayerDefending = false;
    private boolean isBossPreparingSpecial = false;

    // Timer para produção de barcos (Clicker)
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
        setContentView(R.layout.activity_fights);

        gameData = GameData.getInstance();
        MusicManager.setVolume(gameData.getMusicVolume());
        gameData.resetEnemy();

        // Vincular UI
        tvScore = findViewById(R.id.tvScoreFights);
        tvRunTimer = findViewById(R.id.tvRunTimerFights);
        tvAttack = findViewById(R.id.tvAttack);
        tvDefense = findViewById(R.id.tvDefense);
        tvHealth = findViewById(R.id.tvHealth);
        tvAGI = findViewById(R.id.tvAGI);
        tvLucky = findViewById(R.id.tvLucky);
        
        tvPlayerHealthLabel = findViewById(R.id.tvPlayerHealthLabel);
        tvEnemyHealthLabel = findViewById(R.id.tvEnemyHealthLabel);
        tvBossWarning = findViewById(R.id.tvBossWarning);
        pbPlayerHealth = findViewById(R.id.pbPlayerHealth);
        pbEnemyHealth = findViewById(R.id.pbEnemyHealth);
        
        ivPlayer = findViewById(R.id.ivPlayer);
        ivEnemy = findViewById(R.id.ivEnemy);

        layoutCombatMenu = findViewById(R.id.layoutCombatMenu);
        btnActionAttack = findViewById(R.id.btn_attack);
        btnActionDefend = findViewById(R.id.btn_defend);

        btnBack = findViewById(R.id.btn_back);
        btnMusic = findViewById(R.id.btnMusic);
        btnSettings = findViewById(R.id.btnSettings);
        btnUpgradeAttack = findViewById(R.id.btnUpgradeAttack);
        btnUpgradeDefense = findViewById(R.id.btnUpgradeDefense);
        btnUpgradeHealth = findViewById(R.id.btnUpgradeHealth);
        btnUpgradeAGI = findViewById(R.id.btnUpgradeAGI);
        btnUpgradeLucky = findViewById(R.id.btnUpgradeLucky);

        btnBack.setOnClickListener(v -> finish());
        btnMusic.setOnClickListener(v -> {
            if (!gameData.isMusicBought() && gameData.getScore() >= 1000.0) {
                gameData.subScore(1000.0);
                gameData.setMusicBought(true);
                updateUI();
            }
        });
        btnSettings.setOnClickListener(v -> showSettingsDialog());

        setupUpgradeButtons();

        // Ações de Combate
        btnActionAttack.setOnClickListener(v -> playerAttack());
        btnActionDefend.setOnClickListener(v -> playerDefend());

        determineFirstTurn();
    }

    private void setupUpgradeButtons() {
        // Attack
        btnUpgradeAttack.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostAttack()) {
                gameData.subScore(gameData.getCostAttack());
                gameData.addAttack(2);
                gameData.multiplyCostAttack(1.4);
                updateUI();
            }
        });
        btnUpgradeAttack.setOnLongClickListener(v -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostAttack()) {
                gameData.subScore(gameData.getCostAttack());
                gameData.addAttack(2);
                gameData.multiplyCostAttack(1.4);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        // Defense
        btnUpgradeDefense.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostDefense()) {
                gameData.subScore(gameData.getCostDefense());
                gameData.addDefense(1);
                gameData.multiplyCostDefense(1.4);
                updateUI();
            }
        });
        btnUpgradeDefense.setOnLongClickListener(v -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostDefense()) {
                gameData.subScore(gameData.getCostDefense());
                gameData.addDefense(1);
                gameData.multiplyCostDefense(1.4);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        // Health
        btnUpgradeHealth.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostHealth()) {
                gameData.subScore(gameData.getCostHealth());
                gameData.addMaxHealth(50);
                gameData.setCurrentHealth(gameData.getMaxHealth());
                gameData.multiplyCostHealth(1.4);
                updateUI();
            }
        });
        btnUpgradeHealth.setOnLongClickListener(v -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostHealth()) {
                gameData.subScore(gameData.getCostHealth());
                gameData.addMaxHealth(50);
                gameData.multiplyCostHealth(1.4);
                bought = true;
            }
            if (bought) {
                gameData.setCurrentHealth(gameData.getMaxHealth());
                updateUI();
            }
            return true;
        });

        // AGI
        btnUpgradeAGI.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostAGI()) {
                gameData.subScore(gameData.getCostAGI());
                gameData.addAGI(2);
                gameData.multiplyCostAGI(1.4);
                updateUI();
            }
        });
        btnUpgradeAGI.setOnLongClickListener(v -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostAGI()) {
                gameData.subScore(gameData.getCostAGI());
                gameData.addAGI(2);
                gameData.multiplyCostAGI(1.4);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });

        // Lucky
        btnUpgradeLucky.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostLucky()) {
                gameData.subScore(gameData.getCostLucky());
                gameData.addLucky(1);
                gameData.multiplyCostLucky(1.4);
                updateUI();
            }
        });
        btnUpgradeLucky.setOnLongClickListener(v -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostLucky()) {
                gameData.subScore(gameData.getCostLucky());
                gameData.addLucky(1);
                gameData.multiplyCostLucky(1.4);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });
    }

    private void determineFirstTurn() {
        isPlayerDefending = false;
        isBossPreparingSpecial = false;
        if (gameData.getAGI() >= gameData.getEnemyAGI()) {
            startPlayerTurn();
        } else {
            startEnemyTurn();
        }
    }

    private void startPlayerTurn() {
        isPlayerTurn = true;
        isPlayerDefending = false;
        layoutCombatMenu.setVisibility(View.VISIBLE);
    }

    private void playerAttack() {
        if (!isPlayerTurn) return;
        isPlayerTurn = false;
        layoutCombatMenu.setVisibility(View.GONE);

        playSound(R.raw.simple_hit);

        double baseDamage = gameData.getAttack() * 4.0;
        double mitigation = gameData.getEnemyDefense() * 2.0;
        double finalDamage = Math.max(1, baseDamage - mitigation);

        if (random.nextDouble() * 100 < gameData.getLucky()) {
            finalDamage *= 2.0;
        }

        gameData.subEnemyCurrentHealth(finalDamage);
        onActionFinished();
    }

    private void playerDefend() {
        if (!isPlayerTurn) return;
        isPlayerTurn = false;
        isPlayerDefending = true;
        layoutCombatMenu.setVisibility(View.GONE);
        onActionFinished();
    }

    private void onActionFinished() {
        updateCombatUI();
        if (gameData.getEnemyCurrentHealth() <= 0) {
            checkCombatStatus();
        } else {
            if (isBossPreparingSpecial) {
                handler.postDelayed(this::executeBossSpecial, 1000);
            } else {
                handler.postDelayed(this::startEnemyTurn, 1000);
            }
        }
    }

    private void startEnemyTurn() {
        if (gameData.isBossActive() && !isBossPreparingSpecial && random.nextInt(100) < 30) {
            isBossPreparingSpecial = true;
            tvBossWarning.setVisibility(View.VISIBLE);
            handler.postDelayed(this::startPlayerTurn, 1000);
            return;
        }

        playSound(R.raw.simple_hit);

        double baseDamage = gameData.getEnemyAttack() * 4.0;
        double defMult = isPlayerDefending ? 3.0 : 1.0;
        double mitigation = (gameData.getDefense() * 2.0) * defMult;
        double finalDamage = Math.max(1, baseDamage - mitigation);

        gameData.subCurrentHealth(finalDamage);
        checkCombatStatus();

        if (gameData.getCurrentHealth() > 0) {
            handler.postDelayed(this::startPlayerTurn, 1000);
        }
    }

    private String getEnemyName() {
        if (!gameData.isBossActive()) return getString(R.string.enemy_name_fungo);
        int v = gameData.getFungoVictories();
        if (v == 5) return getString(R.string.enemy_name_boss1);
        if (v == 10) return getString(R.string.enemy_name_boss2);
        return getString(R.string.enemy_name_boss3);
    }

    private void executeBossSpecial() {
        isBossPreparingSpecial = false;
        tvBossWarning.setVisibility(View.GONE);
        
        if (comboPlayer != null) { comboPlayer.release(); }
        comboPlayer = MediaPlayer.create(this, R.raw.hit_combo);
        if (comboPlayer != null) {
            comboPlayer.setLooping(true);
            comboPlayer.start();
        }

        final int totalHits = 800;
        final int durationMs = 5000;
        final int intervalMs = 100;
        final int totalIntervals = durationMs / intervalMs;
        final int hitsPerInterval = totalHits / totalIntervals;
        
        handler.post(new Runnable() {
            int intervalsDone = 0;
            @Override
            public void run() {
                if (intervalsDone < totalIntervals && gameData.getCurrentHealth() > 0) {
                    double baseDamage = gameData.getEnemyAttack() * 2.0;
                    double defMult = isPlayerDefending ? 3.0 : 1.0;
                    double mitigation = (gameData.getDefense() * 2.0) * defMult;
                    double finalDamagePerHit = Math.max(1, baseDamage - mitigation);
                    
                    gameData.subCurrentHealth(finalDamagePerHit * hitsPerInterval);
                    updateCombatUI();
                    
                    intervalsDone++;
                    handler.postDelayed(this, intervalMs);
                } else {
                    if (comboPlayer != null) {
                        comboPlayer.stop();
                        comboPlayer.release();
                        comboPlayer = null;
                    }
                    if (gameData.getCurrentHealth() <= 0) {
                        checkCombatStatus();
                    } else {
                        startPlayerTurn();
                    }
                }
            }
        });
    }

    private void checkCombatStatus() {
        updateCombatUI();

        // Vitória
        if (gameData.getEnemyCurrentHealth() <= 0) {
            playGritoSound();
            gameData.addScore(gameData.getEnemyReward());
            
            if (gameData.isBossActive()) {
                int v = gameData.getFungoVictories();
                gameData.setBossActive(false);
                gameData.addFungoVictory(); // Increment count to progress to next milestone
                
                if (v == 15) {
                    showWinDialog();
                    return;
                }

                // Normal balance after boss
                gameData.multiplyEnemyMaxHealth(0.10);
                gameData.multiplyEnemyAttack(0.10);
                gameData.multiplyEnemyReward(0.10);
                gameData.setEnemyAGI(gameData.getEnemyAGI() * 0.10);
                updateMusic();
            } else {
                gameData.addFungoVictory();
                gameData.multiplyEnemyMaxHealth(1.2);
                gameData.multiplyEnemyAttack(1.1);
                gameData.multiplyEnemyReward(1.3);
                gameData.setEnemyAGI(gameData.getEnemyAGI() * 1.1);

                int v = gameData.getFungoVictories();
                if (v == 5 || v == 10 ) {
                    gameData.setBossActive(true);
                    gameData.multiplyEnemyMaxHealth(10.0);
                    gameData.multiplyEnemyAttack(10.0);
                    gameData.multiplyEnemyReward(10.0);
                    gameData.setEnemyAGI(gameData.getEnemyAGI() * 10.0);
                } else if (v == 15) {
                    gameData.setBossActive(true);
                    gameData.multiplyEnemyMaxHealth(20.0);
                    gameData.multiplyEnemyAttack(10.0);
                    gameData.multiplyEnemyReward(1000.0);
                    gameData.setEnemyAGI(gameData.getEnemyAGI() * 10.0);
                    updateMusic();
                }
            }
            gameData.resetEnemy();
            determineFirstTurn();
        }

        // Derrota
        if (gameData.getCurrentHealth() <= 0) {
            playSound(R.raw.dying);
            gameData.setCurrentHealth(gameData.getMaxHealth());
            gameData.resetEnemy();
            determineFirstTurn();
        }
    }

    private void showWinDialog() {
        MusicManager.stop();
        gameData.freezeLeaderboardDisplayTimer();
        handler.removeCallbacks(timerRunnable);
        updateRunTimer();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_end_game_submit, null);
        EditText input = dialogView.findViewById(R.id.etPlayerName);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton(R.string.leaderboard_submit_action, null)
                .setNegativeButton(R.string.leaderboard_submit_cancel, (dialogInterface, which) -> restartGameFlow())
                .create();

        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String playerName = input.getText().toString().trim();
            if (playerName.isEmpty()) {
                input.setError(getString(R.string.error_player_name_required));
                return;
            }

            if (!gameData.hasLeaderboardSession()) {
                Toast.makeText(this, R.string.error_missing_session, Toast.LENGTH_LONG).show();
                restartGameFlow();
                dialog.dismiss();
                return;
            }

            input.setError(null);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            submitLeaderboardScore(playerName, dialog);
        }));
        dialog.show();
    }

    private void submitLeaderboardScore(String playerName, AlertDialog dialog) {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        int submissionSeconds = gameData.getLeaderboardSubmissionSeconds();

        ApiClient.submitScore(
                this,
                playerName,
                submissionSeconds,
                gameData.getLeaderboardSessionToken(),
                deviceId,
                new ApiClient.SubmitScoreCallback() {
                    @Override
                    public void onSuccess(double storedTimeSeconds) {
                        runOnUiThread(() -> {
                            Toast.makeText(
                                    FightsActivity.this,
                                    getString(R.string.score_submit_success, storedTimeSeconds),
                                    Toast.LENGTH_LONG
                            ).show();
                            dialog.dismiss();
                            LeaderboardCache.setEntries(java.util.Collections.emptyList());
                            restartGameFlow();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            Toast.makeText(FightsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        });
                    }
                }
        );
    }

    private void restartGameFlow() {
        gameData.resetGame();
        Intent intent = new Intent(FightsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void playSound(int soundResId) {
        if (effectPlayer != null) {
            effectPlayer.release();
        }
        effectPlayer = MediaPlayer.create(this, soundResId);
        if (effectPlayer != null) {
            effectPlayer.setOnCompletionListener(MediaPlayer::release);
            effectPlayer.start();
        }
    }

    private void playGritoSound() {
        if (gameData.isGritoSoundPlaying() && gritoPlayer != null) {
            try { gritoPlayer.stop(); gritoPlayer.release(); } catch (Exception e) {}
            gritoPlayer = null;
            gameData.setGritoSoundPlaying(false);
        }
        gritoPlayer = MediaPlayer.create(this, R.raw.grito);
        if (gritoPlayer != null) {
            gameData.setGritoSoundPlaying(true);
            gritoPlayer.setOnCompletionListener(mp -> {
                mp.release();
                gritoPlayer = null;
                gameData.setGritoSoundPlaying(false);
            });
            gritoPlayer.start();
        }
    }

    private void updateCombatUI() {
        pbPlayerHealth.setMax((int) gameData.getMaxHealth());
        pbPlayerHealth.setProgress((int) Math.max(0, gameData.getCurrentHealth()));
        tvPlayerHealthLabel.setText(getString(R.string.tu_label, NumberFormatter.format(gameData.getCurrentHealth()), NumberFormatter.format(gameData.getMaxHealth())));

        pbEnemyHealth.setMax((int) gameData.getEnemyMaxHealth());
        pbEnemyHealth.setProgress((int) Math.max(0, gameData.getEnemyCurrentHealth()));
        
        String enemyName = getEnemyName();
        tvEnemyHealthLabel.setText(getString(R.string.stat_health_enemy_label, enemyName, NumberFormatter.format(gameData.getEnemyCurrentHealth()), NumberFormatter.format(gameData.getEnemyMaxHealth())));

        if (gameData.isBossActive()) {
            int v = gameData.getFungoVictories();
            if (v == 5) ivEnemy.setImageResource(R.drawable.fungo_idle_boss);
            else if (v == 10) ivEnemy.setImageResource(R.drawable.fungo_bravo);
            else ivEnemy.setImageResource(R.drawable.fungo_estrategista);
        } else {
            ivEnemy.setImageResource(R.drawable.fungo_idle);
        }

        ivPlayer.setImageResource(gameData.isMusicBought() ? R.drawable.warrior_idle : R.drawable.player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleManager.applyLocale(this);
        MusicManager.setVolume(gameData.getMusicVolume());
        gameData.lastUpdateTime = System.currentTimeMillis();
        gameData.resumeLeaderboardDisplayTimer();
        handler.post(autoClickRunnable);
        handler.post(timerRunnable);
        updateMusic();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoClickRunnable);
        handler.removeCallbacks(timerRunnable);
        if (comboPlayer != null) {
            comboPlayer.stop();
            comboPlayer.release();
            comboPlayer = null;
        }
    }

    private void updateRunTimer() {
        tvRunTimer.setText(getString(R.string.run_timer_label, gameData.getLeaderboardDisplaySeconds()));
    }

    private void updateUI() {
        tvScore.setText(getString(R.string.score_label, NumberFormatter.format(gameData.getScore())));
        updateRunTimer();
        tvAttack.setText(getString(R.string.stat_attack_label, NumberFormatter.format(gameData.getAttack())));
        tvDefense.setText(getString(R.string.stat_defense_label, NumberFormatter.format(gameData.getDefense())));
        tvHealth.setText(getString(R.string.stat_health_label, NumberFormatter.format(gameData.getCurrentHealth()), NumberFormatter.format(gameData.getMaxHealth())));
        tvAGI.setText(getString(R.string.stat_agi_label, NumberFormatter.format(gameData.getAGI())));
        tvLucky.setText(getString(R.string.stat_lucky_label, NumberFormatter.format(gameData.getLucky())));

        btnUpgradeAttack.setText(getString(R.string.upgrade_attack, NumberFormatter.format(gameData.getCostAttack())));
        btnUpgradeDefense.setText(getString(R.string.upgrade_defense, NumberFormatter.format(gameData.getCostDefense())));
        btnUpgradeHealth.setText(getString(R.string.upgrade_health, NumberFormatter.format(gameData.getCostHealth())));
        btnUpgradeAGI.setText(getString(R.string.upgrade_agi, NumberFormatter.format(gameData.getCostAGI())));
        btnUpgradeLucky.setText(getString(R.string.upgrade_lucky, NumberFormatter.format(gameData.getCostLucky())));

        btnUpgradeAttack.setEnabled(gameData.getScore() >= gameData.getCostAttack());
        btnUpgradeDefense.setEnabled(gameData.getScore() >= gameData.getCostDefense());
        btnUpgradeHealth.setEnabled(gameData.getScore() >= gameData.getCostHealth());
        btnUpgradeAGI.setEnabled(gameData.getScore() >= gameData.getCostAGI());
        btnUpgradeLucky.setEnabled(gameData.getScore() >= gameData.getCostLucky());

        if (gameData.isMusicBought()) {
            btnMusic.setText(R.string.player_upgrade_activated);
            btnMusic.setEnabled(false);
        } else {
            btnMusic.setText(R.string.buy_player_upgrade);
            btnMusic.setEnabled(gameData.getScore() >= 1000.0);
        }
        
        updateCombatUI();
    }

    private void updateMusic() {
        if (!gameData.isMusicActive()) {
            MusicManager.stop();
            return;
        }

        int targetResId = (gameData.isBossActive() && gameData.getFungoVictories() == 15)
                ? R.raw.chefemusic
                : R.raw.music;

        MusicManager.play(this, targetResId);
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
        if (effectPlayer != null) {
            effectPlayer.release();
            effectPlayer = null;
        }
        if (comboPlayer != null) {
            comboPlayer.release();
            comboPlayer = null;
        }
    }
}