package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dc.galos.R;

public class RestorePassword extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        final Button acceptButton = findViewById(R.id.acceptButton);
        final Button backButton = findViewById(R.id.backButton);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.acceptButton:
                        intent = new Intent(RestorePassword.this, Menu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.backButton:
                        intent = new Intent(RestorePassword.this, Authorization.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
            }
        };

        acceptButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
    }
}
