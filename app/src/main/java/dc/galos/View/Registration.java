package dc.galos.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        progress = findViewById(R.id.progress);

        intent = getIntent();
        flag = intent.getBooleanExtra("registration", true);

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptButton:
                    if (!loginEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("") &&
                            !confirmPasswordEditText.getText().toString().equals("") && !emailEditText.getText().toString().equals(""))
                        if (loginEditText.getText().toString().matches("^[a-zA-Z][a-zA-Z0-9-_.]{1,20}$") &&
                                passwordEditText.getText().toString().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$") &&
                                emailEditText.getText().toString().matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$"))
                            if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
                                if (DatabaseHelper.isOnline()) new ParseTask().execute();
                                else DatabaseHelper.showInformation("Нет подключения к интернету");
                            else  DatabaseHelper.showInformation(getResources().getString(R.string.match_passwords));
                        else DatabaseHelper.showInformation("Введены недопустимые символы");
                    else  DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));
                    break;
                case R.id.backButton:
                    intent = new Intent(Registration.this, Authorization.class);
                    intent.putExtra("back", false);
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
            if (flag) return HttpRequest.get("https://galos.000webhostapp.com/create_user.php",
                    true, "login", loginEditText.getText().toString(), "password",
                    passwordEditText.getText().toString(), "email", emailEditText.getText().toString(), "money", 0, "record", 0,
                    "status", "00000000000000000000000000", "all_levels", 0, "all_money", 0, "all_eating", 0, "all_wins", 0,
                    "mode", 0, "score", 0, "all_rewards", 0, "score_1", 0, "all_rewards_1", 0, "score_2", 0, "all_rewards_2", 0,
                    "score_3", 0, "all_rewards_3", 0, "score_4", 0, "all_rewards_4", 0).body();
            else return HttpRequest.get("https://galos.000webhostapp.com/create_user.php",
                    true, "login", loginEditText.getText().toString(), "password",
                    passwordEditText.getText().toString(), "email", emailEditText.getText().toString(), "money",
                    DatabaseHelper.getMoney(), "record", DatabaseHelper.getRecord(), "status", DatabaseHelper.getStatus(),
                    "all_levels", DatabaseHelper.getAll_levels(), "all_money", DatabaseHelper.getAll_money(),
                    "all_eating", DatabaseHelper.getAll_eating(), "all_wins", DatabaseHelper.getAll_wins(),
                    "mode", DatabaseHelper.getMode(), "score", DatabaseHelper.getScore(), "all_rewards",
                    DatabaseHelper.getAll_rewards(), "score_1", DatabaseHelper.getScore_1(), "all_rewards_1",
                    DatabaseHelper.getAll_rewards_1(), "score_2", DatabaseHelper.getScore_2(), "all_rewards_2",
                    DatabaseHelper.getAll_rewards_2(), "score_3", DatabaseHelper.getScore_3(), "all_rewards_3",
                    DatabaseHelper.getAll_rewards_3(), "score_4", DatabaseHelper.getScore_4(), "all_rewards_4",
                    DatabaseHelper.getAll_rewards_4()).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                int success = dataJsonObj.getInt("success");
                if (success == 1) {
                    DatabaseHelper.zeroGuest();
                    DatabaseHelper.showInformation("Аккаунт успешно создан");
                }
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
        intent.putExtra("back", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
