package ca.team21.pagepal.controllers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.views.BookFragment;

public class ReccomendationRecyclerViewAdapter extends RecyclerView.Adapter<ReccomendationRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Book> mValues;

    private User owner;
    private User borrower;


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommendation,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder = called");


    }

    @Override
    public int getItemCount() {
        return reccommendedBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;

        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.book_image_view);
            title = itemView.findViewById(R.id.my_books_title);

        }

    }


}

