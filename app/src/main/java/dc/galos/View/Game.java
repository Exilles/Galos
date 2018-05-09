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
import dc.galos.R;

public class Game extends AppCompatActivity {

    public static boolean PAUSE = false;
    private ConstraintLayout pauseMenuConstraintLayout;
    private Intent intent;

    private Button exitButton;
    private ImageButton pauseImageButton;
    private TextView countMoneyTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        exitButton = findViewById(R.id.exitButton);
        pauseImageButton = findViewById(R.id.pauseImageButton);
        pauseMenuConstraintLayout = findViewById(R.id.pauseMenuConstraintLayout);
        countMoneyTextView = findViewById(R.id.countMoneyTextView);

        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()) + "$");

        exitButton.setOnClickListener(onClickListener);
        pauseImageButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
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
                        pauseMenuConstraintLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        PAUSE = false;
                        pauseImageButton.setImageResource(R.drawable.ic_pause_circle_filled_black_36dp);
                        pauseMenuConstraintLayout.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };
}
