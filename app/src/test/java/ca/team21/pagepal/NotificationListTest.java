package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class NotificationListTest {

    private NotificationList notificationList = new NotificationList();
    private Notification notification1 = new Notification("Notification 1");
    private Notification notification2 = new Notification("Notification 2");
    private ArrayList<Notification> testList = new ArrayList<>();

    @Test
    public void addNotificationTest() {
        assertEquals(testList, notificationList.getNotifications()); // should start as an empty list

        testList.add(notification1);
        notificationList.addNotification(notification1);
        assertEquals(testList, notificationList.getNotifications());

        testList.add(notification2);
        notificationList.addNotification(notification2);
        assertEquals(testList, notificationList.getNotifications());
    }

    public void delNotificationTest() {
        testList.add(notification1);
        notificationList.addNotification(notification1);

        testList.add(notification2);
        notificationList.addNotification(notification2);

        testList.remove(notification1);
        notificationList.delNotification(notification1);

        assertEquals(testList, notificationList.getNotifications());

    }
}
