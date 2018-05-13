package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.Sound;
import dc.galos.R;

public class Authorization extends AppCompatActivity {

    private Button loginAsGuestButton;
    private Button registrationButton;
    private Button restorePasswordButton;
    private Button loginButton;
    private EditText loginEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;

    private Sound sound = new Sound();
    private Intent intent;
    private boolean remember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        DatabaseHelper.setMyContext(getApplicationContext());

        loginAsGuestButton = findViewById(R.id.loginAsGuestButton);
        registrationButton = findViewById(R.id.registrationButton);
        restorePasswordButton = findViewById(R.id.restorePasswordButton);
        loginButton = findViewById(R.id.loginButton);
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);

        sound.initialization(this, R.raw.background_music);

        loginAsGuestButton.setOnClickListener(onClickListener);
        registrationButton.setOnClickListener(onClickListener);
        restorePasswordButton.setOnClickListener(onClickListener);
        loginButton.setOnClickListener(onClickListener);
        rememberCheckBox.setOnClickListener(onClickListener);

        if (DatabaseHelper.searchRememberUser()) {
            intent = new Intent(Authorization.this, Menu.class);
            startActivity(intent);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginAsGuestButton:
                    DatabaseHelper.searchRowUsers("Гость", "1111", null, 1);
                    DatabaseHelper.rememberOrForgetUser(true);
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
                        // в id вернется 1, если пользователь найден или 0, если такой пользователь не найден
                        int id = DatabaseHelper.searchRowUsers(loginEditText.getText().toString(),
                                passwordEditText.getText().toString(), null, 1);
                        if (id == 1) {
                            DatabaseHelper.rememberOrForgetUser(remember);
                            intent = new Intent(Authorization.this, Menu.class);
                            startActivity(intent);
                        }
                        else DatabaseHelper.showInformation(getResources().getString(R.string.incorrect_login_or_password));
                    }
                    else  DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));
                    break;
                case R.id.rememberCheckBox:
                    remember = rememberCheckBox.isChecked();
                    break;
            }
        }
    };
}
