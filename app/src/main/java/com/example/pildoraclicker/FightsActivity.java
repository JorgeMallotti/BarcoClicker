package com.example.pildoraclicker;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class FightsActivity extends AppCompatActivity {

    private GameData gameData;
    private TextView tvScore, tvAttack, tvDefense, tvHealth, tvAGI, tvLucky;
    private TextView tvPlayerHealthLabel, tvEnemyHealthLabel, tvBossWarning;
    private ProgressBar pbPlayerHealth, pbEnemyHealth;
    private Button btnBack, btnUpgradeAttack, btnUpgradeDefense, btnUpgradeHealth, btnUpgradeAGI, btnUpgradeLucky;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fights);

        gameData = GameData.getInstance();
        gameData.resetEnemy();

        // Vincular UI
        tvScore = findViewById(R.id.tvScoreFights);
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
        btnUpgradeAttack = findViewById(R.id.btnUpgradeAttack);
        btnUpgradeDefense = findViewById(R.id.btnUpgradeDefense);
        btnUpgradeHealth = findViewById(R.id.btnUpgradeHealth);
        btnUpgradeAGI = findViewById(R.id.btnUpgradeAGI);
        btnUpgradeLucky = findViewById(R.id.btnUpgradeLucky);

        btnBack.setOnClickListener(v -> finish());

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
        new AlertDialog.Builder(this)
                .setTitle("Parabéns!")
                .setMessage("Derrotaste o Fungo Estrategista e ganhaste o jogo! Queres jogar novamente?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialog, which) -> {
                    gameData.resetGame();
                    Intent intent = new Intent(FightsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Não", (dialog, which) -> finish())
                .show();
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
        tvPlayerHealthLabel.setText(getString(R.string.tu_label, gameData.getCurrentHealth(), gameData.getMaxHealth()));

        pbEnemyHealth.setMax((int) gameData.getEnemyMaxHealth());
        pbEnemyHealth.setProgress((int) Math.max(0, gameData.getEnemyCurrentHealth()));
        
        String enemyName = getEnemyName();
        tvEnemyHealthLabel.setText(getString(R.string.stat_health_enemy_label, enemyName, gameData.getEnemyCurrentHealth(), gameData.getEnemyMaxHealth()));

        if (gameData.isBossActive()) {
            int v = gameData.getFungoVictories();
            if (v == 5) ivEnemy.setImageResource(R.drawable.fungo_idle_boss);
            else if (v == 10) ivEnemy.setImageResource(R.drawable.fungo_bravo);
            else ivEnemy.setImageResource(R.drawable.fungo_estrategista);
        } else {
            ivEnemy.setImageResource(R.drawable.fungo_idle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameData.lastUpdateTime = System.currentTimeMillis();
        handler.post(autoClickRunnable);
        updateMusic();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoClickRunnable);
        if (comboPlayer != null) {
            comboPlayer.stop();
            comboPlayer.release();
            comboPlayer = null;
        }
    }

    private void updateUI() {
        tvScore.setText(getString(R.string.score_label, gameData.getScore()));
        tvAttack.setText(getString(R.string.stat_attack_label, gameData.getAttack()));
        tvDefense.setText(getString(R.string.stat_defense_label, gameData.getDefense()));
        tvHealth.setText(getString(R.string.stat_health_label, gameData.getCurrentHealth(), gameData.getMaxHealth()));
        tvAGI.setText(getString(R.string.stat_agi_label, gameData.getAGI()));
        tvLucky.setText(getString(R.string.stat_lucky_label, gameData.getLucky()));

        btnUpgradeAttack.setText(getString(R.string.upgrade_attack, gameData.getCostAttack()));
        btnUpgradeDefense.setText(getString(R.string.upgrade_defense, gameData.getCostDefense()));
        btnUpgradeHealth.setText(getString(R.string.upgrade_health, gameData.getCostHealth()));
        btnUpgradeAGI.setText(getString(R.string.upgrade_agi, gameData.getCostAGI()));
        btnUpgradeLucky.setText(getString(R.string.upgrade_lucky, gameData.getCostLucky()));

        btnUpgradeAttack.setEnabled(gameData.getScore() >= gameData.getCostAttack());
        btnUpgradeDefense.setEnabled(gameData.getScore() >= gameData.getCostDefense());
        btnUpgradeHealth.setEnabled(gameData.getScore() >= gameData.getCostHealth());
        btnUpgradeAGI.setEnabled(gameData.getScore() >= gameData.getCostAGI());
        btnUpgradeLucky.setEnabled(gameData.getScore() >= gameData.getCostLucky());
        
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