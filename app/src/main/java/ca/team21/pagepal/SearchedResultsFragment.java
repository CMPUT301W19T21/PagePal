package ca.team21.pagepal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.team21.pagepal.Book.Book;
import ca.team21.pagepal.Book.MyBookRecyclerViewAdapter;
import ca.team21.pagepal.Book.SearchRecyclerViewAdapter;


/**
 * A fragment representing a list of Items (i.e. books that are available to be borrowed).
 *
 * Activities containing this fragment MUST implement the {@link OnSearchFragmentInteractionListener}
 *  * interface.
 */
public class SearchedResultsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private ArrayList<Book> mParam1;

    private OnSearchFragmentInteractionListener mListener;

    private SearchRecyclerViewAdapter adapter;
    private ArrayList<Book> mBookList;
    public SearchedResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Empty constructor that creates a new instance of the SearchedResultsFragment
     * @param param1  an ArrayList of books to be displayed.
     * @return A new instance of fragment SearchedResultsFragment.
     */

    public static SearchedResultsFragment newInstance(ArrayList<Book> param1) {
        SearchedResultsFragment fragment = new SearchedResultsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *  Activity is initialized
     * @param savedInstanceState contains information about past activity instances
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBookList = getArguments().getParcelableArrayList(ARG_PARAM1);

        }
    }

    /**
     * SearchedResultsFragment instantiates its UI view
     * @param inflater  instantiates xml into view objects
     * @param container a ViewGroup, which is the base class for layouts
     * @param savedInstanceState contains information about past activity instances
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searched_results, container, false);
        View listView = view.findViewById(R.id.list);

        //Set the adaptor
        if (listView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) listView;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            adapter = new SearchRecyclerViewAdapter(mBookList, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    /**
     * Called once the fragment is associated with SearchActivity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFragmentInteractionListener) {
            mListener = (OnSearchFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * Called immediately before this fragment is no longer associated with SearchActivity
     */
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
     */
    public interface OnSearchFragmentInteractionListener {
       void viewBookInteraction(Book book, User user) ;
       void viewUserInteraction(User user);
    }
}
