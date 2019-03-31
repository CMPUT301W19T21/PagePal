package ca.team21.pagepal;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.views.BookDetailsActivity;
//import com.google.firebase.quickstart.fcm.R;

//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkManager;


public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    public static final String USER_EXTRA = "ca.team21.pagepal.user";
    public static final String BOOK_EXTRA = "ca.team21.pagepal.models.Book";
    User user = User.getInstance();


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getNotification() != null) {

            String isbn = remoteMessage.getData().get("isbn");
            final String title = remoteMessage.getData().get("title");
            final String message = remoteMessage.getData().get("body");
            final String owner = remoteMessage.getData().get("owner");


            FirebaseDatabase.getInstance().getReference("books").child(owner).child(isbn)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book book = dataSnapshot.getValue(Book.class);

                            sendMessageNotification( title,  message,  book,  user);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



        }

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId:failure", task.getException());
                            return;
                        } else {
                            Log.d(TAG, "getInstanceId:success");
                            String token = task.getResult().getToken();
                            String userName = user.getUsername();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(userName)
                                    .child("messagingToken").setValue(token);
                        }


                    }
                });
    }
    private void sendMessageNotification(String title, String message, Book book, User user) {
        Intent newIntent = new Intent(getApplicationContext(), BookDetailsActivity.class);
        newIntent.putExtra(BOOK_EXTRA, book);
        newIntent.putExtra(USER_EXTRA, user);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, newIntent, 0);
        // Set up notifications
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.btn_star, "See Book", pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}

