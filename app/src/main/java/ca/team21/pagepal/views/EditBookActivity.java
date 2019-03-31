package ca.team21.pagepal.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
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

        if (savedInstanceState != null) {
            book = savedInstanceState.getParcelable("BOOK");
            isbnView.setText(savedInstanceState.getString("ISBN"));
            titleEdit.setText(savedInstanceState.getString("TITLE"));
            authorEdit.setText(savedInstanceState.getString("AUTHOR"));
            descriptionEdit.setText(savedInstanceState.getString("DESCRIPTION"));
            setDisplayPhoto(savedInstanceState.getString("PHOTO"));
        } else if (sentIntent.hasExtra(BOOK_EXTRA)) {
            book = sentIntent.getParcelableExtra(BOOK_EXTRA);

            isbnView.setText(book.getIsbn());
            titleEdit.setText(book.getTitle());
            authorEdit.setText(book.getAuthor());
            descriptionEdit.setText(book.getDescription());
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
            rotated.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
     * Gets input from textEdits and either edits an existing book or creates a new book if it
     * doesn't exist yet.
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
