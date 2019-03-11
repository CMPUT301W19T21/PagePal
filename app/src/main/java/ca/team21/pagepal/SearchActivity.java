package ca.team21.pagepal;
import android.app.SearchManager;
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
import android.view.MenuItem;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import ca.team21.pagepal.Book.Book;
import ca.team21.pagepal.Book.BookFragment;
import ca.team21.pagepal.Book.BookList;

/**
    SearchActivity retrieves a query from an Intent, searches firebase data and presents the result
 **/

public class SearchActivity extends AppCompatActivity implements SearchedResultsFragment.OnSearchFragmentInteractionListener {

    private static final String TAG = "SearchActivity";
    private String query;
    private DatabaseReference reference;
    private ArrayList<Book> bookList = new ArrayList<Book>();
    private FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize Database

        reference = FirebaseDatabase.getInstance().getReference();

        //Get intent (aka receive query)
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
             final String keyWord = intent.getStringExtra(SearchManager.QUERY);
            queryBooks(keyWord);
            //Probably put adaptor here
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }



    }

    /**
     * Queries the firebase realtime database, filters the results, maps them to Books and adds the Books to a list.
     *
     * @param query a string representation of the user's search query
     */
    public void queryBooks(final String query) {

        final String[] keyWords = query.split("\\s+");
        // Query Firebase
        Query bookQuery = reference.child("books");

        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        String status = data.child("status").getValue(String.class);
                        // Filter by Status
                            if (status.equals("Available") || status.equals("Requested")) {

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
                    loadFragment(SearchedResultsFragment.newInstance(bookList));

                }

                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                Log.w(TAG, "queryBooks:failure", databaseError.toException());
            }

        });


        }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     *  Used to implement OnSearchFragmentInteractionListener
     * @param book an instance of the Book class
     * @param user an instance of the User class
     */
    @Override
    public void viewBookInteraction(Book book, User user) {

    }

    /**
     * Used to implement OnSearchFragmentInteractionListener
     * @param user an instance of the User class
     */
    @Override
    public void viewUserInteraction(User user) {

    }
}


