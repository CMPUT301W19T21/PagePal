package ca.team21.pagepal.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.User;

/**
 * Activity to allow a user to edit their profile or view other profiles
 */
public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "EditUserActivity";

    private User user;
    private EditText email;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        // TODO force users to reauthenticate before editing.

        user = User.getInstance();

        email = findViewById(R.id.email_input);
        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);

        email.setText(user.getEmail());

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.save:
                writeChanges();
                setResult(RESULT_OK);
                finish();
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                finish();
        }

    }

    /**
     * Write the new user data to the database and the FirebaseUser profile if necessary.
     */
    private void writeChanges() {
        String newEmail = email.getText().toString();
        user.setEmail(newEmail);
        user.writeToDb();
    }
}
