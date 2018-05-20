package dc.galos.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private LinearLayout progress;

    private Sound sound = new Sound();
    private Intent intent;

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
        progress = findViewById(R.id.progress);

        sound.initialization(this, R.raw.background_music);

        loginAsGuestButton.setOnClickListener(onClickListener);
        registrationButton.setOnClickListener(onClickListener);
        restorePasswordButton.setOnClickListener(onClickListener);
        loginButton.setOnClickListener(onClickListener);
        rememberCheckBox.setOnClickListener(onClickListener);

        rememberCheckBox.setTypeface(Typeface.createFromAsset(getApplicationContext().getResources().getAssets(), "a_futurica_extrabold.ttf"));

        Intent intent = getIntent();

        if (intent.getBooleanExtra("back", true)){
            if (DatabaseHelper.searchRemember()) {
                if (DatabaseHelper.getLogin().equals("Гость")) {
                    DatabaseHelper.getGuestData();
                    intent = new Intent(Authorization.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    loginEditText.setText(DatabaseHelper.getLogin());
                    passwordEditText.setText(DatabaseHelper.getPassword());
                    rememberCheckBox.setChecked(true);
                    new ParseTask().execute();
                }
            }
        }
        else {
            if (!DatabaseHelper.getLogin().equals("Гость") && DatabaseHelper.searchRemember()) {
                loginEditText.setText(DatabaseHelper.getLogin());
                passwordEditText.setText(DatabaseHelper.getPassword());
                rememberCheckBox.setChecked(true);
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginAsGuestButton:
                    DatabaseHelper.getGuestData();
                    DatabaseHelper.rememberOrForgetUser("Гость", "Guest");
                    intent = new Intent(Authorization.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.registrationButton:
                    intent = new Intent(Authorization.this, Registration.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.restorePasswordButton:
                    intent = new Intent(Authorization.this, RestorePassword.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.loginButton:
                    if (!loginEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals("")){
                        new ParseTask().execute();
                    }
                    else  DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));
                    break;
                case R.id.rememberCheckBox:

                    break;
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    private class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {

            return HttpRequest.get("https://galos.000webhostapp.com/get_user_data.php",
                    true, "login", loginEditText.getText().toString(), "password",
                    passwordEditText.getText().toString()).body();

        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                int success = dataJsonObj.getInt("success");
                if (success == 1){
                    JSONArray data_user = dataJsonObj.getJSONArray("user");
                    JSONObject user = data_user.getJSONObject(0);
                    DatabaseHelper.getUserData(user.getInt("_id"), user.getString("login"), user.getString("password"),
                            user.getString("email"), user.getInt("money"), user.getInt("record"));

                    JSONArray achievements_user = dataJsonObj.getJSONArray("achievements");
                    JSONObject achievements = achievements_user.getJSONObject(0);
                    DatabaseHelper.getAchievementsUser(achievements.getString("status"), achievements.getInt("all_levels"),
                            achievements.getInt("all_money"), achievements.getInt("all_eating"), achievements.getInt("all_wins"));

                    JSONArray resume_user = dataJsonObj.getJSONArray("resume");
                    JSONObject resume = resume_user.getJSONObject(0);
                    DatabaseHelper.getResumeUser(resume.getInt("mode"), resume.getInt("score"), resume.getInt("all_rewards"));

                    if (rememberCheckBox.isChecked()) DatabaseHelper.rememberOrForgetUser(loginEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    else DatabaseHelper.rememberOrForgetUser(null,null);

                    intent = new Intent(Authorization.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else DatabaseHelper.showInformation("Неверно введены логин или пароль");
            } catch (JSONException e) {
                Log.d("my log", "Не вышло получить данные :(");
                e.printStackTrace();
            }
            progress.setVisibility(View.INVISIBLE);
        }
    }
}
