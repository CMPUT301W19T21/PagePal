package ca.team21.pagepal.Book;

import ca.team21.pagepal.R;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.team21.pagepal.SearchedResultsFragment;
import ca.team21.pagepal.User;

/**
 * Class that implements a recycler view for
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>{

    private final ArrayList<Book> mValues;
    private final SearchedResultsFragment.OnSearchFragmentInteractionListener mListener;

    public SearchRecyclerViewAdapter(ArrayList<Book> items, SearchedResultsFragment.OnSearchFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new SearchRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mAuthorView.setText(mValues.get(position).getAuthor());
        holder.mStatusView.setText(holder.mItem.getStatus());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.viewBookInteraction(holder.mItem, new User());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     *  Viewholder class describes a book view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mAuthorView;
        public final TextView mStatusView;
        public final TextView borrowerView;
        public final Button editButton;
        public final Button deleteButton;
        public Book mItem;

        /**
         * Constructor for the ViewHolder, initializes book representation
         * @param view base view for book representation
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mAuthorView = (TextView) view.findViewById(R.id.author);
            mStatusView = (TextView) view.findViewById(R.id.status);
            borrowerView = view.findViewById(R.id.borrower);
            editButton = view.findViewById(R.id.edit_button);
            deleteButton = view.findViewById(R.id.delete_button);
        }

        /**
         *
         * @return
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

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

            /**
             * 
             * @param databaseError
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
