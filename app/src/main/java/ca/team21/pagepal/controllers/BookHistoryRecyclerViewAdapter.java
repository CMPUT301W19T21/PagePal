package ca.team21.pagepal.controllers;

import android.app.LauncherActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.core.Context;

import java.util.List;

import ca.team21.pagepal.Book;
import ca.team21.pagepal.R;
import ca.team21.pagepal.models.BookHistoryList;

public class BookHistoryRecyclerViewAdapter extends RecyclerView.Adapter<BookHistoryRecyclerViewAdapter.ViewHolder>{

    private List<BookHistoryList> listItems;
    private Context context;

    public BookHistoryRecyclerViewAdapter(List<BookHistoryList>, Context context){
        this.listItems = listItems;
        this.context = context;


    }


    @Override
    public BookHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_bookhistory,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHistoryRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        BookHistoryList listItem = listItems.get(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView BookHisAuthor;
        public TextView BookHisTitle;
        public TextView BookHisGenre;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            BookHisAuthor = (TextView) itemView.findViewById(R.id.BookHisAuthor);
            BookHisTitle = (TextView) itemView.findViewById(R.id.BookHisTitle);
            BookHisGenre = (TextView) itemView.findViewById(R.id.BookHisGenre);


        }
    }

}
