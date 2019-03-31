package ca.team21.pagepal.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.team21.pagepal.R;
import ca.team21.pagepal.controllers.BorrowingRecyclerViewAdapter;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.Loan;
import ca.team21.pagepal.models.Request;
import ca.team21.pagepal.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnBorrowingInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BorrowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BorrowingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private OnBorrowingInteractionListener interactionListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String filter = "Borrowing";

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Loan> loans = new ArrayList<>();
    private ArrayList<Request> requests = new ArrayList<>();
    private Query loanQuery;
    private Query requestQuery;
    private ValueEventListener loanListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            books.clear();
            for (DataSnapshot data: dataSnapshot.getChildren()) {
                Loan loan = data.getValue(Loan.class);
                Book book = loan.getBook();
                books.add(book);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private ValueEventListener requestListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            books.clear();
            for (DataSnapshot data: dataSnapshot.getChildren()) {
                Request request = data.getValue(Request.class);
                Book book = request.getBook();
                books.add(book);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public BorrowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BorrowingFragment.
     */
    public static BorrowingFragment newInstance() {
        BorrowingFragment fragment = new BorrowingFragment();
        // Set arguments here
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get arguments here

        User user = User.getInstance();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        loanQuery = db.child("loans").child("borrower").child(user.getUsername());
        requestQuery = db.child("requests").child("requester").child(user.getUsername());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrowing, container, false);
        recyclerView = view.findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BorrowingRecyclerViewAdapter(books, interactionListener);
        recyclerView.setAdapter(adapter);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBorrowingInteractionListener) {
            interactionListener = (OnBorrowingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBorrowingInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filter = parent.getItemAtPosition(position).toString();
        if (filter.equals("Borrowing")) {
            loanQuery.addListenerForSingleValueEvent(loanListener);
        } else {
            requestQuery.addListenerForSingleValueEvent(requestListener);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBorrowingInteractionListener {
        void viewUserInteraction(String user);
        void viewBookInteraction(Book book);
    }
}
