package dc.galos.View;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import dc.galos.Controller.Sound;
import dc.galos.R;

public class Menu extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        final ImageButton volumeImageButton = findViewById(R.id.volumeImageButton);
        final ImageButton playImageButton = findViewById(R.id.playImageButton);
        final ImageButton exitImageButton = findViewById(R.id.exitImageButton);
        final ImageButton profileImageButton = findViewById(R.id.profileImageButton);
        final ImageButton ratingImageButton = findViewById(R.id.ratingImageButton);
        final ImageButton achievementsImageButton = findViewById(R.id.achievementsImageButton);
        final ImageButton helpImageButton = findViewById(R.id.helpImageButton);
        final Sound sound = new Sound();

        sound.mediaPlay(this, R.raw.background_music);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.playImageButton:
                        intent = new Intent(Menu.this, Game.class);
                        startActivity(intent);
                        break;
                    case R.id.volumeImageButton:
                        if (Sound.isVolume())  {
                            Sound.setVolume(false);
                            volumeImageButton.setImageResource(R.drawable.ic_volume_off_white_36dp);
                            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        }
                        else {
                            Sound.setVolume(true);
                            volumeImageButton.setImageResource(R.drawable.ic_volume_up_white_36dp);
                            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        }
                        break;
                    case R.id.exitImageButton:
                        Intent intent = new Intent(Menu.this, Authorization.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.profileImageButton:
                        intent = new Intent(Menu.this, Profile.class);
                        startActivity(intent);
                        break;
                    case R.id.ratingImageButton:
                        intent = new Intent(Menu.this, Rating.class);
                        startActivity(intent);
                        break;
                    case R.id.achievementsImageButton:
                        intent = new Intent(Menu.this, Achievements.class);
                        startActivity(intent);
                        break;
                    case R.id.helpImageButton:
                        intent = new Intent(Menu.this, Help.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        volumeImageButton.setOnClickListener(onClickListener);
        playImageButton.setOnClickListener(onClickListener);
        exitImageButton.setOnClickListener(onClickListener);
        profileImageButton.setOnClickListener(onClickListener);
        ratingImageButton.setOnClickListener(onClickListener);
        achievementsImageButton.setOnClickListener(onClickListener);
        helpImageButton.setOnClickListener(onClickListener);
    }
}
