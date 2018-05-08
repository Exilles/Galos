package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dc.galos.R;

public class Achievements extends AppCompatActivity {

    private Intent intent;

    private static final String TITLE = "catname"; // Верхний текст
    private static final String DESCRIPTION = "description"; // ниже главного
    private static final String REWARD = "reward";  // вознаграждение

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        final Button backButton = findViewById(R.id.backButton);

        // получаем экземпляр элемента ListView
        ListView listView = findViewById(R.id.listView);

        // создаем массив списков
        ArrayList<HashMap<String, Object>> catList = new ArrayList<>();

        HashMap<String, Object> hashMap;

        for (int i = 0; i < 20; i ++){
            hashMap = new HashMap<>();
            hashMap.put(TITLE, "Рыжик"); // Название
            hashMap.put(DESCRIPTION, "Рыжий и хитрый"); // Описание
            hashMap.put(REWARD, "Get 100"); // Вознаграждение
            catList.add(hashMap);
        }

        // используем адаптер данных
        SimpleAdapter adapter = new SimpleAdapter(this, catList,
                R.layout.list_item, new String[]{TITLE, DESCRIPTION, REWARD},
                new int[]{R.id.tittleTextView, R.id.descriptionTextView, R.id.rewardButton});

        // Устанавливаем адаптер для списка
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(itemClickListener);
        backButton.setOnClickListener(onClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap =
                    (HashMap<String, Object>) parent.getItemAtPosition(position);
            String titleItem = itemHashMap.get(TITLE).toString();
            String descriptionItem = itemHashMap.get(DESCRIPTION).toString();
            Toast.makeText(getApplicationContext(),
                    "Вы выбрали " + titleItem + ". Он " + descriptionItem, Toast.LENGTH_SHORT)
                    .show();
        }
    };

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
