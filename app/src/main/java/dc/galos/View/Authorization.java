package dc.galos.View;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dc.galos.Controller.CanvasView;
import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.Sound;
import dc.galos.R;

public class Authorization extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        final Button loginAsGuestButton = findViewById(R.id.loginAsGuestButton);
        final Button registrationButton = findViewById(R.id.registrationButton);
        final Button restorePasswordButton = findViewById(R.id.restorePasswordButton);
        final Button loginButton = findViewById(R.id.loginButton);

        final EditText loginEditText = findViewById(R.id.loginEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);

        final Sound sound = new Sound();

        sound.initialization(this, R.raw.background_music);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.loginAsGuestButton:
                        intent = new Intent(Authorization.this, Menu.class);
                        startActivity(intent);
                        break;
                    case R.id.registrationButton:
                        intent = new Intent(Authorization.this, Registration.class);
                        startActivity(intent);
                        break;
                    case R.id.restorePasswordButton:
                        intent = new Intent(Authorization.this, RestorePassword.class);
                        startActivity(intent);
                        break;
                    case R.id.loginButton:
                        if (!loginEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")){
                            databaseHelper = new DatabaseHelper(getApplicationContext());
                            // создаем базу данных
                            databaseHelper.create_db();

                            // открываем подключение
                            db = databaseHelper.open();

                            String query = String.format("SELECT \"%s\" from \"%s\" WHERE \"%s\" = \"%s\" AND \"%s\" = \"%s\"", DatabaseHelper.COLUMN_ID, DatabaseHelper.TABLE, DatabaseHelper.COLUMN_LOGIN, loginEditText.getText().toString(), DatabaseHelper.COLUMN_PASSWORD, passwordEditText.getText().toString());

                            cursor = db.rawQuery(query, null);

                            if (cursor.getCount() > 0) {
                                intent = new Intent(Authorization.this, Menu.class);
                                startActivity(intent);
                            }
                            else DatabaseHelper.showInformation(getApplicationContext(), "Incorrect login or password");
                        }
                        else  DatabaseHelper.showInformation(getApplicationContext(),"Wrong data");
                        break;
                }
            }
        };

        loginAsGuestButton.setOnClickListener(onClickListener);
        registrationButton.setOnClickListener(onClickListener);
        restorePasswordButton.setOnClickListener(onClickListener);
        loginButton.setOnClickListener(onClickListener);

    }
}
