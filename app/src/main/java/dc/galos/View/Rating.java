package dc.galos.View;

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
import java.util.Arrays;
import java.util.HashMap;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.JSONParser;
import dc.galos.R;

public class Rating extends AppCompatActivity {

    private Intent intent;

    private String NUMBER = "number"; // Номер
    private String NAME = "name"; // Имя
    private String RECORD = "record"; // Рекорд

    private ArrayList<HashMap<String, Object>> recordsList;
    private static SimpleAdapter adapter;
    private static Context mycontext;

    private Button backButton;
    private static ListView listView;
    private static TextView goldUserTextView;
    private static TextView silverUserTextView;
    private static TextView bronzeUserTextView;
    private static TextView positionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        goldUserTextView = findViewById(R.id.goldUserTextView);
        silverUserTextView = findViewById(R.id.silverUserTextView);
        bronzeUserTextView = findViewById(R.id.bronzeUserTextView);
        positionTextView = findViewById(R.id.positionTextView);

        mycontext = getApplicationContext();

        new ParseTask().execute();

        backButton.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButton:
                    intent = new Intent(Rating.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };

    private static class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            return HttpRequest.get("https://galos.000webhostapp.com/get_records.php").body();

        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                JSONArray users = dataJsonObj.getJSONArray("records");

                String[] names = new String[users.length()];
                String[] records = new String[users.length()];

                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);

                    names[i] = user.getString("login");
                    records[i] = user.getString("record");
                }

                ArrayList<HashMap<String, Object>> recordsList = new ArrayList<>();
                HashMap<String, Object> hashMap;

                String NUMBER = "number";
                String NAME = "name";
                String RECORD = "record";

                for(int i = records.length - 1 ; i > 0 ; i--){
                    for(int j = 0 ; j < i ; j++){
                        if( Integer.parseInt(records[j]) < Integer.parseInt(records[j+1]) ){
                            String tmp = records[j];
                            records[j] = records[j+1];
                            records[j+1] = tmp;
                            tmp = names[j];
                            names[j] = names[j+1];
                            names[j+1] = tmp;
                        }
                    }
                }

                String user_record = "Не найдено";

                for (int i = 0; i < names.length; i++) {
                    hashMap = new HashMap<>();
                    hashMap.put(NUMBER, i + 1);
                    hashMap.put(NAME, names[i]);
                    hashMap.put(RECORD, records[i]);
                    recordsList.add(hashMap);
                    if (names[i].equals(DatabaseHelper.getLogin()))  user_record = Integer.toString(i + 1);
                }

                HashMap<String, Object> hashMap2  = recordsList.get(0);
                goldUserTextView.setText((String)hashMap2.get(NAME));

                if (recordsList.size() > 1) {
                    hashMap2 = recordsList.get(1);
                    silverUserTextView.setText((String)hashMap2.get(NAME));
                }
                if (recordsList.size() > 2) {
                    hashMap2 = recordsList.get(2);
                    bronzeUserTextView.setText((String)hashMap2.get(NAME));
                }

                positionTextView.setText("Ваше место: " + user_record);

                adapter = new SimpleAdapter(mycontext, recordsList,
                        R.layout.list_item_records, new String[]{NUMBER, NAME, RECORD},
                        new int[]{R.id.numberTextView, R.id.nameTextView, R.id.recordTextView});

                listView.setAdapter(adapter);
            } catch (JSONException e) {
                Log.d("my log", "Не вышло получить данные :(");
                e.printStackTrace();
            }
        }
    }
}
