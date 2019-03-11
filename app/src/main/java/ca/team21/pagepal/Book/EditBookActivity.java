package ca.team21.pagepal.Book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ca.team21.pagepal.R;

import static ca.team21.pagepal.MainActivity.BOOK_EXTRA;

/**
 * Used to edit and add books
 */
public class EditBookActivity extends AppCompatActivity implements View.OnClickListener{

    private Book book;
    private Intent sentIntent;
    private Intent returnIntent;

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

        sentIntent = getIntent();

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
                // book.setISBN( ISBN from scan )
            case R.id.scan_ISBN_button:
                //TODO open camera to scan ISBN, auto fill info from google books
            case R.id.done_button:
                processInput();
                finish();
            case R.id.cancel_button:
                finish();
        }
    }

    private void processInput() {
        String title = titleEdit.toString();
        String author = authorEdit.toString();
        String description = descriptionEdit.toString();

        if (book == null) {
            book = new Book();
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);

        returnIntent = new Intent();
        returnIntent.putExtra(BOOK_EXTRA, book);
    }
}
