package ca.team21.pagepal.controllers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.team21.pagepal.views.BookFragment.OnListFragmentInteractionListener;
import ca.team21.pagepal.R;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.models.Book;

import java.util.ArrayList;

import static android.view.View.VISIBLE;
import static ca.team21.pagepal.models.Book.BORROWED;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookRecyclerViewAdapter extends RecyclerView.Adapter<MyBookRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Book> mValues;
    private final OnListFragmentInteractionListener mListener;
    private User owner;
    private User borrower;

    public MyBookRecyclerViewAdapter(ArrayList<Book> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mAuthorView.setText(mValues.get(position).getAuthor());
        holder.mStatusView.setText(holder.mItem.getStatus());

        if (holder.mItem.getOwner().equals(FirebaseAuth.getInstance().getUid())) {
            holder.editButton.setVisibility(VISIBLE);
            holder.deleteButton.setVisibility(VISIBLE);
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.editBookInteraction(holder.mItem);
                    }
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        deleteBook(holder.mItem);
                    }
                }
            });
            
            if (holder.mItem.getStatus().equals(BORROWED)) {
                FirebaseDatabase.getInstance().getReference("users")
                        .child(holder.mItem.getBorrower())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                borrower = dataSnapshot.getValue(User.class);
                                holder.usernameView.setVisibility(VISIBLE);
                                holder.usernameView.setText("Borrowed by: " + borrower.getUsername()/*TODO + get borrower */);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println("Borrower not found");
                            }
                        });
            } else {
                holder.usernameView.setVisibility(View.INVISIBLE);
            }
            
        } else {
            FirebaseDatabase.getInstance().getReference("users")
                    .child(holder.mItem.getOwner())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            owner = dataSnapshot.getValue(User.class);
                            holder.usernameView.setVisibility(VISIBLE);
                            holder.usernameView.setText("Owned by: " + owner.getUsername()/*TODO + get owner */);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("Owner not found");
                        }
                    });
        }

        if (holder.usernameView.getVisibility() == VISIBLE) {
            holder.usernameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mItem.getStatus().equals(BORROWED)) {
                        mListener.viewUserInteraction(borrower);
                    } else {
                        mListener.viewUserInteraction(owner);
                    }
                }
            });
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.viewMyBookInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Class representing an item in the list
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mAuthorView;
        public final TextView mStatusView;
        public final TextView usernameView;
        public final Button editButton;
        public final Button deleteButton;
        public Book mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mAuthorView = (TextView) view.findViewById(R.id.author);
            mStatusView = (TextView) view.findViewById(R.id.status);
            usernameView = view.findViewById(R.id.user);
            editButton = view.findViewById(R.id.edit_button);
            deleteButton = view.findViewById(R.id.delete_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    /**
     * Deletes a book from the database
     * @param book Book to be deleted
     */
    private void deleteBook(final Book book) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("books");
        Query bookQuery = dbRef.orderByChild("owner").equalTo(book.getOwner());
        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Book dbBook = data.getValue(Book.class);
                    if (dbBook.getIsbn().equals(book.getIsbn())) {
                        String bookKey = data.getKey();
                        dbRef.child(bookKey).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
