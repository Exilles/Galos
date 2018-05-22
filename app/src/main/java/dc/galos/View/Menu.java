package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.GameManager;
import dc.galos.Controller.Sound;
import dc.galos.R;

public class Menu extends AppCompatActivity {

    private AudioManager audioManager;
    private ImageButton volumeImageButton;
    private ImageButton blackImageButton;
    private ImageButton logoutImageButton;
    private ImageButton settingsImageButton;
    private ImageButton ratingImageButton;
    private ImageButton achievementsImageButton;
    private ImageButton helpImageButton;
    private ImageButton darkGreyImageButton;
    private ImageButton lightGreyImageButton;
    private ImageButton redImageButton;
    private ImageButton blueImageButton;
    private TextView loginTextView;
    private TextView countMoneyTextView;
    private TextView countRecordTextView;
    private Button registrationButton;
    private Button laterButton;
    private LinearLayout registration;

    private Sound sound = new Sound();
    private Intent intent;
    private Animation upAnimation;
    private Animation downAnimation;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volumeImageButton = findViewById(R.id.volumeImageButton);
        blackImageButton = findViewById(R.id.blackImageButton);
        logoutImageButton = findViewById(R.id.logoutImageButton);
        settingsImageButton = findViewById(R.id.settingsImageButton);
        ratingImageButton = findViewById(R.id.ratingImageButton);
        darkGreyImageButton = findViewById(R.id.darkGreyImageButton);
        lightGreyImageButton = findViewById(R.id.lightGreyImageButton);
        redImageButton = findViewById(R.id.redImageButton);
        blueImageButton = findViewById(R.id.blueImageButton);
        achievementsImageButton = findViewById(R.id.achievementsImageButton);
        helpImageButton = findViewById(R.id.helpImageButton);
        loginTextView = findViewById(R.id.loginTextView);
        countMoneyTextView = findViewById(R.id.countMoneyTextView);
        countRecordTextView = findViewById(R.id.countRecordTextView);
        registrationButton = findViewById(R.id.registrationButton);
        laterButton = findViewById(R.id.laterButton);
        registration = findViewById(R.id.registration);

        loginTextView.setText(DatabaseHelper.getLogin());
        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");
        countRecordTextView.setText(Integer.toString(DatabaseHelper.getRecord()));

        upAnimation = AnimationUtils.loadAnimation(this, R.anim.up);
        downAnimation = AnimationUtils.loadAnimation(this, R.anim.down);

