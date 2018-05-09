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
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.acceptButton:
                    if (!currentPasswordEditText.getText().toString().equals(""))
                        if (!newPasswordEditText.getText().toString().equals("") || !newEmailEditText.getText().toString().equals(""))
                            if (!currentPasswordEditText.getText().toString().equals(newPasswordEditText.getText().toString()))
                                if (!DatabaseHelper.getEmail().equals(newEmailEditText.getText().toString()))
                                    if (DatabaseHelper.getPassword().equals(currentPasswordEditText.getText().toString()))
                                    {
                                        DatabaseHelper.updateRowUsers(getApplicationContext(), newPasswordEditText.getText().toString(),
                                                newEmailEditText.getText().toString());
                                        DatabaseHelper.showInformation(getApplicationContext(),"Information updated successfully");
                                    }
                                    else DatabaseHelper.showInformation(getApplicationContext(),"Current password does not match");
                                else DatabaseHelper.showInformation(getApplicationContext(),"New and current e-mail match");
                            else DatabaseHelper.showInformation(getApplicationContext(),"New and current passwords match");
                        else DatabaseHelper.showInformation(getApplicationContext(),"Wrong data");
                    else DatabaseHelper.showInformation(getApplicationContext(),"Enter the current password");
                    break;
            }
        }
    };
}
