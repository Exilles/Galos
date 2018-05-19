package dc.galos.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class Registration extends AppCompatActivity {

    private Button acceptButton;
    private Button backButton;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;
    private LinearLayout progress;

    private Intent intent;

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
        progress = findViewById(R.id.progress);

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
                            new ParseTask().execute();
                        }
                        else  DatabaseHelper.showInformation(getResources().getString(R.string.match_passwords));
                    }
                    else  DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));
                    break;
                case R.id.backButton:
                    intent = new Intent(Registration.this, Authorization.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };

    private class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get("https://galos.000webhostapp.com/create_user.php",
                    true, "login", loginEditText.getText().toString(), "password",
                    passwordEditText.getText().toString(), "email", emailEditText.getText().toString()).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                int success = dataJsonObj.getInt("success");
                if (success == 1) DatabaseHelper.showInformation("Аккаунт успешно создан");
                else {
                    String message = dataJsonObj.getString("message");
                    DatabaseHelper.showInformation(message);
                }

            } catch (JSONException e) {
                Log.d("my log", "Не вышло получить данные :(");
                e.printStackTrace();
            }
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(Registration.this, Authorization.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
