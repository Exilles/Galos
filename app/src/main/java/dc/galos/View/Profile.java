package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class Profile extends AppCompatActivity {

    private Intent intent;
    private int id = DatabaseHelper.getSession();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Button editAccInfButton = findViewById(R.id.editAccInfButton);
        final Button backButton = findViewById(R.id.backButton);
        final ImageButton editIconImageButton = findViewById(R.id.editIconImageButton);
        final TextView loginTextView = findViewById(R.id.loginTextView);
        final TextView countRecordTextView = findViewById(R.id.countRecordTextView);
        final TextView countMoneyTextView = findViewById(R.id.countMoneyTextView);

        loginTextView.setText(DatabaseHelper.getLogin());
        countMoneyTextView.setText(Integer.toString(DatabaseHelper.getMoney()));
        countRecordTextView.setText(Integer.toString(DatabaseHelper.getRecord()));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.backButton:
                        intent = new Intent(Profile.this, Menu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.editAccInfButton:
                        intent = new Intent(Profile.this, EditAccInf.class);
                        startActivity(intent);
                        break;
                    case R.id.editIconImageButton:
                        intent = new Intent(Profile.this, EditIcon.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        editAccInfButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
        editIconImageButton.setOnClickListener(onClickListener);
    }
}
