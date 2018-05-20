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
import android.widget.SimpleAdapter;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.Controller.JSONParser;
import dc.galos.R;

public class EditAccInf extends AppCompatActivity {

    private EditText newEmailEditText;
    private EditText newPasswordEditText;
    private EditText currentPasswordEditText;
    private Button backButton;
    private Button acceptButton;
    private LinearLayout progress;


    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acc_inf);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        newEmailEditText = findViewById(R.id.newEmailEditText);
        progress = findViewById(R.id.progress);

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButton:
                    intent = new Intent(EditAccInf.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.acceptButton:
                    if (!currentPasswordEditText.getText().toString().equals("") || !newPasswordEditText.getText().toString().equals("") || !newEmailEditText.getText().toString().equals(""))
                        if (!currentPasswordEditText.getText().toString().equals(newPasswordEditText.getText().toString()))
                            if (!DatabaseHelper.getEmail().equals(newEmailEditText.getText().toString()))
                                if (DatabaseHelper.getPassword().equals(currentPasswordEditText.getText().toString()))
                                {
                                    if (!newPasswordEditText.getText().toString().equals("")) DatabaseHelper.setPassword(newPasswordEditText.getText().toString());
                                    if (!newEmailEditText.getText().toString().equals("")) DatabaseHelper.setEmail(newEmailEditText.getText().toString());
                                    new ParseTask().execute();
                                }
                                else DatabaseHelper.showInformation(getResources().getString(R.string.incorrect_current_password));
                            else DatabaseHelper.showInformation(getResources().getString(R.string.match_mail));
                        else DatabaseHelper.showInformation(getResources().getString(R.string.match_password));
                    else DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));
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
            return HttpRequest.get("https://galos.000webhostapp.com/update_user.php",
                    true,
                    "_id", DatabaseHelper.getId(),
                    "password", DatabaseHelper.getPassword(),
                    "email", DatabaseHelper.getEmail()).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            progress.setVisibility(View.INVISIBLE);
            DatabaseHelper.showInformation(getResources().getString(R.string.information_update));
        }
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(EditAccInf.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
