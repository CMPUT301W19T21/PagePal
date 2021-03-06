package ca.team21.pagepal.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.team21.pagepal.controllers.MyBookRecyclerViewAdapter;
import ca.team21.pagepal.R;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.models.Book;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_BOOKLIST = "bookList";
    private final User user = User.getInstance();
    // Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Book> mBookList;
    private OnListFragmentInteractionListener mListener;

    private MyBookRecyclerViewAdapter adapter;

    private String filter = "All";
    private Query ownedBooksQuery;
    ValueEventListener bookListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mBookList.clear();
            for (DataSnapshot data: dataSnapshot.getChildren()) {
                Book book = data.getValue(Book.class);
                if (filter.equals("All")) {
                    mBookList.add(book);
                } else if (book.getStatus().equals(filter)) {
                    mBookList.add(book);
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookFragment() {
    }

    // Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookFragment newInstance(/*int columnCount, ArrayList<Book> bookList*/) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        /*
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelableArrayList(ARG_BOOKLIST, bookList);
        */
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookList = new ArrayList<Book>();

        /*
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mBookList = getArguments().getParcelableArrayList(ARG_BOOKLIST);
        }
        */

        ownedBooksQuery = FirebaseDatabase.getInstance().getReference().child("books")
                .child(user.getUsername());
        ownedBooksQuery.addValueEventListener(bookListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        View listView = view.findViewById(R.id.list);

        Spinner spinner = view.findViewById(R.id.filter_selector);
        spinner.setOnItemSelectedListener(this);

        Button addButton = view.findViewById(R.id.add_book);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(new Book());
            }
        });

        // Set the adapter
        if (listView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) listView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyBookRecyclerViewAdapter(mBookList, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onButtonPressed(Book book) {
        if (mListener != null) {
            mListener.onBookListAddButtonClick();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filter = parent.getItemAtPosition(position).toString();
        ownedBooksQuery.addListenerForSingleValueEvent(bookListener);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onBookListAddButtonClick();
        void viewMyBookInteraction(Book book);
        void editBookInteraction(Book book);
        void viewUserInteraction(String user);
    }
}
