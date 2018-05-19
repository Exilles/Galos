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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class RestorePassword extends AppCompatActivity {

    private Button acceptButton;
    private Button backButton;
    private EditText enterLoginEditText;
    private EditText enterEmailEditText;
    private LinearLayout progress;

    private int flag = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);


        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        enterLoginEditText = findViewById(R.id.enterLoginEditText);
        enterEmailEditText = findViewById(R.id.enterEmailEditText);
        progress = findViewById(R.id.progress);

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptButton:

                    if (!enterLoginEditText.getText().toString().equals("") || !enterEmailEditText.getText().toString().equals("")) {
                        if (!enterLoginEditText.getText().toString().equals("") && !enterEmailEditText.getText().toString().equals("")) flag = 1;
                        else if (!enterLoginEditText.getText().toString().equals("")) flag = 2;
                        else flag = 3;
                        new ParseTask().execute();
                    }
                    else  DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));

                    break;
                case R.id.backButton:
                    intent = new Intent(RestorePassword.this, Authorization.class);
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
            switch (flag){
                case 1:
                    return HttpRequest.get("https://galos.000webhostapp.com/restore_password.php",
                            true,"login", enterLoginEditText.getText().toString(), "email", enterEmailEditText.getText().toString()).body();
                case 2:
                    return HttpRequest.get("https://galos.000webhostapp.com/restore_password.php",
                            true,"login", enterLoginEditText.getText().toString()).body();
                case 3:
                    return HttpRequest.get("https://galos.000webhostapp.com/restore_password.php",
                            true,"email", enterEmailEditText.getText().toString()).body();
                default: return null;
            }
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject dataJsonObj = new JSONObject(strJson);
                int success = dataJsonObj.getInt("success");
                if (success == 1) DatabaseHelper.showInformation("Ссылка для активации отправлена на указанный email");
                else DatabaseHelper.showInformation("Такой пользователь не найден");
            } catch (JSONException e) {
                Log.d("my log", "Не вышло получить данные :(");
                e.printStackTrace();
            }
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(RestorePassword.this, Authorization.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
