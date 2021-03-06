package ca.team21.pagepal.views;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ca.team21.pagepal.BR;
import ca.team21.pagepal.R;
import ca.team21.pagepal.controllers.BorrowingRecyclerViewAdapter;
import ca.team21.pagepal.controllers.ReccomendationRecyclerViewAdapter;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.HistoryItem;
import ca.team21.pagepal.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Book> ReccomendationList;
    private String current_user;

    private RecyclerView.Adapter adapter;

    private OnHomeInteractionListener mListener;

    private User user;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        /*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        */
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        user = User.getInstance();

        ReccomendationList = new ArrayList<Book>();

        if (user.getUid().equals("")) {
            user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (propertyId == BR._all) {
                        generateList();
                    }
                }
            });
        } else {
            generateList();
        }
    }


    public void generateList() {
        current_user = user.getUsername();
        final ArrayList<HistoryItem> userHistory = user.getHistoryBookList();
        final ArrayList<String> GenreDict = user.getDictionary();
            Query query = FirebaseDatabase.getInstance().getReference("books");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot allBooks : dataSnapshot.getChildren()) {
                                for (DataSnapshot item : allBooks.getChildren()) {
                                    Book book = item.getValue(Book.class);
                                    HistoryItem historyItem = new HistoryItem(book.getAuthor(), book.getTitle(), book.getGenre(), book.getPhoto());
                                    if (!(book.getOwner().equals(current_user))
                                            && !userHistory.contains(historyItem)
                                            && (book.getStatus().equals(Book.AVAILABLE) || book.getStatus().equals(Book.REQUESTED))) {
                                        for (final String value : GenreDict.subList(0, GenreDict.size() < 3 ? GenreDict.size() : 2)) {
                                            ReccomendationList.add(book);
                                        }
                                    }
                                }
                            }
                            if (ReccomendationList.size() < 5) {
                                Random random = new Random();
                                ArrayList<Book> books = new ArrayList<>();
                                for (DataSnapshot allBooks : dataSnapshot.getChildren()) {
                                    for (DataSnapshot item : allBooks.getChildren()) {
                                        books.add(item.getValue(Book.class));
                                    }
                                }
                                while(ReccomendationList.size() < 5) {
                                    Book fillBook = books.get(random.nextInt(books.size()));
                                    if(!ReccomendationList.contains(fillBook)) { ReccomendationList.add(fillBook); }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
    }


    /**
     * TODO: Update comments
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Configure Search Widget
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.findBooks);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);


        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ReccomendationRecyclerViewAdapter(ReccomendationList, mListener);
        recyclerView.setAdapter(adapter);
        return rootView;    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeInteractionListener) {
            mListener = (OnHomeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnHomeInteractionListener {
        // TODO: Update argument type and name
        void viewBookInteraction(Book book);
    }
}
