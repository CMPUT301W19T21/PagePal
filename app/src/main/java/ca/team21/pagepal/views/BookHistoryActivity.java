package ca.team21.pagepal.views;

import android.content.Intent;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.team21.pagepal.BR;
import ca.team21.pagepal.R;
import ca.team21.pagepal.controllers.BookHistoryRecyclerViewAdapter;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.HistoryItem;
import ca.team21.pagepal.models.User;

import static ca.team21.pagepal.views.MainActivity.BOOK_EXTRA;
import static ca.team21.pagepal.views.MainActivity.USER_EXTRA;

public class BookHistoryActivity extends AppCompatActivity {

    User user;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("users");
    private ArrayList<HistoryItem> HistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_history);
        Intent intent = new Intent();
        user = intent.getParcelableExtra(USER_EXTRA);

        HistoryList = user.getHistoryBookList();


        RecyclerView recylerView = findViewById(R.id.list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recylerView.setLayoutManager(layoutManager);

        final RecyclerView.Adapter adapter = new BookHistoryRecyclerViewAdapter(user);
        recylerView.setAdapter(adapter);
        user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.historyBookList){
                    adapter.notifyDataSetChanged();
                }
            }
        });



    }


}
