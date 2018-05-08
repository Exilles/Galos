package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class Registration extends AppCompatActivity {

    private Button acceptButton;
    private Button backButton;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;

    private Intent intent;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emailEditText = findViewById(R.id.emailEditText);

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptButton:
                    if (!loginEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("") &&
                            !confirmPasswordEditText.getText().toString().equals("") && !emailEditText.getText().toString().equals("")) {
                        if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {

                            count = DatabaseHelper.searchRow(getApplicationContext(), loginEditText.getText().toString(), null,
                                    null, 2);
                            if (count == 0){
                                count = DatabaseHelper.searchRow(getApplicationContext(), null, null,
                                        emailEditText.getText().toString(), 3);
                                if (count == 0) {
                                    DatabaseHelper.insertRow(getApplicationContext(), loginEditText.getText().toString(),
                                            passwordEditText.getText().toString(), emailEditText.getText().toString());

                                    intent = new Intent(Registration.this, Authorization.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else DatabaseHelper.showInformation(getApplicationContext(),"This e-mail already exists");
                            }
                            else DatabaseHelper.showInformation(getApplicationContext(),"This login already exists");
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
}
