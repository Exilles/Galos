package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.GameManager;
import dc.galos.R;

public class Game extends AppCompatActivity {

    public static boolean PAUSE = false;
    private Intent intent;
    private Timer timer;
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

    private static int money;
    private static int record;
    private static String status;
    private static int all_levels;
    private static int all_money;
    private static int all_eating;
    private static int all_wins;

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
                    getData();
                    DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating, all_wins);
                    timer = GameManager.getTimer();
                    timer.cancel();
                    timer.purge();
                    GameManager.setTimer(timer);
                    GameManager.setLife(false);
                    GameManager.setDeceleration(false);
                    intent = new Intent(Game.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.pauseImageButton:
                    getData();
                    DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating, all_wins);
                    if (!dialog) {
                        if (!PAUSE) {
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
                    if (!GameManager.isLife()){
                        if (DatabaseHelper.getMoney() >= PRICE_LIFE){
                            lifeBonusImageButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_states_bonus));
                            getData();
                            DatabaseHelper.updateData(money - PRICE_LIFE, record, all_levels, all_money, all_eating, all_wins);
                            //DatabaseHelper.buyBonus(PRICE_LIFE);
                            GameManager.useLifeBonus();
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            DatabaseHelper.showInformation(getResources().getString(R.string.bonus_used), 130);
                        }
                        else DatabaseHelper.showInformation(getResources().getString(R.string.not_enough_money), 130);
                    }
                    else DatabaseHelper.showInformation(getResources().getString(R.string.bonus_alredy_used), 130);
                    break;
                case R.id.decelerationBonusImageButton:
                    if (!GameManager.isDeceleration()){
                        if (DatabaseHelper.getMoney() >= PRICE_DECELERATION){
                            decelerationBonusImageButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_states_bonus));
                            getData();
                            DatabaseHelper.updateData(money - PRICE_DECELERATION, record, all_levels, all_money, all_eating, all_wins);
                            //DatabaseHelper.buyBonus(PRICE_DECELERATION);
                            GameManager.useDecelerationBonus();
                            countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                            DatabaseHelper.showInformation(getResources().getString(R.string.bonus_used), 385);
                        }
                        else DatabaseHelper.showInformation(getResources().getString(R.string.not_enough_money), 385);
                    }
                    else DatabaseHelper.showInformation(getResources().getString(R.string.bonus_alredy_used), 385);
                    break;
                case R.id.growthBonusImageButton:
                    if (DatabaseHelper.getMoney() >= PRICE_GROWTH){
                        growthBonusImageButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_states_bonus));
                        getData();
                        DatabaseHelper.updateData(money - PRICE_GROWTH, record, all_levels, all_money, all_eating, all_wins);
                        //DatabaseHelper.buyBonus(PRICE_GROWTH);
                        GameManager.useGrowthBonus();
                        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
                        DatabaseHelper.showInformation(getResources().getString(R.string.bonus_used), 635);
                    }
                    else DatabaseHelper.showInformation(getResources().getString(R.string.not_enough_money), 635);
                    break;
                case R.id.continueButton:
                    getData();
                    DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating, all_wins);
                    dialog = false;
                    PAUSE = false;
                    lifeBonusImageButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_states_grey));
                    decelerationBonusImageButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_states_grey));
                    growthBonusImageButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_states_grey));
                    dialogConstraintLayout.setVisibility(View.INVISIBLE);
                    GameManager.gameEnd();
                    break;
                case R.id.menuButton:
                    getData();
                    DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating, all_wins);
                    dialog = false;
                    PAUSE = false;
                    timer = GameManager.getTimer();
                    timer.cancel();
                    timer.purge();
                    GameManager.setTimer(timer);
                    intent = new Intent(Game.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        getData();
        DatabaseHelper.updateData(money, record, all_levels + 1, all_money, all_eating, all_wins);
        //DatabaseHelper.updateAllLevels();
        dialogConstraintLayout.setVisibility(View.VISIBLE);
        countScoreTextView.setText(Integer.toString(_score));
        switch (_flag) {
            case 1: // победа
                if (GameManager.getMode() != 4) GameManager.setMode(GameManager.getMode() + 1);
                else GameManager.setMode(1);
                continueButton.setText("Следующий уровень");
                titleTextView.setText("Победа");
                rewardTextView.setText("Награда:");
                countRewardTextView.setText(Integer.toString(_reward) + "$");
                DatabaseHelper.updateResume(GameManager.getMode(), GameManager.getScore(), GameManager.getAll_rewards());
                break;
            case 2: // поражение
                GameManager.setMode(1);
                continueButton.setText("Начать новую игру");
                titleTextView.setText("Поражение");
                rewardTextView.setText("Все награды:");
                countRewardTextView.setText(Integer.toString(_sum) + "$");
                DatabaseHelper.updateResume(GameManager.getMode(), GameManager.getScore(), GameManager.getAll_rewards());
                break;
            case 3: // переигровка
                continueButton.setText("Переиграть уровень");
                titleTextView.setText("Ни победа, ни поражение");
                rewardTextView.setText("Награда:");
                countRewardTextView.setText("0$");
                DatabaseHelper.updateResume(GameManager.getMode(), GameManager.getScore(), GameManager.getAll_rewards());
                break;
        }
    }

    private static void getData(){
        money = DatabaseHelper.getMoney();
        record = DatabaseHelper.getRecord();
        status = DatabaseHelper.getStatus();
        all_levels = DatabaseHelper.getAll_levels();
        all_money = DatabaseHelper.getAll_money();
        all_eating = DatabaseHelper.getAll_eating();
        all_wins = DatabaseHelper.getAll_wins();
    }

    @Override
    public void onBackPressed() {
        getData();
        DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating, all_wins);
        dialog = false;
        PAUSE = false;
        timer = GameManager.getTimer();
        timer.cancel();
        timer.purge();
        GameManager.setTimer(timer);
        intent = new Intent(Game.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
