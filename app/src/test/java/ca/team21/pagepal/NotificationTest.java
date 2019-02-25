package ca.team21.pagepal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationTest {

    private Notification notification = new Notification("You have been notified");

    @Test
    public void setMessageTest() {
        assertEquals("You have been notified", notification.getMessage());

        notification.setMessage("User wants your book");

        assertEquals("User wants your book", notification.getMessage());
    }
}
