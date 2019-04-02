package ca.team21.pagepal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.views.BookDetailsActivity;


/**
 * Responds to messages sent using firebase functions
 */
public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    public static final String USER_EXTRA = "ca.team21.pagepal.user";
    public static final String BOOK_EXTRA = "ca.team21.pagepal.models.Book";
    User user = User.getInstance();


    /**
     * Called when message is received.
     * Gets information about the book related to the notification, and then creates notification
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Remote Message Received");
        if (remoteMessage.getData() != null) {
            String isbn = remoteMessage.getData().get("isbn");
            final String title = remoteMessage.getData().get("title");
            final String message = remoteMessage.getData().get("body");
            final String owner = remoteMessage.getData().get("owner");


            FirebaseDatabase.getInstance().getReference("books").child(owner).child(isbn)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book book = dataSnapshot.getValue(Book.class);

                            sendMessageNotification(title, message, book, user);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        }

    }


    /**
     * Retrieves new token if token is updated, and writes it in firebase
     *
     * @param token string message token
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }


    /**
     * Stores token in firebase under users
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

    /**
     * Creates push notification
     *
     * @param title   string title of notifications (always set to be PagePal)
     * @param message message of notification
     * @param book    specific book mentioned in notification
     * @param user    the user
     */
    private void sendMessageNotification(String title, String message, Book book, User user) {
        Intent newIntent = new Intent(getApplicationContext(), BookDetailsActivity.class);
        newIntent.putExtra(BOOK_EXTRA, book);
        newIntent.putExtra(USER_EXTRA, user);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // Set up notifications

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channelId")
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.btn_star)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channelId", "PagePal", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("channelId");
            notificationManager.notify(1, builder.build());
        } else {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.btn_star)
                    .addAction(android.R.drawable.btn_star, "See Book", pendingIntent)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);

        }
    }
}




