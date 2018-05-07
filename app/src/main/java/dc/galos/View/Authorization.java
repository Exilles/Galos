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

                            // в id находится id пользователя, который авторизируется, или 0, если такой пользователь не будет найден
                            int id = DatabaseHelper.searchRow(getApplicationContext(), loginEditText.getText().toString(),
                                    passwordEditText.getText().toString(), null, 1);

                            if (id != 0) {
                                DatabaseHelper.setSession(id); // устанавливаем id пользователя в сессию
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
