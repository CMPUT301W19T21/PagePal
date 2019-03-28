package ca.team21.pagepal.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.CoverPhoto;
import ca.team21.pagepal.models.User;
import id.zelory.compressor.Compressor;

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
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                //TODO open camera to take picture of book
                // book.setIsbn( ISBN from scan )
                return;
            case R.id.scan_ISBN_button:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            File f = new File(this.getCacheDir(), "filename");
            try{
                f.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte [] byteMapData = stream.toByteArray();
            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(byteMapData);
                fos.flush();
                fos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            try{
                Bitmap compressedImageBitmap = new Compressor(this).compressToBitmap(f);
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte [] photoByteArray = stream.toByteArray();
                String imageString = Base64.encodeToString(photoByteArray, Base64.DEFAULT);
                CoverPhoto finalPhoto = new CoverPhoto(imageString);
            } catch(IOException e){
                e.printStackTrace();
            }

        }
        else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null){
                isbnEdit.setText(scanningResult.getContents());
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "no scan data", Toast.LENGTH_SHORT);
                toast.show();

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
