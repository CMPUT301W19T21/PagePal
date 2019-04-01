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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.team21.pagepal.R;
import ca.team21.pagepal.controllers.NotificationRecyclerViewAdapter;
import ca.team21.pagepal.models.Book;
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


    private NotificationRecyclerViewAdapter adapter;
    private OnNotificationsInteractionListener mListener;
    private final User user = User.getInstance();
    private int mColumnCount = 1;

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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        View listView = view.findViewById(R.id.list);

        if (listView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) listView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new NotificationRecyclerViewAdapter(notifications, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;


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
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onNotificationsInteraction();
        }
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
        void viewBookInteraction(Book book);
    }
}
