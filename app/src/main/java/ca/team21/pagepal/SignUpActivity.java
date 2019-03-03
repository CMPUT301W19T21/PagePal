package ca.team21.pagepal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Date;

public class SignUpActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser authUser;
    private DatabaseReference dbRef;
    private User user;

    private EditText nameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmPasswordView;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        nameView = findViewById(R.id.name_input);
        emailView = findViewById(R.id.email_input);
        passwordView = findViewById(R.id.password_input);
        confirmPasswordView = findViewById(R.id.confirm_password_input);

        Button signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_up_button) {

            name = nameView.getText().toString();
            email = emailView.getText().toString();
            password = passwordView.getText().toString();
            confirmPassword = confirmPasswordView.getText().toString();

            if (createAccount()) {
                finish();
            }
        }
    }

    /**
     * Attempts to create a new account given the form info. Preforms validation on the fields to
     * ensure correctness. If the validation fails there will be no attempt to create a user.
     *
     * @return true if the user is created. false otherwise.
     */
    public boolean createAccount() {
        emailView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email
        if (TextUtils.isEmpty(email)) {
            emailView.setError("This field is required");
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError("This email address is invalid");
            focusView = emailView;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            passwordView.setError("This field is required");
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError("This password is too short");
            focusView = passwordView;
            cancel = true;
        } else if (!passwordsMatch(password, confirmPassword)) {
            confirmPasswordView.setError("Passwords don't match!");
            focusView = confirmPasswordView;
            cancel = true;
        }

        // If name is empty use email
        if (TextUtils.isEmpty(name)) {
            name = email.split("@")[0];
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // attempt to sign up
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(SignUpActivity.this, "User Created.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    //Toast.makeText(SignUpActivity.this, "User exists.",
                                    //        Toast.LENGTH_SHORT).show();
                                    emailView.setError("Email in use.");
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            //mAuth.signInWithEmailAndPassword(email, password);
            // Wait for user to be authenticated or timeout
            long waitTime = new Date().getTime() + 2 * 1000;
            while( authUser == null && (new Date().getTime() < waitTime) ) {
                authUser = mAuth.getCurrentUser();
            }
            user = new User(name, authUser.getEmail());
            writeNewUser(authUser.getUid(), user);
            return true;
        }
        return false;


    }

    /**
     * Validates the entered email.
     *
     * @param email The String the user is trying to use as an email.
     * @return  true if valid. false otherwise.
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Validates the entered password.
     *
     * @param password The string the User is trying to use as a password.
     * @return  true if valid. false otherwise.
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Ensures the two password fields match. This ensures there was no incorrect input by the user.
     * @param password The string the User is trying to use as a password.
     * @param confirmPassword The string the User is trying to use as a password.
     * @return  true if the passwords match, false otherwise.
     */
    private boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Stores a user object in the database when a new user is created.
     *
     * @param uid   The FirebaseUser uid to serve as a key.
     * @param user  The User object to store.
     */
    private void writeNewUser(String uid, User user) {
        dbRef.child("users").child(uid).setValue(user);
    }
}
