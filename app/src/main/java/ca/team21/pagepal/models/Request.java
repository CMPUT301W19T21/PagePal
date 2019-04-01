package ca.team21.pagepal.models;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Request {

    private String owner;
    private String requester;
    private Book book;
    boolean ownerReady;
    boolean borrowerReady;

    // didn't use Location class because of errors when reading from Firebase due to Location not having an empty constructor
    private Double latitude;
    private Double longitude;

    public Request() {}

    public Request(String owner, String requester, Book book) {
        this.owner = owner;
        this.requester = requester;
        this.book = book;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.ownerReady = false;
        this.borrowerReady = false;
    }

    public Request(String owner, String requester, Book book, Double latitude, Double longitude) {
        this.owner = owner;
        this.requester = requester;
        this.book = book;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerReady = false;
        this.ownerReady = false;
    }

    /**
     * Set the book's owner.
     * @param owner The username of the owner.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Set the book's requester.
     * @param requester The username of the requester.
     */
    public void setRequester(String requester) {
        this.requester = requester;
    }

    /**
     * Set the book that is being requested.
     * @param book  The ISBN String of the book.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Get the ISBN of the book being requested.
     * @return  The ISBN String.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Get the owner of the book being requested.
     * @return  The username String.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the User requesting the book.
     * @return  The username String.
     */
    public String getRequester() {
        return requester;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isOwnerReady() {
        return ownerReady;
    }

    public void setOwnerReady(boolean ownerReady) {
        this.ownerReady = ownerReady;
    }

    public boolean isBorrowerReady() {
        return borrowerReady;
    }

    public void setBorrowerReady(boolean borrowerReady) {
        this.borrowerReady = borrowerReady;
    }

    /**
     * Write the object to the database.
     */
    public void writeToDb() {
        OnCompleteListener requester = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Log.d("RequesterWrite", "Requester:Successfully written");
                } else {
                    Log.w("RequesterWrite", task.getException());
                }
            }
        };

        OnCompleteListener owner = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Log.d("OwnerWrite", "Owner:Successfully written");
                } else {
                    Log.w("OwnerWrite", task.getException());
                }
            }
        };

        this.writeToDb(requester, owner);
    }

    /**
     * Write the object to the database.
     * @param requesterListener Listens for the result from writing the requester's object.
     * @param ownerListener Listens for the result from writing the owner's object.
     */
    public void writeToDb(OnCompleteListener requesterListener, OnCompleteListener ownerListener) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("requests");

        db.child("requester").child(this.getRequester())
                .child(this.getOwner() + this.getBook().getIsbn())
                .setValue(this).addOnCompleteListener(requesterListener);
        db.child("owner").child(this.getOwner() + this.getBook().getIsbn())
                .child(this.getRequester())
                .setValue(this).addOnCompleteListener(ownerListener);
    }

    /**
     * Delete the object from the database
     */
    public void delete() {
        DatabaseReference.CompletionListener requester =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w("RequesterDelete", databaseError.toException());
                        } else {
                            Log.d("RequesterDelete", "Requester:Successfully Deleted");
                        }
                    }
                };
        DatabaseReference.CompletionListener owner =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w("OwnerDelete", databaseError.toException());
                        } else {
                            Log.d("OwnerDelete", "Owner:Successfully Deleted");
                        }
                    }
                };
        this.delete(requester, owner);
    }
    /**
     * Remove the object from the database.
     * @param requesterListener CompletionListener for the requester's object.
     * @param ownerListener CompletionListener for the owner's object.
     */

    public void delete(DatabaseReference.CompletionListener requesterListener,
                       DatabaseReference.CompletionListener ownerListener) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("requests");

        db.child("requester").child(this.getRequester())
                .child(this.getOwner() + this.getBook().getIsbn())
                .removeValue(requesterListener);
        db.child("owner").child(this.getOwner() + this.getBook().getIsbn())
                .child(this.getRequester())
                .removeValue(ownerListener);
    }
}
