package ca.team21.pagepal.views;

import android.content.Intent;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ca.team21.pagepal.BR;
import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;
import id.zelory.compressor.Compressor;

import static ca.team21.pagepal.models.Book.AVAILABLE;
import static ca.team21.pagepal.views.MainActivity.BOOK_EXTRA;

/**
 * Activity to edit and add books
 */
public class EditBookActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String TAG = "EditBookActivity";

    private DatabaseReference dbRef;
    private Book book;
    private String bookKey;
    private Intent sentIntent;
    private Intent returnIntent;

    private ImageView coverPhoto;
    private Button isbnView;
    private EditText titleEdit;
    private EditText authorEdit;
    private EditText descriptionEdit;
    private Button uploadImageButton;
    private Button deleteImageButton;
    private Button doneButton;
    private Button cancelButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private CameraManager cameraManager;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        dbRef = FirebaseDatabase.getInstance().getReference().child("books");

        sentIntent = getIntent();

        coverPhoto = findViewById(R.id.photo);
        isbnView = findViewById(R.id.isbn);
        titleEdit = findViewById(R.id.title_edit);
        authorEdit = findViewById(R.id.author_edit);
        descriptionEdit = findViewById(R.id.description_edit);
        Spinner spinner = findViewById(R.id.genre_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Genre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        uploadImageButton = findViewById(R.id.upload_image_button);
        deleteImageButton = findViewById(R.id.delete_image_button);
        doneButton = findViewById(R.id.done_button);
        cancelButton = findViewById(R.id.cancel_button);


        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);



        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[0]);
            orientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        isbnView.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);
        deleteImageButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        if (savedInstanceState != null) {
            book = savedInstanceState.getParcelable("BOOK");
            isbnView.setText(savedInstanceState.getString("ISBN"));
            titleEdit.setText(savedInstanceState.getString("TITLE"));
            authorEdit.setText(savedInstanceState.getString("AUTHOR"));
            descriptionEdit.setText(savedInstanceState.getString("DESCRIPTION"));
            setDisplayPhoto(savedInstanceState.getString("PHOTO"));
        } else if (sentIntent.hasExtra(BOOK_EXTRA)) {
            book = sentIntent.getParcelableExtra(BOOK_EXTRA);

            FirebaseDatabase.getInstance().getReference("books/" + book.getOwner() + "/" + book.getIsbn())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book temp = dataSnapshot.getValue(Book.class);
                            book.setPhoto(temp.getPhoto());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            book.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (propertyId == BR.photo) {
                        setDisplayPhoto(book.getPhoto());
                    }
                }
            });

            isbnView.setText(book.getIsbn());
            titleEdit.setText(book.getTitle());
            authorEdit.setText(book.getAuthor());
            descriptionEdit.setText(book.getDescription());
            String searchedItem = book.getGenre();
            int itemPosition = adapter.getPosition(searchedItem);
            if (searchedItem.equals("")) { // if book had no genre
                itemPosition = 0;
            }
            spinner.setSelection(itemPosition);
            setDisplayPhoto(book.getPhoto());

        } else {
            book = new Book();
            book.setStatus(AVAILABLE);
        }

    }

    private void setDisplayPhoto(String bitString) {
        if (bitString != null && !bitString.equals("")) {
            byte [] stringToBit = Base64.decode(bitString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(stringToBit, 0, stringToBit.length);
            coverPhoto.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

        if (parent.getItemAtPosition(position).equals("Choose Genre")) {
            //do nothing
        } else {
            String genreEdit = parent.getItemAtPosition(position).toString();
            book.setGenre(genreEdit);
            //Toast.makeText(parent.getContext(), genreEdit, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("BOOK", book != null ? book : new Book());
        savedInstanceState.putString("ISBN", isbnView.getText().toString());
        savedInstanceState.putString("TITLE", titleEdit.getText().toString());
        savedInstanceState.putString("AUTHOR", authorEdit.getText().toString());
        savedInstanceState.putString("DESCRIPTION", descriptionEdit.getText().toString());
        Book tempBook = savedInstanceState.getParcelable("BOOK");
        savedInstanceState.putString("PHOTO", tempBook != null ? tempBook.getPhoto() : "");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.upload_image_button:
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                return;
            case R.id.delete_image_button:
                if (book.getPhoto().equals("")) {
                    break;
                } else {
                    book.setPhoto("");
                    coverPhoto.setImageResource(R.drawable.ic_book_24px);
                    break;
                }
            case R.id.isbn:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                return;
            case R.id.done_button:
                if (isbnView.getText().toString().equals(getString(R.string.isbn_prompt))) {
                    isbnView.setError("ISBN must be scanned!");
                    return;
                } else {
                    processInput();
                    finish();
                    return;
                }
            case R.id.cancel_button:
                finish();
                return;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File f = new File(this.getCacheDir(), "filename");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            Matrix matrix = new Matrix();
            if (image.getWidth() > image.getHeight()) {
                matrix.postRotate(90);
            }
            Bitmap rotated = Bitmap.createBitmap(image,0,0,image.getWidth(),image.getHeight(), matrix, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotated.compress(Bitmap.CompressFormat.PNG, 75, stream);
            byte[] byteMapData = stream.toByteArray();
            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(byteMapData);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Bitmap compressedImageBitmap = new Compressor(this).compressToBitmap(f);
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte [] output = stream.toByteArray();
                String stringPic = Base64.encodeToString(output, Base64.DEFAULT);
                book.setPhoto(stringPic);
                setDisplayPhoto(book.getPhoto());

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null && scanningResult.getContents() != null) {
                isbnView.setText(scanningResult.getContents());
            } else {
                isbnView.setText(getString(R.string.isbn_prompt));
                Toast toast = Toast.makeText(getApplicationContext(), "no scan data", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    /**
     * Gets input from textEdits and sets information in the book object
     */
    private void processInput() {
        String isbn = isbnView.getText().toString();
        String title = titleEdit.getText().toString();
        String author = authorEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

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
