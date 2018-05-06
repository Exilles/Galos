package dc.galos.View;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import dc.galos.Controller.CanvasView;
import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.Sound;
import dc.galos.R;

public class Registration extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button acceptButton = findViewById(R.id.acceptButton);
        final Button backButton = findViewById(R.id.backButton);

        final EditText loginEditText = findViewById(R.id.loginEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        final EditText emailEditText = findViewById(R.id.emailEditText);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.acceptButton:
                        if (!loginEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("") &&
                                !confirmPasswordEditText.getText().toString().equals("") && !emailEditText.getText().toString().equals("")) {
                            if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                                databaseHelper = new DatabaseHelper(getApplicationContext());
                                // создаем базу данных
                                databaseHelper.create_db();

                                // Gets the database in write mode
                                db = databaseHelper.open();
                                // Создаем объект ContentValues, где имена столбцов ключи,
                                // а информация о госте является значениями ключей
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(DatabaseHelper.COLUMN_LOGIN, "'" + loginEditText.getText().toString() + "'");
                                contentValues.put(DatabaseHelper.COLUMN_PASSWORD, "'" + passwordEditText.getText().toString() + "'");
                                contentValues.put(DatabaseHelper.COLUMN_EMAIL, "'" + emailEditText.getText().toString() + "'");
                                contentValues.put(DatabaseHelper.COLUMN_MONEY, 0);
                                contentValues.put(DatabaseHelper.COLUMN_RECORD, 0);

                                db.insert(DatabaseHelper.TABLE, null, contentValues);

                                intent = new Intent(Registration.this, Menu.class);
                                startActivity(intent);
                            }
                            else  DatabaseHelper.showInformation(getApplicationContext(),"Passwords do not match");
                        }
                        else  DatabaseHelper.showInformation(getApplicationContext(),"Wrong data");
                        break;
                    case R.id.backButton:
                        intent = new Intent(Registration.this, Authorization.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
            }
        };

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }
}
