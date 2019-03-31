package ca.team21.pagepal.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.database.ValueEventListener;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.User;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        Button createAccountButton = findViewById(R.id.create_account_button);
        mEmailSignInButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                return;
            case R.id.create_account_button:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                return;
            default:
        }
    }
    /**
     *  Starts the Main activity.
     */
    private void onAuthSuccess() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        // There was an error; don't attempt login and focus the first
        // form field with an error.
        if (cancel) {
            focusView.requestFocus();
        } else {
            // Safe to try logging in
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Log.d(TAG, "signInWithEmail:success");

                                //Get and set new notification token here
                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w(TAG, "getInstanceId:failure", task.getException());
                                                    return;
                                                } else {
                                                    Log.d(TAG, "getInstanceId:success");
                                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                                    final String token = task.getResult().getToken();

                                                    final Query query = db.child("users").orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    query.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String key = "";
                                                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                                                key = item.getKey();
                                                                break;
                                                            }
                                                            User user = dataSnapshot.child(key).getValue(User.class);
                                                            String userName = user.getUsername();

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("users")
                                                                    .child(userName)
                                                                    .child("messagingToken").setValue(token);
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            }
                                        });



                                onAuthSuccess();
                            } else {
                                // If sign in fails, display a message.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    /**
     * Validates the entered email.
     *
     * @param email The string the user is trying to use as an email.
     * @return  true if valid. false otherwise.
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Validates the entered password.
     *
     * @param password  The string the user is trying to use as a password.
     * @return  true if valid. false otherwise.
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

