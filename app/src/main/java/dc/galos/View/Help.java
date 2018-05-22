package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dc.galos.R;

public class Help extends AppCompatActivity {

    private Intent intent;
    private Button backButton;

    //private String[] titlesArray = new String[] { "ОБ ИГРЕ", "ЦЕЛЬ ИГРЫ", "УПРАВЛЕНИЕ", "РЕКОРД", "СМЕШАННЫЙ", "ОЧЕРЕДЬ", "ДОГОНЯЛКИ", "НЕВИДИМКИ", "БОНУСЫ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String[] titlesArray = getResources().getStringArray(R.array.titles_help);
        String[] descriptionArray = getResources().getStringArray(R.array.descriptions_help);
        Map<String, String> map; // коллекция для групп
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>(); // заполняем коллекцию групп из массива с названиями групп

        for (String group : titlesArray) {
            // заполняем список атрибутов для каждой группы
            map = new HashMap<>();
            map.put("title", group); // заголовок
            groupDataList.add(map);
        }

        String groupFrom[] = new String[] { "title" }; // список атрибутов групп для чтения
        int groupTo[] = new int[] { android.R.id.text1 }; // список ID view-элементов, в которые будет помещены атрибуты групп
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>(); // создаем общую коллекцию для коллекций элементов

        for (int i = 0; i < 9; i++) {
            map = new HashMap<>();
            map.put("description", descriptionArray[i]); // описание
            ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
            сhildDataItemList.add(map);
            сhildDataList.add(сhildDataItemList);
        }

        String childFrom[] = new String[] { "description" }; // список атрибутов элементов для чтения
        int childTo[] = new int[] { android.R.id.text1 }; // список ID view-элементов, в которые будет помещены атрибуты элементов

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                R.layout.exp_list_title_help, groupFrom,
                groupTo, сhildDataList, R.layout.exp_list_item_help,
                childFrom, childTo);

        ExpandableListView expandableListView = findViewById(R.id.expListView);
        expandableListView.setAdapter(adapter);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButton:
                    intent = new Intent(Help.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        intent = new Intent(Help.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
