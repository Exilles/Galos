package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import dc.galos.R;

public class Profile extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Button editAccInfButton = findViewById(R.id.editAccInfButton);
        final Button backButton = findViewById(R.id.backButton);
        final ImageButton editIconImageButton = findViewById(R.id.editIconImageButton);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.backButton:
                        intent = new Intent(Profile.this, Menu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.editAccInfButton:
                        intent = new Intent(Profile.this, EditAccInf.class);
                        startActivity(intent);
                        break;
                    case R.id.editIconImageButton:
                        intent = new Intent(Profile.this, EditIcon.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        editAccInfButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
        editIconImageButton.setOnClickListener(onClickListener);
    }
}
