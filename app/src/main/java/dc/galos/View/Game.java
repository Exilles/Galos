package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.GameManager;
import dc.galos.R;

public class Game extends AppCompatActivity {

    public static boolean PAUSE = false;
    private ConstraintLayout pauseMenuConstraintLayout;
    private Intent intent;

    private final int PRICE_LIFE = 100;
    private final int PRICE_DECELERATION = 200;
    private final int PRICE_GROWTH = 300;

    private Button exitButton;
    private ImageButton pauseImageButton;
    private TextView countMoneyTextView;
    private ImageButton lifeBonusImageButton;
    private ImageButton decelerationBonusImageButton;
    private ImageButton growthBonusImageButton;
    private TextView priceLifeBonusTextView;
    private TextView priceDecelerationBonusTextView;
    private TextView priceGrowthBonusTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        exitButton = findViewById(R.id.exitButton);
        pauseImageButton = findViewById(R.id.pauseImageButton);
        pauseMenuConstraintLayout = findViewById(R.id.pauseMenuConstraintLayout);
        countMoneyTextView = findViewById(R.id.countMoneyTextView);
        lifeBonusImageButton = findViewById(R.id.lifeBonusImageButton);
        decelerationBonusImageButton = findViewById(R.id.decelerationBonusImageButton);
        growthBonusImageButton = findViewById(R.id.growthBonusImageButton);
        priceLifeBonusTextView = findViewById(R.id.priceLifeBonusTextView);
        priceDecelerationBonusTextView = findViewById(R.id.priceDecelerationBonusTextView);
        priceGrowthBonusTextView = findViewById(R.id.priceGrowthBonusTextView);

        priceLifeBonusTextView.setText(PRICE_LIFE + "$");
        priceDecelerationBonusTextView.setText(PRICE_DECELERATION + "$");
        priceGrowthBonusTextView.setText(PRICE_GROWTH + "$");

        exitButton.setOnClickListener(onClickListener);
        pauseImageButton.setOnClickListener(onClickListener);
        lifeBonusImageButton.setOnClickListener(onClickListener);
        decelerationBonusImageButton.setOnClickListener(onClickListener);
        growthBonusImageButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.exitButton:
                    intent = new Intent(Game.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.pauseImageButton:
                    if (PAUSE == false) {
                        PAUSE = true;
                        pauseImageButton.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);
                        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                        pauseMenuConstraintLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        PAUSE = false;
                        pauseImageButton.setImageResource(R.drawable.ic_pause_circle_filled_black_36dp);
                        pauseMenuConstraintLayout.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.lifeBonusImageButton:
                    if (!GameManager.life){
                        if (DatabaseHelper.getMoney() >= PRICE_LIFE){
                            DatabaseHelper.buyBonus(PRICE_LIFE);
                            GameManager.useLifeBonus();
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            DatabaseHelper.showInformation("Bonus activated");
                        }
                        else DatabaseHelper.showInformation("Not enough money");
                    }
                    else DatabaseHelper.showInformation("Bonus is already in use");
                    break;
                case R.id.decelerationBonusImageButton:
                    if (!GameManager.deceleration){
                        if (DatabaseHelper.getMoney() >= PRICE_DECELERATION){
                            DatabaseHelper.buyBonus(PRICE_DECELERATION);
                            GameManager.useDecelerationBonus();
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            DatabaseHelper.showInformation("Bonus activated");
                        }
                        else DatabaseHelper.showInformation("Not enough money");
                    }
                    else DatabaseHelper.showInformation("Bonus is already in use");
                    break;
                case R.id.growthBonusImageButton:
                    if (DatabaseHelper.getMoney() >= PRICE_GROWTH){
                        DatabaseHelper.buyBonus(PRICE_GROWTH);
                        GameManager.useGrowthBonus();
                        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                        DatabaseHelper.showInformation("Bonus activated");
                    }
                    else DatabaseHelper.showInformation("Not enough money");
                    break;
            }
        }
    };
}
