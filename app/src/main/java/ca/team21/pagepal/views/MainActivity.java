package ca.team21.pagepal.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;

/**
 * Home page activity, this is the first thing the user sees after logging in. From this page they
 * can access all of the functionality of the app using a bottom navigation bar.
 */
public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnHomeInteractionListener,
        BorrowingFragment.OnBorrowingInteractionListener,
        BookFragment.OnListFragmentInteractionListener,
        NotificationsFragment.OnNotificationsInteractionListener,
        ProfileFragment.OnProfileInteractionListener,
        SearchedResultsFragment.OnSearchFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int EDIT_USER = 9;
    private static final int EDIT_BOOK = 5;
    private static final int VIEW_MY_BOOK = 7;
    public static final String USER_EXTRA = "ca.team21.pagepal.user";
    public static final String BOOK_EXTRA = "ca.team21.pagepal.models.Book";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FirebaseUser authUser;
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
                    loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.borrowed_books:
                    loadFragment(BorrowingFragment.newInstance());
                    return true;
                case R.id.owned_books:

                    loadFragment(BookFragment.newInstance(/*1, user.getOwnedBookList()*/)); // 1 = number of columns in book list
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(NotificationsFragment.newInstance());
                    return true;
                case R.id.navigation_profile:
                    loadFragment(ProfileFragment.newInstance(user));
                    return true;
                default:
                    loadFragment(HomeFragment.newInstance());
            }
            return false;
        }
    };

    /**
     * Load the given fragment in to view.
     *
     * @param fragment  The Fragment to load.
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(HomeFragment.newInstance());

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String keyword = getIntent().getStringExtra(SearchManager.QUERY);
            queryBooks(keyword);
        }
        if (intent.hasExtra(USER_EXTRA)) {
            User userToView = intent.getParcelableExtra(USER_EXTRA);
            loadFragment(ProfileFragment.newInstance(userToView));
        }

        // Wait for user to authenticate or timeout
        long waitTime = new Date().getTime() + 2 * 1000;
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
        // Get the user who is logged in
        user = User.getInstance();

        // Set up top toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Set up bottom nav bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);





    }
    /**
     * Queries the firebase realtime database, filters the results, maps them to Books and adds the Books to a list.
     *
     * @param query a string representation of the user's search query
     */
    public void queryBooks(final String query) {

        final ArrayList<Book> bookList = new ArrayList<Book>();
        final String[] keyWords = query.split("\\s+");
        // Query Firebase
        Query bookQuery = FirebaseDatabase.getInstance().getReference().child("books");

        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot users: dataSnapshot.getChildren()) { // for each user
                        for (DataSnapshot data: users.getChildren()) { // for each of their books
                            String status = data.child("status").getValue(String.class);
                            // Filter by Status
                            if (status != null && ( status.equals("Available") || status.equals("Requested"))) {

                                Book book = data.getValue(Book.class);
                                for (String keyWord : keyWords) {

                                    if ((book.getAuthor().toUpperCase()).contains(keyWord.toUpperCase()) ||
                                            (book.getTitle().toUpperCase()).contains(keyWord.toUpperCase()) ||
                                            (book.getDescription().toUpperCase()).contains(keyWord.toUpperCase())) {
                                        bookList.add(book);
                                    }
                                }
                            }
                        }
                    }
                    loadFragment(SearchedResultsFragment.newInstance(bookList));

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                Log.w(TAG, "queryBooks:failure", databaseError.toException());
            }

        });


    }


    // TODO Remove the toolbar and place the sign out option in the user profile page.
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

    @Override
    public void onHomeInteraction() {
    }

    @Override
    public void onBorrowingInteraction() {
    }

    /**
     * Responds to clicking the Add Book button
     */
    @Override
    public void onBookListAddButtonClick() {
        Intent intent = new Intent(this, EditBookActivity.class);
        startActivity(intent);
    }

    /**
     * Responds to the user clicking on a book
     * @param book The book the user clicked on
     */
    @Override
    public void viewMyBookInteraction(Book book) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra(BOOK_EXTRA, book);
        intent.putExtra(USER_EXTRA, user);
        startActivityForResult(intent, VIEW_MY_BOOK);
    }

    /**
     * Responds to the user clicked the Edit Book button
     * @param book The book the user wants to edit
     */
    @Override
    public void editBookInteraction(Book book) {
        Intent intent = new Intent(this, EditBookActivity.class);
        intent.putExtra(BOOK_EXTRA, book);
        startActivityForResult(intent, EDIT_BOOK);
    }

    /**
     * Responds to the user clicking on a profile
     * @param user The profile that the user clicked on
     */
    @Override
    public void viewUserInteraction(String user) {
        loadFragment(ProfileFragment.newInstance(User.getUser(user)));
    }

    /**
     * Responds to the Notifications button
     */
    @Override
    public void onNotificationsInteraction() {
    }

    /**
     * Responds to the user clicking the profile button
     * @param user The profile to view
     */
    @Override
    public void onProfileInteraction(User user) {
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra(USER_EXTRA, user);
        startActivityForResult(intent, EDIT_USER);
    }


    @Override
    public void viewBookInteraction(Book book) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra(MainActivity.BOOK_EXTRA, book);
        User requester = User.getInstance();
        intent.putExtra(MainActivity.USER_EXTRA, requester);
        startActivity(intent);
    }
}
