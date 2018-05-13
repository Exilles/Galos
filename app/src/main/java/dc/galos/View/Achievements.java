package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class Achievements extends AppCompatActivity {

    private Intent intent;

    private String TITLE = "achievement"; // Название достижения
    private String DESCRIPTION = "description"; // Описание достижения
    private String STATUS = "status"; // Статус достижения
    private String REWARD = "reward";  // Вознаграждение за достижение

    private Button backButton;
    private ListView listView;
    private TextView progressTextView;

    private ArrayList<HashMap<String, Object>> achievementsList;
    private SimpleAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        progressTextView = findViewById(R.id.progressTextView);

        achievementsList = DatabaseHelper.getAchievements();

        adapter = new SimpleAdapter(this, achievementsList,
                R.layout.list_item_achievements, new String[]{TITLE, DESCRIPTION, STATUS, REWARD},
                new int[]{R.id.titleTextView, R.id.descriptionTextView, R.id.statusTextView, R.id.rewardTextView});


        int progress = DatabaseHelper.getStatus().length() - DatabaseHelper.getStatus().replace("1", "").length();

        progressTextView.setText(getResources().getString(R.string.progress_achievements) + " " + Integer.toString(progress) + "/26");

        listView.setAdapter(adapter);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButton:
                    intent = new Intent(Achievements.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };
}
