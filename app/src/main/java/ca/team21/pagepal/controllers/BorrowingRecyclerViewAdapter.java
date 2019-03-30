package ca.team21.pagepal.controllers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.views.BorrowingFragment.OnBorrowingInteractionListener;

import static android.view.View.VISIBLE;

public class BorrowingRecyclerViewAdapter extends RecyclerView.Adapter<BorrowingRecyclerViewAdapter.BorrowingViewHolder> {
    private ArrayList<Book> books;
    private OnBorrowingInteractionListener listener;

    public static class BorrowingViewHolder extends RecyclerView.ViewHolder {
        public View bookView;
        public TextView titleView;
        public TextView authorView;
        public TextView ownerView;
        public TextView statusView;

        public BorrowingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookView = itemView;
            titleView = itemView.findViewById(R.id.title);
            authorView = itemView.findViewById(R.id.author);
            ownerView = itemView.findViewById(R.id.user);
            statusView = itemView.findViewById(R.id.status);
        }
    }

    public BorrowingRecyclerViewAdapter(ArrayList<Book> books, OnBorrowingInteractionListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BorrowingRecyclerViewAdapter.BorrowingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_book, viewGroup, false);

        BorrowingViewHolder viewHolder = new BorrowingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowingViewHolder viewHolder, int i) {
        final Book book = books.get(i);
        viewHolder.titleView.setText(book.getTitle());
        viewHolder.authorView.setText(book.getAuthor());
        viewHolder.statusView.setText(book.getStatus());
        viewHolder.ownerView.setText("Owned by: " + book.getOwner());
        viewHolder.ownerView.setVisibility(VISIBLE);

        viewHolder.ownerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.viewUserInteraction(book.getOwner());
            }
        });

        viewHolder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.viewBookInteraction(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
