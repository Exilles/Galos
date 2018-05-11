package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
    private ArrayList<HashMap<String, Object>> achievementsList;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        achievementsList = DatabaseHelper.getAchievements();

        adapter = new SimpleAdapter(this, achievementsList,
                R.layout.list_item, new String[]{TITLE, DESCRIPTION, STATUS, REWARD},
                new int[]{R.id.titleTextView, R.id.descriptionTextView, R.id.statusTextView, R.id.rewardTextView});

        listView.setAdapter(adapter);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButton:
                    intent = new Intent(Achievements.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }
        }
    };
}
