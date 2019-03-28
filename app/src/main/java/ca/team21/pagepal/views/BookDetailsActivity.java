package ca.team21.pagepal.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    Book book;
    User user;
    DatabaseReference dbRefUsers = FirebaseDatabase.getInstance().getReference().child("users");
    String ownerUsername;
    String ownerLabel;
    int selectedRequesterIndex;
    ArrayList<Request> requesters = new ArrayList<>();
    ArrayList<String> requesterUsernames = new ArrayList<>();

    TextView titleView;
    TextView authorView;
    TextView isbnView;
    TextView statusView;
    TextView descriptionView;
    TextView ownerView;
    TextView requesterLabel;
    Button requestButton;
    Button acceptButton;
    Button declineButton;
    Spinner requesterSpinner;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent intent = getIntent();
        book = intent.getParcelableExtra(BOOK_EXTRA);
        user = intent.getParcelableExtra(USER_EXTRA);

        titleView = findViewById(R.id.title_view);
        authorView = findViewById(R.id.author_view);
        isbnView = findViewById(R.id.isbn_view);
        statusView = findViewById(R.id.status_view);
        descriptionView = findViewById(R.id.description_view);
        ownerView = findViewById(R.id.owner_view);
        requestButton = findViewById(R.id.request_button);
        acceptButton = findViewById(R.id.accept_button);
        declineButton = findViewById(R.id.decline_button);
        requesterSpinner = findViewById(R.id.requester_spinner);
        requesterLabel = findViewById(R.id.requester_label);


        requestButton.setOnClickListener(this);
        acceptButton.setOnClickListener(this);
        declineButton.setOnClickListener(this);
        requesterSpinner.setOnItemSelectedListener(this);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, requesterUsernames);
        requesterSpinner.setAdapter(spinnerAdapter);

        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthor());
        String isbnLabel = "ISBN: " + book.getIsbn();
        isbnView.setText(isbnLabel);
        statusView.setText(book.getStatus().toUpperCase());
        descriptionView.setText(book.getDescription());


        ownerUsername = book.getOwner();

        if (ownerUsername.equals(user.getUsername())) { // if the current user owns this book
            ownerLabel = "You own this book";
            if (book.getStatus().equals(Book.REQUESTED)) {
                acceptButton.setVisibility(View.VISIBLE);
                declineButton.setVisibility(View.VISIBLE);
                requesterSpinner.setVisibility(View.VISIBLE);
                requesterLabel.setVisibility(View.VISIBLE);

                getRequesters();
            }
        } else { // if the current user does not own the book
            ownerLabel = "Owner: " + ownerUsername;
            requestButton.setVisibility(View.VISIBLE);

            if (book.getStatus().equals(Book.AVAILABLE) || book.getStatus().equals(Book.REQUESTED)) { // if the book can be requested
                requestButton.setText("Request Book");
            } else if (book.getStatus().equals(Book.ACCEPTED) || book.getStatus().equals(Book.BORROWED)) { // if the book cannot be requested
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

    private void updateSpinner() {
        requesterUsernames.clear();
        for (Request r: requesters) {
            requesterUsernames.add(r.getRequester());
        }
        spinnerAdapter.notifyDataSetChanged();
    }

    private void sendRequest() {
        Request request = new Request(book.getOwner(), user.getUsername(), book.getIsbn());
        request.writeToDb();

        String message = user.getUsername() + " has requested " + book.getTitle();
        String senderUsername = user.getUsername();
        String recipientUsername = book.getOwner();
        Notification notification = new Notification(message, senderUsername, recipientUsername, book.getIsbn(), book.getOwner());
        notification.writeToDb();

        book.setStatus(Book.REQUESTED);
        book.writeToDb();
    }

    private void decline(Request declined) {
        declined.delete();
        requesters.remove(declined);
        updateSpinner();

        String message = user.getUsername() + " has declined your request for " + book.getTitle();
        Notification notify = new Notification(message, user.getUsername(), declined.getRequester(), book.getIsbn(), user.getUsername());
        notify.writeToDb();
    }

    private void declineRequest() {
        Request requestToDecline = requesters.get(selectedRequesterIndex);
        decline(requestToDecline);

        if (requesters.size() == 0) { // if last remaining request declined
            // update book to be Available
            String owner = requestToDecline.getOwner();
            String bookIsbn = requestToDecline.getBook();
            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(owner).child(bookIsbn);
            bookRef.child("status").setValue(Book.AVAILABLE);
            Toast.makeText(this, "Last remaining request declined", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void acceptRequest() {
        Request requestToAccept = requesters.remove(selectedRequesterIndex);

        for (Request r: requesters) { // decline all other requests
            decline(r);
        }

        String owner = requestToAccept.getOwner();
        String bookIsbn = requestToAccept.getBook();
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(owner).child(owner).child(bookIsbn);
        bookRef.child("status").setValue(Book.ACCEPTED);

        String message = user.getUsername() + " has accepted your request for " + book.getTitle();
        Notification notify = new Notification(message, user.getUsername(), requestToAccept.getRequester(), book.getIsbn(), user.getUsername());
        notify.writeToDb();
    }
}
