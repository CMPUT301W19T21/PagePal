package ca.team21.pagepal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DatabaseReference usersRef;
    private FirebaseUser authUser;
    private TextView mTextMessage;
    private TextView helloMessage;
    private User user;

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

        // Wait for user to authenticate or timeout
        long waitTime = new Date().getTime() + 1 * 1000;
        while ( authUser == null && new Date().getTime() < waitTime ) {
            authUser = FirebaseAuth.getInstance().getCurrentUser();
        }
        // Quit if authentication fails. Might want to add something more elegant later...
        if (authUser == null) {
            Toast.makeText(MainActivity.this, "Couldn't Authenticate",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Connect to database
        usersRef = FirebaseDatabase.getInstance().getReference();

        // Get the user who is logged in
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.child("users").child(authUser.getUid()).getValue(User.class);
                        helloMessage.setText("Hello " + user.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                    }
                });

        // Set up top toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Set up bottom nav bar
        mTextMessage = (TextView) findViewById(R.id.message);
        helloMessage = findViewById(R.id.username_test);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
