package ca.team21.pagepal;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import ca.team21.pagepal.Book.Book;
import ca.team21.pagepal.Book.BookList;

/*
TODO: FIX this
basic check query of

 */


public class SearchActivity extends AppCompatActivity  {

    private static final String TAG = "SearchActivity";
    private String query;
    private DatabaseReference reference;
    private BookList bookList = new BookList();
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
        }

        

    }

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
                        if (status != null) {
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
                    }
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                Log.w(TAG, "queryBooks:failure", databaseError.toException());
            }

        });


        }
    }


