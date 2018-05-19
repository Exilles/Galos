package dc.galos.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.kevinsawicki.http.HttpRequest;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class RestorePassword extends AppCompatActivity {

    private Button acceptButton;
    private Button backButton;
    private LinearLayout progress;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        progress = findViewById(R.id.progress);

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptButton:
                    intent = new Intent(RestorePassword.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
        intent = new Intent(RestorePassword.this, Authorization.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
