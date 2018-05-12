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
    private Intent intent;
    private static boolean dialog = false;

    private final int PRICE_LIFE = 100;
    private final int PRICE_DECELERATION = 200;
    private final int PRICE_GROWTH = 300;

    private static ConstraintLayout dialogConstraintLayout;
    private static Button continueButton;
    private Button menuButton;
    private static TextView countScoreTextView;
    private static TextView titleTextView;
    private static TextView countRewardTextView;
    private static TextView rewardTextView;
    private ConstraintLayout pauseMenuConstraintLayout;
    private Button exitButton;
    private ImageButton pauseImageButton;
    private TextView countMoneyTextView;
    private ImageButton lifeBonusImageButton;
    private ImageButton decelerationBonusImageButton;
    private ImageButton growthBonusImageButton;
    private TextView priceLifeBonusTextView;
    private TextView priceDecelerationBonusTextView;
    private TextView priceGrowthBonusTextView;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dialogConstraintLayout = findViewById(R.id.dialogConstraintLayout);
        titleTextView = findViewById(R.id.titleTextView);
        continueButton = findViewById(R.id.continueButton);
        menuButton = findViewById(R.id.menuButton);
        countScoreTextView = findViewById(R.id.countScoreTextView);
        countRewardTextView = findViewById(R.id.countRewardTextView);
        rewardTextView = findViewById(R.id.rewardTextView);
        pauseMenuConstraintLayout = findViewById(R.id.pauseMenuConstraintLayout);
        exitButton = findViewById(R.id.exitButton);
        pauseImageButton = findViewById(R.id.pauseImageButton);
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
        continueButton.setOnClickListener(onClickListener);
        menuButton.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint({"SetTextI18n"})
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.exitButton:
                    intent = new Intent(Game.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.pauseImageButton:
                    if (!dialog) {
                        if (PAUSE == false) {
                            PAUSE = true;
                            pauseImageButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            pauseMenuConstraintLayout.setVisibility(View.VISIBLE);
                        }
                        else {
                            PAUSE = false;
                            pauseImageButton.setImageResource(R.drawable.ic_pause_white_24dp);
                            pauseMenuConstraintLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                case R.id.lifeBonusImageButton:
                    if (!GameManager.life){
                        if (DatabaseHelper.getMoney() >= PRICE_LIFE){
                            DatabaseHelper.buyBonus(PRICE_LIFE);
                            GameManager.useLifeBonus();
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            DatabaseHelper.showInformation(getResources().getString(R.string.bonus_used));
                        }
                        else DatabaseHelper.showInformation(getResources().getString(R.string.not_enough_money));
                    }
                    else DatabaseHelper.showInformation(getResources().getString(R.string.bonus_alredy_used));
                    break;
                case R.id.decelerationBonusImageButton:
                    if (!GameManager.deceleration){
                        if (DatabaseHelper.getMoney() >= PRICE_DECELERATION){
                            DatabaseHelper.buyBonus(PRICE_DECELERATION);
                            GameManager.useDecelerationBonus();
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            DatabaseHelper.showInformation(getResources().getString(R.string.bonus_used));
                        }
                        else DatabaseHelper.showInformation(getResources().getString(R.string.not_enough_money));
                    }
                    else DatabaseHelper.showInformation(getResources().getString(R.string.bonus_alredy_used));
                    break;
                case R.id.growthBonusImageButton:
                    if (DatabaseHelper.getMoney() >= PRICE_GROWTH){
                        DatabaseHelper.buyBonus(PRICE_GROWTH);
                        GameManager.useGrowthBonus();
                        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                        DatabaseHelper.showInformation(getResources().getString(R.string.bonus_used));
                    }
                    else DatabaseHelper.showInformation(getResources().getString(R.string.not_enough_money));
                    break;
                case R.id.continueButton:
                    dialog = false;
                    PAUSE = false;
                    dialogConstraintLayout.setVisibility(View.INVISIBLE);
                    GameManager.gameEnd();
                    break;
                case R.id.menuButton:
                    dialog = false;
                    PAUSE = false;
                    intent = new Intent(Game.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    public static void showDialog(int _score, int _reward, int _sum, int _flag) {
        dialog = true;
        PAUSE = true;
        DatabaseHelper.updateAllLevels();
        dialogConstraintLayout.setVisibility(View.VISIBLE);
        countScoreTextView.setText(Integer.toString(_score));
        switch (_flag) {
            case 1:
                if (GameManager.mode != 4) GameManager.mode ++;
                else GameManager.mode = 1;
                continueButton.setText("Следующий уровень");
                titleTextView.setText("Победа");
                rewardTextView.setText("Награда:");
                countRewardTextView.setText(Integer.toString(_reward) + "$");
                break;
            case 2:
                GameManager.mode = 1;
                continueButton.setText("Начать новую игру");
                titleTextView.setText("Поражение");
                rewardTextView.setText("Все награды:");
                countRewardTextView.setText(Integer.toString(_sum) + "$");
                break;
            case 3:
                continueButton.setText("Переиграть уровень");
                titleTextView.setText("Ни победа, ни поражение");
                rewardTextView.setText("Награда:");
                countRewardTextView.setText("0$");
                break;
        }
    }
}
