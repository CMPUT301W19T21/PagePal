package ca.team21.pagepal.controllers;


import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.Notification;
import ca.team21.pagepal.views.NotificationsFragment;
import ca.team21.pagepal.R;

import static android.view.View.VISIBLE;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder> {
    private ArrayList<Notification> notifications;
    private NotificationsFragment.OnNotificationsInteractionListener listener;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public View notificationView;
        public final TextView messageView;
        public final TextView timeStampView;
        public final Button deleteButton;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationView = itemView;
            messageView = itemView.findViewById(R.id.author);
            timeStampView = itemView.findViewById(R.id.user);
            deleteButton = itemView.findViewById(R.id.delete_button);

        }
    }

    public NotificationRecyclerViewAdapter(ArrayList<Notification> notifications, NotificationsFragment.OnNotificationsInteractionListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_notifications, viewGroup, false);

        NotificationRecyclerViewAdapter.NotificationViewHolder viewHolder = new NotificationRecyclerViewAdapter.NotificationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerViewAdapter.NotificationViewHolder viewHolder, int i) {
        final Notification notification = notifications.get(i);
        viewHolder.messageView.setText(notification.getMessage());
        viewHolder.timeStampView.setText(notification.getTimestamp().toString());

        if (notification.getUnread().equals(true)) {
            viewHolder.messageView.setTypeface(viewHolder.messageView.getTypeface(), Typeface.BOLD);

        }
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    deleteNotification(notification);
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }


    private void deleteNotification(Notification notification) {
        notification.delete();
    }

}
