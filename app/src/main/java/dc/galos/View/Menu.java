package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.GameManager;
import dc.galos.Controller.Sound;
import dc.galos.R;

public class Menu extends AppCompatActivity {

    private AudioManager audioManager;
    private ImageButton volumeImageButton;
    private ImageButton playImageButton;
    private ImageButton logoutImageButton;
    private ImageButton settingsImageButton;
    private ImageButton ratingImageButton;
    private ImageButton achievementsImageButton;
    private ImageButton helpImageButton;
    private TextView loginTextView;
    private TextView countMoneyTextView;
    private TextView countRecordTextView;

    private Sound sound = new Sound();
    private Intent intent;

    private static int money;
    private static int record;
    private static String status;
    private static int all_levels;
    private static int all_money;
    private static int all_eating;
    private static int all_wins;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //getData();
        //DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating, all_wins);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volumeImageButton = findViewById(R.id.volumeImageButton);
        playImageButton = findViewById(R.id.playImageButton);
        logoutImageButton = findViewById(R.id.logoutImageButton);
        settingsImageButton = findViewById(R.id.settingsImageButton);
        ratingImageButton = findViewById(R.id.ratingImageButton);
        achievementsImageButton = findViewById(R.id.achievementsImageButton);
        helpImageButton = findViewById(R.id.helpImageButton);
        loginTextView = findViewById(R.id.loginTextView);
        countMoneyTextView = findViewById(R.id.countMoneyTextView);
        countRecordTextView = findViewById(R.id.countRecordTextView);

        loginTextView.setText(DatabaseHelper.getLogin());
        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
        countRecordTextView.setText(Integer.toString(DatabaseHelper.getRecord()));

        sound = new Sound();
        sound.mediaStart();

        volumeImageButton.setOnClickListener(onClickListener);
        playImageButton.setOnClickListener(onClickListener);
        logoutImageButton.setOnClickListener(onClickListener);
        settingsImageButton.setOnClickListener(onClickListener);
        ratingImageButton.setOnClickListener(onClickListener);
        achievementsImageButton.setOnClickListener(onClickListener);
        helpImageButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playImageButton:
                    Game.PAUSE = false;
                    GameManager.setMode(DatabaseHelper.getMode());
                    GameManager.setScore(DatabaseHelper.getScore());
                    GameManager.setAll_rewards(DatabaseHelper.getAll_rewards());
                    intent = new Intent(Menu.this, Game.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.volumeImageButton:
                    if (Sound.isVolume())  {
                        Sound.setVolume(false);
                        volumeImageButton.setImageResource(R.drawable.ic_volume_off_white_48dp);
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    }
                    else {
                        Sound.setVolume(true);
                        volumeImageButton.setImageResource(R.drawable.ic_volume_up_white_48dp);
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    }
                    break;
                case R.id.logoutImageButton:
                    DatabaseHelper.rememberOrForgetUser("false");
                    sound.mediaStop();
                    intent = new Intent(Menu.this, Authorization.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.settingsImageButton:
                    if (DatabaseHelper.getLogin().equals("Гость")) DatabaseHelper.showInformation("Недоступно неавторизированному пользователю");
                    else {
                        intent = new Intent(Menu.this, EditAccInf.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    break;
                case R.id.ratingImageButton:
                    if (DatabaseHelper.getLogin().equals("Гость")) DatabaseHelper.showInformation("Недоступно неавторизированному пользователю");
                    else {
                        intent = new Intent(Menu.this, Rating.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    break;
                case R.id.achievementsImageButton:
                    intent = new Intent(Menu.this, Achievements.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.helpImageButton:
                    intent = new Intent(Menu.this, Help.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };

    private static void getData(){
        money = DatabaseHelper.getMoney();
        record = DatabaseHelper.getRecord();
        status = DatabaseHelper.getStatus();
        all_levels = DatabaseHelper.getAll_levels();
        all_money = DatabaseHelper.getAll_money();
        all_eating = DatabaseHelper.getAll_eating();
        all_wins = DatabaseHelper.getAll_wins();
    }
}
