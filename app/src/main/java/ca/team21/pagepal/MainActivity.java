package ca.team21.pagepal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.client.Firebase;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    /**
     * Sets up the Bottom navigation bar
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set up top toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        // Set up bottom nav bar
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Connect to FirebaseDB
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://pagepal-5b78c.firebaseio.com/");

        // Listen for data changes
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check if there is a user signed in and update UI accordingly
        user = mAuth.getCurrentUser();
        updateUI(user);
    }

    /**
     * Creates the buttons in the toolbar.
     *
     * @param menu  The menu resource to inflate.
     * @return      The result of the super call.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Responds to clicking items in the toolbar.
     *
     * @param item  The item that was pressed.
     * @return      The result of the click.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("Login Successful");
            } else {
                if (response == null) {
                    return;
                } else {
                    System.out.println("Login failed: " + response.getError().getErrorCode());
                }

            }
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            // Start log in activity
            // Get action code for email links
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setAndroidPackageName("ca.team21.pagepal", /* installIfNotAvailable= */ true,
                            /* minimumVersion= */ null)
                    .setHandleCodeInApp(true) // This must be set to true
                    .setUrl("https://pagepal.page.link") // This URL needs to be whitelisted
                    .build();
            // Set up providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().enableEmailLinkSignIn()
                            .setActionCodeSettings(actionCodeSettings).build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.AnonymousBuilder().build()
            );
            // Start Sign-in Activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN
            );
        }
    }
}
