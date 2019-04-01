package ca.team21.pagepal.views;

import android.content.Intent;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.team21.pagepal.BR;
import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Notification;
import ca.team21.pagepal.models.Request;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.models.Book;

import static ca.team21.pagepal.views.MainActivity.BOOK_EXTRA;
import static ca.team21.pagepal.views.MainActivity.USER_EXTRA;

/**
 * Activity for viewing book details. Is also used for accepting/declining/requesting
 */
public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = "BookDetailsActivity";

    private final int MAP_REQUEST_CODE = 1;

    Book book;
    User user;
    String ownerUsername;
    String ownerLabel;
    int selectedRequesterIndex;
    Request selectedRequest;
    ArrayList<Request> requesters = new ArrayList<>();
    ArrayList<String> requesterUsernames = new ArrayList<>();

    TextView titleView;
    TextView authorView;
    TextView isbnView;
    TextView statusView;
    TextView descriptionView;
    TextView ownerView;
    TextView genre_view;
    ImageView imageView;
    TextView requesterLabel;
    Button requestButton;
    Button acceptButton;
    Button declineButton;
    Button viewLocationButton;
    Spinner requesterSpinner;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent intent = getIntent();
        book = intent.getParcelableExtra(BOOK_EXTRA);
        user = intent.getParcelableExtra(USER_EXTRA);

        FirebaseDatabase.getInstance().getReference("books").child(book.getOwner()).child(book.getIsbn())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Book temp = dataSnapshot.getValue(Book.class);
                        book.setTitle(temp.getTitle());
                        book.setAuthor(temp.getAuthor());
                        book.setDescription(temp.getDescription());
                        book.setIsbn(temp.getIsbn());
                        book.setStatus(temp.getStatus());
                        book.setGenre(temp.getGenre());
                        book.setOwner(temp.getOwner());
                        book.setPhoto(temp.getPhoto());
                        book.setBorrower(temp.getBorrower());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        book.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setDisplayInfo();
            }
        });

        imageView = findViewById(R.id.book_image_view);
        titleView = findViewById(R.id.title_view);
        authorView = findViewById(R.id.author_view);
        isbnView = findViewById(R.id.isbn_view);
        statusView = findViewById(R.id.status_view);
        descriptionView = findViewById(R.id.description_view);
        genre_view = findViewById(R.id.genre_view);
        ownerView = findViewById(R.id.owner_view);
        requestButton = findViewById(R.id.request_button);
        acceptButton = findViewById(R.id.accept_button);
        declineButton = findViewById(R.id.decline_button);
        viewLocationButton = findViewById(R.id.view_location_button);

        requesterSpinner = findViewById(R.id.requester_spinner);
        requesterLabel = findViewById(R.id.requester_label);


        requestButton.setOnClickListener(this);
        acceptButton.setOnClickListener(this);
        declineButton.setOnClickListener(this);
        requesterSpinner.setOnItemSelectedListener(this);
        viewLocationButton.setOnClickListener(this);
        ownerView.setOnClickListener(this);

        ownerView.setClickable(true);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, requesterUsernames);
        requesterSpinner.setAdapter(spinnerAdapter);

    }
    private void setDisplayInfo () {
        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        String isbnLabel = "ISBN: " + book.getIsbn();
        isbnView.setText(isbnLabel);
        statusView.setText(book.getStatus().toUpperCase());
        descriptionView.setText(book.getDescription());
        genre_view.setText(book.getGenre());

        setPicture();

        ownerUsername = book.getOwner();

        boolean isOwner = ownerUsername.equals(user.getUsername());
        boolean isBorrower = book.getBorrower().equals(user.getUsername());
        boolean isAvailable = book.getStatus().equals(Book.AVAILABLE);
        boolean isRequested = book.getStatus().equals(Book.REQUESTED);
        boolean isAccepted = book.getStatus().equals(Book.ACCEPTED);
        boolean isBorrowed = book.getStatus().equals(Book.BORROWED);


        if (isOwner) { // if the current user owns this book
            ownerLabel = "You own this book";
            ownerView.setClickable(false);
            if (isRequested) {
                acceptButton.setVisibility(View.VISIBLE);
                declineButton.setVisibility(View.VISIBLE);
                requesterSpinner.setVisibility(View.VISIBLE);
                requesterLabel.setVisibility(View.VISIBLE);

                getRequesters();
            } else if (isAccepted) {
                viewLocationButton.setVisibility(View.VISIBLE);
                // TODO show button to initiate scan (owner)
            }

        } else { // if the current user does not own the book
            ownerLabel = "Owner: " + ownerUsername;
            requestButton.setVisibility(View.VISIBLE);

            if (isAvailable || isRequested) { // if the book can be requested
                requestButton.setText("Request Book");

            } else if (isAccepted) {

                if (isBorrower) {
                    requestButton.setText("Your request for this book has been accepted");
                    requestButton.setClickable(false);
                    viewLocationButton.setVisibility(View.VISIBLE);

                } else { // user is not the borrower
                    requestButton.setClickable(false);
                    requestButton.setText("This book is unavailable");
                }

            } else if (isBorrowed) {
                requestButton.setClickable(false);
                requestButton.setText("This book is unavailable");
            }

        }

        ownerView.setText(ownerLabel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_button:
                sendRequest();
                break;
            case R.id.decline_button:
                declineRequest();
                break;
            case R.id.accept_button:
                acceptRequest();
                break;
            case R.id.owner_view:
                Intent viewUserIntent = new Intent(this, MainActivity.class);
                viewUserIntent.putExtra(Intent.EXTRA_TEXT, book.getOwner());
                startActivity(viewUserIntent);
                break;
            case R.id.view_location_button:
                viewLocation();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRequesterIndex = position;
        //requesterSpinner.setSelection(position, false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "nothing selected");
    }


    private void getRequesters() {
        DatabaseReference dbRefRequests = FirebaseDatabase.getInstance().getReference()
                .child("requests").child("owner").child(user.getUsername() + book.getIsbn());

        dbRefRequests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot request: dataSnapshot.getChildren()) {
                     requesters.add(request.getValue(Request.class));
                }
                updateSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Requesters query failed", databaseError.toException());
                Toast.makeText(BookDetailsActivity.this, "Failed retrieving requesters from database", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setPicture() {
        if (!book.getPhoto().equals("")) {
            byte[] stringToBit = Base64.decode(book.getPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(stringToBit, 0, stringToBit.length);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void updateSpinner() {
        requesterUsernames.clear();
        for (Request r: requesters) {
            requesterUsernames.add(r.getRequester());
        }
        spinnerAdapter.notifyDataSetChanged();
    }

    private void sendRequest() {
        book.setStatus(Book.REQUESTED);
        Request request = new Request(book.getOwner(), user.getUsername(), book);
        request.writeToDb();

        String message = user.getUsername() + " has requested " + book.getTitle();
        String senderUsername = user.getUsername();
        String recipientUsername = book.getOwner();
        Notification notification = new Notification(message, senderUsername, recipientUsername, book.getIsbn(), book.getOwner());
        notification.writeToDb();

        book.writeToDb();
        finish();
    }

    private void decline(Request declined) {
        declined.delete();
        requesters.remove(declined);

        String message = user.getUsername() + " has declined your request for " + book.getTitle();
        Notification notify = new Notification(message, user.getUsername(), declined.getRequester(), book.getIsbn(), user.getUsername());
        notify.writeToDb();
    }

    private void declineRequest() {
        Request requestToDecline = requesters.get(selectedRequesterIndex);
        decline(requestToDecline);
        updateSpinner();

        if (requesters.size() == 0) { // if last remaining request declined
            // update book to be Available
            String owner = requestToDecline.getOwner();
            String bookIsbn = requestToDecline.getBook().getIsbn();
            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(owner).child(bookIsbn);
            bookRef.child("status").setValue(Book.AVAILABLE);
            Toast.makeText(this, "Last remaining request declined", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void acceptRequest() {
        selectedRequest = requesters.remove(selectedRequesterIndex);

        for (Request r: requesters) { // decline all other requests
            decline(r);
        }

        Intent intent = new Intent(this, PickMapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);

        String owner = selectedRequest.getOwner();
        String bookIsbn = selectedRequest.getBook().getIsbn();
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(owner).child(bookIsbn);
        bookRef.child("status").setValue(Book.ACCEPTED);
        bookRef.child("borrower").setValue(selectedRequest.getRequester());

        String message = user.getUsername() + " has accepted your request for " + book.getTitle();
        Notification notify = new Notification(message, user.getUsername(), selectedRequest.getRequester(), book.getIsbn(), user.getUsername());
        notify.writeToDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedRequest.setLatitude(data.getDoubleExtra("latitude", -1));
            selectedRequest.setLongitude(data.getDoubleExtra("longitude", -1));

            selectedRequest.writeToDb();
            finish();
        }
    }

    public void viewLocation() {
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("requests").child("requester")
                .child(user.getUsername()).child(book.getOwner() + book.getIsbn());

        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Request borrower = dataSnapshot.getValue(Request.class);
                Intent intent = new Intent(BookDetailsActivity.this, DisplayMapsActivity.class);
                intent.putExtra("latitude", borrower.getLatitude());
                intent.putExtra("longitude", borrower.getLongitude());
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
