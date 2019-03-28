package ca.team21.pagepal.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;

import static ca.team21.pagepal.models.Book.AVAILABLE;
import static ca.team21.pagepal.views.MainActivity.BOOK_EXTRA;

/**
 * Activity to edit and add books
 */
public class EditBookActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "EditBookActivity";

    private DatabaseReference dbRef;
    private Book book;
    private String bookKey;
    private Intent sentIntent;
    private Intent returnIntent;

    private EditText isbnEdit;
    private EditText titleEdit;
    private EditText authorEdit;
    private EditText descriptionEdit;
    private Button uploadImageButton;
    private Button scanISBNButton;
    private Button doneButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        dbRef = FirebaseDatabase.getInstance().getReference().child("books");

        sentIntent = getIntent();

        isbnEdit = findViewById(R.id.isbn_edit);
        titleEdit = findViewById(R.id.title_edit);
        authorEdit = findViewById(R.id.author_edit);
        descriptionEdit = findViewById(R.id.description_edit);
        uploadImageButton = findViewById(R.id.upload_image_button);
        scanISBNButton = findViewById(R.id.scan_ISBN_button);
        doneButton = findViewById(R.id.done_button);
        cancelButton = findViewById(R.id.cancel_button);

        uploadImageButton.setOnClickListener(this);
        scanISBNButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        if (sentIntent.hasExtra(BOOK_EXTRA)) {
            book = sentIntent.getParcelableExtra(BOOK_EXTRA);

            isbnEdit.setText(book.getIsbn());
            titleEdit.setText(book.getTitle());
            authorEdit.setText(book.getAuthor());
            descriptionEdit.setText(book.getDescription());
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.upload_image_button:
                //TODO open camera to take picture of book
                // book.setIsbn( ISBN from scan )
                return;
            case R.id.scan_ISBN_button:
                //TODO open camera to scan ISBN, auto fill info from google books
                return;
            case R.id.done_button:
                processInput();
                finish();
                return;
            case R.id.cancel_button:
                finish();
                return;
        }
    }

    /**
     * Gets input from textEdits and either edits an existing book or creates a new book if it
     * doesn't exist yet.
     */
    private void processInput() {
        String isbn = isbnEdit.getText().toString();
        String title = titleEdit.getText().toString();
        String author = authorEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

        if (book == null) {
            book = new Book();
            book.setStatus(AVAILABLE);
        }

        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setOwner(User.getInstance().getUsername());

        book.writeToDb();
        returnIntent = new Intent();
        returnIntent.putExtra(BOOK_EXTRA, book);
    }
}
