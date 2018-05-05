package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dc.galos.R;

public class Authorization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        Button loginAsGuestButton = findViewById(R.id.loginAsGuestButton);
        Button registrationButton = findViewById(R.id.registrationButton);
        Button restorePasswordButton = findViewById(R.id.restorePasswordButton);
        Button loginButton = findViewById(R.id.loginButton);

        loginAsGuestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authorization.this, Menu.class);
                startActivity(intent);
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authorization.this, Registration.class);
                startActivity(intent);
            }
        });

        restorePasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authorization.this, RestorePassword.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authorization.this, Menu.class);
                startActivity(intent);
            }
        });

    }
}
