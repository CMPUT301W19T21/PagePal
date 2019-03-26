package ca.team21.pagepal.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Loan {

    private String book;
    private String owner;
    private String borrower;

    public Loan() {
        /*
        Empty constructor required for Firebase
        */
    }

    public Loan(String book, String owner, String borrower) {
        this.book = book;
        this.owner = owner;
        this.borrower = borrower;
    }

    /**
     * Set the book being loaned.
     * @param book  The ISBN of the book.
     */
    public void setBook(String book) {
        this.book = book;
    }

    /**
     * Set the owner of the book being loaned.
     * @param owner The username of the owner.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Set the borrower of the book being loaned.
     * @param borrower  The username of the borrower.
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Get the book being loaned.
     * @return  The ISBN of the book.
     */
    public String getBook() {
        return book;
    }

    /**
     * Get the owner of the book being loaned.
     * @return  The username of the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the borrower of the book being loaned.
     * @return  The username of the borrower.
     */
    public String getBorrower() {
        return borrower;
    }

    /**
     * Write the object to the database.
     */
    public void writeToDb() {
        OnCompleteListener borrower = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Log.d("BorrowerWrite", "Borrower:Successfully written");
                } else {
                    Log.w("BorrowerWrite", task.getException());
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

        this.writeToDb(borrower, owner);
    }

    /**
     * Write the object to the database.
     * @param borrowerListener Listens for the result from writing the borrower's object.
     * @param ownerListener Listens for the result from writing the owner's object.
     */
    public void writeToDb(OnCompleteListener borrowerListener, OnCompleteListener ownerListener) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("loans");

        db.child("borrower").child(this.getBorrower())
                .child(this.getBook())
                .setValue(this).addOnCompleteListener(borrowerListener);
        db.child("owner").child(this.getOwner())
                .child(this.getBook())
                .setValue(this).addOnCompleteListener(ownerListener);
    }

    /**
     * Delete the object from the database
     */
    public void delete() {
        DatabaseReference.CompletionListener borrower =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.w("BorrowerDelete", databaseError.toException());
                        } else {
                            Log.d("BorrowerDelete", "Borrower:Successfully Deleted");
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
        this.delete(borrower, owner);
    }
    /**
     * Remove the object from the database.
     * @param borrowerListener CompletionListener for the borrower's object.
     * @param ownerListener CompletionListener for the owner's object.
     */

    public void delete(DatabaseReference.CompletionListener borrowerListener,
                       DatabaseReference.CompletionListener ownerListener) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("loans");

        db.child("borrower").child(this.getBorrower())
                .child(this.getBook())
                .removeValue(borrowerListener);
        db.child("owner").child(this.getOwner())
                .child(this.getBook())
                .removeValue(ownerListener);
    }
}
