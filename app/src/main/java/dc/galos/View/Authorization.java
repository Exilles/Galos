package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dc.galos.Controller.Sound;
import dc.galos.R;

public class Authorization extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        final Button loginAsGuestButton = findViewById(R.id.loginAsGuestButton);
        final Button registrationButton = findViewById(R.id.registrationButton);
        final Button restorePasswordButton = findViewById(R.id.restorePasswordButton);
        final Button loginButton = findViewById(R.id.loginButton);
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
                        intent = new Intent(Authorization.this, Menu.class);
                        startActivity(intent);
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
