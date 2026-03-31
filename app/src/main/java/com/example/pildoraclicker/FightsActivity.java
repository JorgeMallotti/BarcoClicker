package com.example.pildoraclicker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FightsActivity extends AppCompatActivity {

    private GameData gameData;
    private TextView tvScore, tvAttack, tvDefense, tvHealth, tvAPS;
    private TextView tvPlayerHealthLabel, tvEnemyHealthLabel;
    private ProgressBar pbPlayerHealth, pbEnemyHealth;
    private Button btnBack, btnUpgradeAttack, btnUpgradeDefense, btnUpgradeHealth, btnUpgradeAPS;
    private ImageView ivPlayer, ivEnemy;
    private MediaPlayer gritoPlayer;

    private final Handler handler = new Handler(Looper.getMainLooper());
    
    // Timer para produção de pílulas (Clicker) - Agora 10x por segundo
    private final Runnable autoClickRunnable = new Runnable() {
        @Override
        public void run() {
            gameData.updateScore();
            updateUI();
            handler.postDelayed(this, 100);
        }
    };

    // Timer para o Combate
    private final Runnable fightRunnable = new Runnable() {
        @Override
        public void run() {
            doCombatTick();
            handler.postDelayed(this, 100); // Processa combate a cada 0.1s para suavidade
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fights);

        gameData = GameData.getInstance();
        gameData.resetEnemy(); // Começa com o inimigo com vida cheia

        // Vincular UI
        tvScore = findViewById(R.id.tvScoreFights);
        tvAttack = findViewById(R.id.tvAttack);
        tvDefense = findViewById(R.id.tvDefense);
        tvHealth = findViewById(R.id.tvHealth);
        tvAPS = findViewById(R.id.tvAPS);
        
        tvPlayerHealthLabel = findViewById(R.id.tvPlayerHealthLabel);
        tvEnemyHealthLabel = findViewById(R.id.tvEnemyHealthLabel);
        pbPlayerHealth = findViewById(R.id.pbPlayerHealth);
        pbEnemyHealth = findViewById(R.id.pbEnemyHealth);
        
        ivPlayer = findViewById(R.id.ivPlayer);
        ivEnemy = findViewById(R.id.ivEnemy);

        btnBack = findViewById(R.id.btnBack);
        btnUpgradeAttack = findViewById(R.id.btnUpgradeAttack);
        btnUpgradeDefense = findViewById(R.id.btnUpgradeDefense);
        btnUpgradeHealth = findViewById(R.id.btnUpgradeHealth);
        btnUpgradeAPS = findViewById(R.id.btnUpgradeAPS);

        btnBack.setOnClickListener(v -> finish());

        // Lógica de Upgrades
        // Clique curto: Compra 1 nível
        // Clique longo: Compra o máximo possível com o score atual

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

        btnUpgradeAPS.setOnClickListener(v -> {
            if (gameData.getScore() >= gameData.getCostAPS()) {
                gameData.subScore(gameData.getCostAPS());
                gameData.addAttackSpeed(0.1);
                gameData.multiplyCostAPS(1.6);
                updateUI();
            }
        });

        btnUpgradeAPS.setOnLongClickListener(v -> {
            boolean bought = false;
            while (gameData.getScore() >= gameData.getCostAPS()) {
                gameData.subScore(gameData.getCostAPS());
                gameData.addAttackSpeed(0.1);
                gameData.multiplyCostAPS(1.6);
                bought = true;
            }
            if (bought) updateUI();
            return true;
        });
    }

    private void doCombatTick() {
        // Cálculo de Dano: (Ataque * AtaquesPorSegundo) / 10 (porque o tick é 0.1s)
        double playerDamagePerTick = (Math.max(1, gameData.getAttack() - gameData.getEnemyDefense()) * gameData.getAttackSpeed()) / 10.0;
        double enemyDamagePerTick = (Math.max(1, gameData.getEnemyAttack() - gameData.getDefense()) * gameData.getEnemyAttackSpeed()) / 10.0;

        // Aplicar dano
        gameData.subEnemyCurrentHealth(playerDamagePerTick);
        gameData.subCurrentHealth(enemyDamagePerTick);

        // Verificar Vitória
        if (gameData.getEnemyCurrentHealth() <= 0) {
            playGritoSound();
            gameData.addScore(gameData.getEnemyReward());
            
            if (gameData.isBossActive()) {
                Toast.makeText(this, "BOSS DERROTADO: FUNGO VELHO!", Toast.LENGTH_SHORT).show();
                gameData.setBossActive(false);
                gameData.resetFungoVictories();
                // Retorna o multiplicador ao normal para os inimigos comuns
                gameData.multiplyEnemyMaxHealth(0.10);
                gameData.multiplyEnemyAttack(0.10);
                gameData.multiplyEnemyReward(0.10);
            } else {
                Toast.makeText(this, R.string.victory, Toast.LENGTH_SHORT).show();
                gameData.addFungoVictory();
                
                // Inimigo fica mais forte a cada derrota
                gameData.multiplyEnemyMaxHealth(1.2);
                gameData.multiplyEnemyAttack(1.1);
                gameData.multiplyEnemyReward(1.3);

                // Verifica se o Boss deve aparecer agora (após 5 vitórias)
                if (gameData.getFungoVictories() >= 5) {
                    gameData.setBossActive(true);
                    gameData.multiplyEnemyMaxHealth(10.0);
                    gameData.multiplyEnemyAttack(10.0);
                    gameData.multiplyEnemyReward(10.0);
                    Toast.makeText(this, "UM FUNGO VELHO APARECEU!", Toast.LENGTH_LONG).show();
                }
            }
            
            gameData.resetEnemy();
        }

        // Verificar Derrota
        if (gameData.getCurrentHealth() <= 0) {
            Toast.makeText(this, R.string.defeat, Toast.LENGTH_SHORT).show();
            gameData.setCurrentHealth(gameData.getMaxHealth()); // Ressuscita
            gameData.resetEnemy();
        }

        updateCombatUI();
    }

    private void playGritoSound() {
        // Se já está a tocar, pára e reinicia
        if (gameData.isGritoSoundPlaying() && gritoPlayer != null) {
            try {
                gritoPlayer.stop();
                gritoPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        // Barras de Vida
        pbPlayerHealth.setMax((int) gameData.getMaxHealth());
        pbPlayerHealth.setProgress((int) Math.max(0, gameData.getCurrentHealth()));
        tvPlayerHealthLabel.setText(String.format("Caçador de Fungos: %.0f/%.0f", gameData.getCurrentHealth(), gameData.getMaxHealth()));

        pbEnemyHealth.setMax((int) gameData.getEnemyMaxHealth());
        pbEnemyHealth.setProgress((int) Math.max(0, gameData.getEnemyCurrentHealth()));
        
        String enemyName = gameData.isBossActive() ? "Fungo Velho" : "Fungo";
        tvEnemyHealthLabel.setText(String.format("%s: %.0f/%.0f", enemyName, gameData.getEnemyCurrentHealth(), gameData.getEnemyMaxHealth()));

        // Troca a imagem se for Boss
        if (gameData.isBossActive()) {
            ivEnemy.setImageResource(R.drawable.fungo_idle_boss);
        } else {
            ivEnemy.setImageResource(R.drawable.fungo_idle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameData.lastUpdateTime = System.currentTimeMillis();
        handler.post(autoClickRunnable);
        handler.post(fightRunnable);
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoClickRunnable);
        handler.removeCallbacks(fightRunnable);
        // Garante que o som pára ao sair da atividade
        if (gritoPlayer != null) {
            gritoPlayer.release();
            gritoPlayer = null;
            gameData.setGritoSoundPlaying(false);
        }
    }

    private void updateUI() {
        tvScore.setText(getString(R.string.score_label, gameData.getScore()));
        tvAttack.setText(getString(R.string.stat_attack, gameData.getAttack()));
        tvDefense.setText(getString(R.string.stat_defense, gameData.getDefense()));
        tvHealth.setText(getString(R.string.stat_health, gameData.getCurrentHealth(), gameData.getMaxHealth()));
        tvAPS.setText(getString(R.string.stat_aps, gameData.getAttackSpeed()));

        btnUpgradeAttack.setText(getString(R.string.upgrade_attack, gameData.getCostAttack()));
        btnUpgradeDefense.setText(getString(R.string.upgrade_defense, gameData.getCostDefense()));
        btnUpgradeHealth.setText(getString(R.string.upgrade_health, gameData.getCostHealth()));
        btnUpgradeAPS.setText(getString(R.string.upgrade_aps, gameData.getCostAPS()));

        btnUpgradeAttack.setEnabled(gameData.getScore() >= gameData.getCostAttack());
        btnUpgradeDefense.setEnabled(gameData.getScore() >= gameData.getCostDefense());
        btnUpgradeHealth.setEnabled(gameData.getScore() >= gameData.getCostHealth());
        btnUpgradeAPS.setEnabled(gameData.getScore() >= gameData.getCostAPS());
        
        updateCombatUI();
    }
}