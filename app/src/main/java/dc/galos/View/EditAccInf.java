package dc.galos.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dc.galos.Controller.DatabaseHelper;
import dc.galos.R;

public class EditAccInf extends AppCompatActivity {

    private EditText newEmailEditText;
    private EditText newPasswordEditText;
    private EditText currentPasswordEditText;
    private Button backButton;
    private Button acceptButton;


    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acc_inf);

        acceptButton = findViewById(R.id.acceptButton);
        backButton = findViewById(R.id.backButton);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        newEmailEditText = findViewById(R.id.newEmailEditText);

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
                                    DatabaseHelper.updateRowUsers(newPasswordEditText.getText().toString(),
                                            newEmailEditText.getText().toString());
                                    DatabaseHelper.showInformation(getResources().getString(R.string.information_update));
                                }
                                else DatabaseHelper.showInformation(getResources().getString(R.string.incorrect_current_password));
                            else DatabaseHelper.showInformation(getResources().getString(R.string.match_mail));
                        else DatabaseHelper.showInformation(getResources().getString(R.string.match_password));
                    else DatabaseHelper.showInformation(getResources().getString(R.string.wrong_data));
                    break;
            }
        }
    };
}