        Animation scalePlayBlackAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_play_black);
        Animation scalePlayDarkGreyAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_play);
        Animation scalePlayLightGreyAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_play);
        Animation scalePlayRedAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_play);
        Animation scalePlayBlueAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_play);

        final long currAnimTime = AnimationUtils.currentAnimationTimeMillis();
        scalePlayDarkGreyAnimation.setStartTime(currAnimTime + 500);
        scalePlayLightGreyAnimation.setStartTime(currAnimTime + 1000);
        scalePlayRedAnimation.setStartTime(currAnimTime + 1500);
        scalePlayBlueAnimation.setStartTime(currAnimTime + 2000);

        blackImageButton.setAnimation(scalePlayBlackAnimation);
        darkGreyImageButton.setAnimation(scalePlayDarkGreyAnimation);
        lightGreyImageButton.setAnimation(scalePlayLightGreyAnimation);
        redImageButton.setAnimation(scalePlayRedAnimation);
        blueImageButton.setAnimation(scalePlayBlueAnimation);

        if (Sound.isVolume()) {
            volumeImageButton.setImageResource(R.drawable.ic_volume_up_white_48dp);
            startService(new Intent(this, Sound.class));
        }
        else {
            volumeImageButton.setImageResource(R.drawable.ic_volume_off_white_48dp);
            stopService(new Intent(this, Sound.class));
        }

        volumeImageButton.setOnClickListener(onClickListener);
        blackImageButton.setOnClickListener(onClickListener);
        darkGreyImageButton.setOnClickListener(onClickListener);
        lightGreyImageButton.setOnClickListener(onClickListener);
        redImageButton.setOnClickListener(onClickListener);
        blueImageButton.setOnClickListener(onClickListener);
        logoutImageButton.setOnClickListener(onClickListener);
        settingsImageButton.setOnClickListener(onClickListener);
        ratingImageButton.setOnClickListener(onClickListener);
        achievementsImageButton.setOnClickListener(onClickListener);
        helpImageButton.setOnClickListener(onClickListener);
        laterButton.setOnClickListener(onClickListener);
        registrationButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.blackImageButton:
                    Game.PAUSE = false;
                    GameManager.setMode(DatabaseHelper.getMode());
                    GameManager.setScore(DatabaseHelper.getScore());
                    GameManager.setAll_rewards(DatabaseHelper.getAll_rewards());
                    intent = new Intent(Menu.this, Game.class);
                    intent.putExtra("mode", 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.darkGreyImageButton:
                    Game.PAUSE = false;
                    GameManager.setMode(1);
                    GameManager.setScore(DatabaseHelper.getScore_1());
                    GameManager.setAll_rewards(DatabaseHelper.getAll_rewards_1());
                    intent = new Intent(Menu.this, Game.class);
                    intent.putExtra("mode", 1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.lightGreyImageButton:
                    Game.PAUSE = false;
                    GameManager.setMode(2);
                    GameManager.setScore(DatabaseHelper.getScore_2());
                    GameManager.setAll_rewards(DatabaseHelper.getAll_rewards_2());
                    intent = new Intent(Menu.this, Game.class);
                    intent.putExtra("mode", 2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.redImageButton:
                    Game.PAUSE = false;
                    GameManager.setMode(3);
                    GameManager.setScore(DatabaseHelper.getScore_3());
                    GameManager.setAll_rewards(DatabaseHelper.getAll_rewards_3());
                    intent = new Intent(Menu.this, Game.class);
                    intent.putExtra("mode", 3);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.blueImageButton:
                    Game.PAUSE = false;
                    GameManager.setMode(4);
                    GameManager.setScore(DatabaseHelper.getScore_4());
                    GameManager.setAll_rewards(DatabaseHelper.getAll_rewards_4());
                    intent = new Intent(Menu.this, Game.class);
                    intent.putExtra("mode", 4);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.volumeImageButton:
                    if (Sound.isVolume())  {
                        Sound.setVolume(false);
                        volumeImageButton.setImageResource(R.drawable.ic_volume_off_white_48dp);
                        stopService(new Intent(getApplicationContext(), Sound.class));
                    }
                    else {
                        Sound.setVolume(true);
                        volumeImageButton.setImageResource(R.drawable.ic_volume_up_white_48dp);
                        startService(new Intent(getApplicationContext(), Sound.class));
                    }
                    break;
                case R.id.logoutImageButton:
                    stopService(new Intent(getApplicationContext(), Sound.class));
                    intent = new Intent(Menu.this, Authorization.class);
                    intent.putExtra("back", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.settingsImageButton:
                    if (DatabaseHelper.getLogin().equals("Гость")) {
                        registration.startAnimation(upAnimation);
                        registration.setVisibility(View.VISIBLE);
                    }
                    else {
                        intent = new Intent(Menu.this, EditAccInf.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    break;
                case R.id.ratingImageButton:
                    if (DatabaseHelper.getLogin().equals("Гость")) {
                        registration.startAnimation(upAnimation);
                        registration.setVisibility(View.VISIBLE);
                    }
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
                case R.id.registrationButton:
                    intent = new Intent(Menu.this, Registration.class);
                    intent.putExtra("registration", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.laterButton:
                    registration.startAnimation(downAnimation);
                    registration.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        intent = new Intent(Menu.this, Authorization.class);
        stopService(new Intent(this, Sound.class));
        intent.putExtra("back", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, Sound.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, Sound.class));
    }
}
