package ca.team21.pagepal.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.team21.pagepal.R;
import ca.team21.pagepal.models.HistoryItem;
import ca.team21.pagepal.models.User;

public class BookHistoryRecyclerViewAdapter extends RecyclerView.Adapter<BookHistoryRecyclerViewAdapter.ViewHolder>{

    private ArrayList<HistoryItem> mHistory;
    private User owner;

    @Override
    public BookHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_bookhistory,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HistoryItem item = mHistory.get(position);
        holder.BookHisAuthor.setText(item.getBookHisAuthor());
        holder.BookHisTitle.setText(item.getBookHisTitle());
        holder.BookHisGenre.setText(item.getBookHisTitle());
        //holder.BookHisPhoto.set(item.get)
        if (!item.getBookHisPhoto().equals("")){
            byte[] stringToBit = Base64.decode(item.getBookHisPhoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(stringToBit, 0, stringToBit.length);
            holder.BookHisPhoto.setImageBitmap(bitmap);

        }
        holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                owner.removeitem(item);
            }
        });


    }
    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    public BookHistoryRecyclerViewAdapter(User owner){

        this.mHistory = owner.getHistoryBookList();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public View HistoryView;
        public TextView BookHisAuthor;
        public TextView BookHisTitle;
        public TextView BookHisGenre;
        public ImageView BookHisPhoto;
        public Button DeleteButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            HistoryView = itemView;
            BookHisAuthor = (TextView) itemView.findViewById(R.id.BookHisAuthor);
            BookHisTitle = (TextView) itemView.findViewById(R.id.BookHisTitle);
            BookHisGenre = (TextView) itemView.findViewById(R.id.BookHisGenre);
            BookHisPhoto = (ImageView) itemView.findViewById(R.id.BookHisPhoto);
            DeleteButton = (Button) itemView.findViewById(R.id.delete_button);


        }
    }

}
