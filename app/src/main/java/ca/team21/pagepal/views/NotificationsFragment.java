package ca.team21.pagepal.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.team21.pagepal.R;
import ca.team21.pagepal.controllers.NotificationRecyclerViewAdapter;
import ca.team21.pagepal.controllers.SearchRecyclerViewAdapter;
import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.Loan;
import ca.team21.pagepal.models.Notification;
import ca.team21.pagepal.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNotificationsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NotificationRecyclerViewAdapter adapter;
    private OnNotificationsInteractionListener mListener;
    private final User user = User.getInstance();

    private ArrayList<Notification> notifications = new ArrayList<>();
    private Query notificationsQuery;
    private ValueEventListener notificationListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            notifications.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Notification notification = data.getValue(Notification.class);
                notifications.add(notification);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
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

        notificationsQuery = FirebaseDatabase.getInstance().getReference().child("notifications")
                .child(user.getUsername());
        notificationsQuery.addValueEventListener(notificationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onNotificationsInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationsInteractionListener) {
            mListener = (OnNotificationsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotificationsInteractionListener");
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
    public interface OnNotificationsInteractionListener {
        // TODO: Update argument type and name
        void onNotificationsInteraction();
    }
}
