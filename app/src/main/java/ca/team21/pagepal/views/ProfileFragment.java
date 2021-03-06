package ca.team21.pagepal.views;

import android.content.Context;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.team21.pagepal.BR;
import ca.team21.pagepal.R;
import ca.team21.pagepal.models.User;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // Parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "user";
    private static final String ARG_USERNAME = "username";
    // Parameters
    private User mUser;
    private String authUid;

    private OnProfileInteractionListener mListener;

    private TextView usernameView;
    private TextView emailView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user The user to load
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, user);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        String username = args.getString(ARG_USERNAME);
        if (username != null) {
            mUser = new User();
            FirebaseDatabase.getInstance().getReference("users").child(username)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User temp = dataSnapshot.getValue(User.class);
                    mUser.setUid(temp.getUid());
                    mUser.setUsername(temp.getUsername());
                    mUser.setEmail(temp.getEmail());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            mUser = args.getParcelable(ARG_USER);
        }

        if (context instanceof OnProfileInteractionListener) {
            mListener = (OnProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeInteractionListener");
        }
        authUid = FirebaseAuth.getInstance().getUid();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String uid = mUser.getUid();

        if (authUid.equals(uid)) {
            Button editButton = view.findViewById(R.id.edit_button);
            editButton.setVisibility(VISIBLE);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onEditUser(mUser);
                }
            });

            Button historyButton = view.findViewById(R.id.history_button);
            historyButton.setVisibility(VISIBLE);
            historyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mListener.onViewHistoryPressed(mUser);
                }

            });

        }


        usernameView = view.findViewById(R.id.username);
        emailView = view.findViewById(R.id.email);

        usernameView.setText(mUser.getUsername());
        emailView.setText(mUser.getEmail());

        mUser.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                switch (propertyId) {
                    case BR.username:
                        usernameView.setText(mUser.getUsername());
                    case BR.email:
                        emailView.setText(mUser.getEmail());
                }
            }
        });

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
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
    public interface OnProfileInteractionListener {
        void onEditUser(User user);
        void onViewHistoryPressed(User user);
    }
}
