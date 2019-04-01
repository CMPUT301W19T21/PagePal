package ca.team21.pagepal.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import ca.team21.pagepal.views.BookDetailsActivity;
import ca.team21.pagepal.views.BookFragment;
import ca.team21.pagepal.views.HomeFragment;
import ca.team21.pagepal.views.MainActivity;

public class ReccomendationRecyclerViewAdapter extends RecyclerView.Adapter<ReccomendationRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Book> mValues;
    private HomeFragment.OnHomeInteractionListener mlistener;
    public ReccomendationRecyclerViewAdapter(ArrayList<Book> values, HomeFragment.OnHomeInteractionListener listener) {
        mValues = values;
        mlistener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recommendation,viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Book book_item = mValues.get(position);


        if (!book_item.getPhoto().equals("")){
            byte[] stringToBit = Base64.decode(book_item.getPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(stringToBit, 0, stringToBit.length);
            holder.image.setImageBitmap(bitmap);
        }
        else{
            holder.image.setVisibility(View.INVISIBLE);
            holder.author.setText(book_item.getAuthor());
            holder.title.setText(book_item.getTitle());
            holder.author.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.VISIBLE);



        }
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.viewBookInteraction(book_item);
            }
        });

    }

    @Override
    public int getItemCount() {return mValues.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView author;
        View book;
        ImageView icon;

        public ViewHolder(View itemView){
            super(itemView);
            book = itemView;
            image = itemView.findViewById(R.id.ReccPhoto);
            title = itemView.findViewById(R.id.ReccTitle);
            author = itemView.findViewById(R.id.ReccAuthor);
            icon = itemView.findViewById(R.id.ReccIcon);

        }

    }


}

