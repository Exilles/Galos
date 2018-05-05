package dc.galos.View;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import dc.galos.R;

public class Game extends AppCompatActivity {

    public static boolean PAUSE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final Button exitButton = findViewById(R.id.exitButton);
        final ImageButton pauseImageButton = findViewById(R.id.pauseImageButton);
        final ConstraintLayout pauseMenuConstraintLayout = findViewById(R.id.pauseMenuConstraintLayout);

        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game.this, Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        pauseImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (PAUSE == false) {
                    PAUSE = true;
                    pauseImageButton.setImageResource(R.drawable.ic_play_circle_filled_black_36dp);
                    pauseMenuConstraintLayout.setVisibility(View.VISIBLE);
                }
                else {
                    PAUSE = false;
                    pauseImageButton.setImageResource(R.drawable.ic_pause_circle_filled_black_36dp);
                    pauseMenuConstraintLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
