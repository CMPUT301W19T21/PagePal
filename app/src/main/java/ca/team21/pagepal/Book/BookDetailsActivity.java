package ca.team21.pagepal.Book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ca.team21.pagepal.R;
import ca.team21.pagepal.User;

import static ca.team21.pagepal.MainActivity.BOOK_EXTRA;
import static ca.team21.pagepal.MainActivity.USER_EXTRA;

public class BookDetailsActivity extends AppCompatActivity {

    Book book;
    User user;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("users");

    TextView titleView = findViewById(R.id.title_view);
    TextView authorView = findViewById(R.id.author_view);
    TextView isbnView = findViewById(R.id.isbn_view);
    TextView statusView = findViewById(R.id.status_view);
    TextView descriptionView = findViewById(R.id.description_view);
    TextView ownerView = findViewById(R.id.owner_view);
    Button requestButton = findViewById(R.id.request_button);
    Button acceptButton = findViewById(R.id.request_button);
    Button declineButton = findViewById(R.id.decline_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent intent = getIntent();
        book = intent.getParcelableExtra(BOOK_EXTRA);
        user = intent.getParcelableExtra(USER_EXTRA);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        String isbnLabel = "ISBN: " + book.getIsbn();
        isbnView.setText(isbnLabel);
        statusView.setText(book.getStatus().toUpperCase());
        descriptionView.setText(book.getDescription());


        String ownerLabel;
        //Get owner's info from db
        String ownerUsername = dbref.child(book.getOwner()).child("username").getKey();
        if (ownerUsername.equals(user.getUsername())) { // current user owns this book
            ownerLabel = "You own this book";
            if (book.getStatus().equals(Book.REQUESTED)) {
                acceptButton.setVisibility(View.VISIBLE);
                declineButton.setVisibility(View.VISIBLE);
            }
        } else {
            ownerLabel = "Owner: " + ownerUsername;
            requestButton.setVisibility(View.VISIBLE);
        }
        ownerView.setText(ownerLabel);
    }
}
