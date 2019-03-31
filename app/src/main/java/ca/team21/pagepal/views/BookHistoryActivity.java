package ca.team21.pagepal.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;

import static ca.team21.pagepal.views.MainActivity.BOOK_EXTRA;
import static ca.team21.pagepal.views.MainActivity.USER_EXTRA;

public class BookHistoryActivity extends AppCompatActivity {
    Book book;
    User user;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("users");

    String ownerUsername;
    String ownerLabel;

    TextView titleView;
    TextView authorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_history);

        Intent intent = new Intent();
        book = intent.getParcelableExtra(BOOK_EXTRA);
        user = intent.getParcelableExtra(USER_EXTRA);

        titleView = findViewById(R.id.title_view);
        authorView = findViewById(R.id.author_view);



    }
}
