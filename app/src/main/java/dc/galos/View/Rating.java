package dc.galos.View;

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

public class Rating extends AppCompatActivity {

    private Intent intent;

    private String NUMBER = "number"; // Номер
    private String NAME = "name"; // Имя
    private String RECORD = "record"; // Рекорд

    private ArrayList<HashMap<String, Object>> recordsList;
    private SimpleAdapter adapter;

    private Button backButton;
    private ListView listView;
    private TextView goldUserTextView;
    private TextView silverUserTextView;
    private TextView bronzeUserTextView;
    private TextView positionTextView;

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

        recordsList = DatabaseHelper.getRecords();

        HashMap<String, Object> hashMap = recordsList.get(0);
        goldUserTextView.setText((String)hashMap.get(NAME));
        hashMap = recordsList.get(1);
        silverUserTextView.setText((String)hashMap.get(NAME));
        hashMap = recordsList.get(2);
        bronzeUserTextView.setText((String)hashMap.get(NAME));
        positionTextView.setText("Ваше место: 1");

        adapter = new SimpleAdapter(this, recordsList,
                R.layout.list_item_records, new String[]{NUMBER, NAME, RECORD},
                new int[]{R.id.numberTextView, R.id.nameTextView, R.id.recordTextView});

        listView.setAdapter(adapter);
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
}
