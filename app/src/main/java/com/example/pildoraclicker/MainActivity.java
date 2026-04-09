package com.example.pildoraclicker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private GameData gameData;

    private TextView tvScore;
    private ImageButton btnClick;
    private Button btnUpgrade, btnRemo, btnMaquina, btnEstrutura, btnBarco, btnMusic, btnGoToFights, btnBoatSpeedE;


    private final Handler handler = new Handler(Looper.getMainLooper());
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
        setContentView(R.layout.activity_main);

        gameData = GameData.getInstance();

        tvScore = findViewById(R.id.tvScore);
        btnClick = findViewById(R.id.btnClick);
        btnUpgrade = findViewById(R.id.btnUpgrade);
        btnRemo = findViewById(R.id.btnRemo);
        btnMaquina = findViewById(R.id.btnMaquina);
        btnEstrutura = findViewById(R.id.btnEstrutura);
        btnBarco = findViewById(R.id.btnBarco);
        btnMusic = findViewById(R.id.btnMusic);
        btnGoToFights = findViewById(R.id.btnGoToFights);
        btnBoatSpeedE = findViewById(R.id.btnBoatSpeedE);

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

        btnMusic.setOnClickListener(v -> {
            if (!gameData.isMusicBought()) {
                if (gameData.getScore() >= 1000.0) {
                    gameData.subScore(1000.0);
                    gameData.setMusicBought(true);
                    gameData.setMusicActive(true);
                    startMusic();
                }
            } else {
                gameData.setMusicActive(!gameData.isMusicActive());
                if (gameData.isMusicActive()) {
                    startMusic();
                } else {
                    stopMusic();
                }
            }
            updateUI();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameData.lastUpdateTime = System.currentTimeMillis();
        handler.post(autoClickRunnable);
        startMusic();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoClickRunnable);
        // Do NOT stop music on pause to keep it playing across activities
    }

    private void startMusic() {
        if (!gameData.isMusicBought() || !gameData.isMusicActive()) return;
        
        // Only play regular music if we are not currently playing boss music
        if (MusicManager.getCurrentResId() != R.raw.chefemusic) {
            MusicManager.play(this, R.raw.music);
        }
    }

    private void stopMusic() {
        MusicManager.stop();
    }

    private void updateUI() {
        tvScore.setText(getString(R.string.score_label, gameData.getScore()));
        btnUpgrade.setText(getString(R.string.upgrade_label, gameData.getUpgradeCost()));
        
        btnRemo.setText(getString(R.string.autoclicker_remo, gameData.getValRemo(), gameData.getIncRemo(), gameData.getCostRemo()));
        btnMaquina.setText(getString(R.string.autoclicker_maquina, gameData.getValMaquina(), gameData.getIncMaquina(), gameData.getCostMaquina()));
        btnEstrutura.setText(getString(R.string.autoclicker_estrutura, gameData.getValEstrutura(), gameData.getIncEstrutura(), gameData.getCostEstrutura()));
        btnBarco.setText(getString(R.string.autoclicker_barco, gameData.getValBarco(), gameData.getIncBarco(), gameData.getCostBarco()));
        
        btnUpgrade.setEnabled(gameData.getScore() >= gameData.getUpgradeCost());
        btnRemo.setEnabled(gameData.getScore() >= gameData.getCostRemo());
        btnMaquina.setEnabled(gameData.getScore() >= gameData.getCostMaquina());
        btnEstrutura.setEnabled(gameData.getScore() >= gameData.getCostEstrutura());
        btnBarco.setEnabled(gameData.getScore() >= gameData.getCostBarco());

        if (gameData.isMusicBought()) {
            btnMusic.setText(gameData.isMusicActive() ? R.string.music_on : R.string.music_off);
            btnMusic.setEnabled(true);
        } else {
            btnMusic.setEnabled(gameData.getScore() >= 1000.0);
        }

        if (gameData.isBoatSpeedEBought()) {
            btnBoatSpeedE.setText(R.string.boat_speed_e_activated);
            btnBoatSpeedE.setEnabled(false);
            btnClick.setImageResource(R.drawable.boat_speed_e);
        } else {
            btnBoatSpeedE.setEnabled(gameData.getScore() >= 1000.0);
            btnClick.setImageResource(R.drawable.boat_row_large);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}