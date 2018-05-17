package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        context = getApplicationContext();

        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        progressTextView = findViewById(R.id.progressTextView);

        if (DatabaseHelper.getLogin().equals("Гость")) {
            DatabaseHelper.getAchievementsGuest();
            achievementsList = DatabaseHelper.getAchievements();
            adapter = new SimpleAdapter(this, achievementsList,
                    R.layout.list_item_achievements, new String[]{TITLE, DESCRIPTION, STATUS, REWARD},
                    new int[]{R.id.titleTextView, R.id.descriptionTextView, R.id.statusTextView, R.id.rewardTextView});


            int progress = DatabaseHelper.getStatus().length() - DatabaseHelper.getStatus().replace("1", "").length();

            progressTextView.setText(getResources().getString(R.string.progress_achievements) + " " + Integer.toString(progress) + "/26");

            listView.setAdapter(adapter);
        }
        else new ParseTask().execute();

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

    private class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get("https://galos.000webhostapp.com/get_achievements_data.php",
                    true, "_id", DatabaseHelper.getId()).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                JSONArray achievements_user = dataJsonObj.getJSONArray("achievements");
                JSONObject achievements = achievements_user.getJSONObject(0);
                DatabaseHelper.getAchievementsGuest(achievements.getString("status"), achievements.getInt("all_levels"),
                        achievements.getInt("all_money"), achievements.getInt("all_eating"), achievements.getInt("all_wins"));

                achievementsList = DatabaseHelper.getAchievements();
                adapter = new SimpleAdapter(context, achievementsList,
                        R.layout.list_item_achievements, new String[]{TITLE, DESCRIPTION, STATUS, REWARD},
                        new int[]{R.id.titleTextView, R.id.descriptionTextView, R.id.statusTextView, R.id.rewardTextView});


                int progress = DatabaseHelper.getStatus().length() - DatabaseHelper.getStatus().replace("1", "").length();

                progressTextView.setText(getResources().getString(R.string.progress_achievements) + " " + Integer.toString(progress) + "/26");

                listView.setAdapter(adapter);
            } catch (JSONException e) {
                Log.d("my log", "Не вышло получить данные :(");
                e.printStackTrace();
            }
        }
    }
}
