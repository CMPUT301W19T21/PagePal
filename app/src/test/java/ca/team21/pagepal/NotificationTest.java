package ca.team21.pagepal;

import org.junit.Test;

import ca.team21.pagepal.models.Notification;
import ca.team21.pagepal.models.User;

import static org.junit.Assert.assertEquals;

/**
 * Tests the notification class
 */
public class NotificationTest {

    private Notification notification = new Notification("You have been notified", "Noah", "Tanner", "9000712345", "Tanner");

    /**
     * Tests setting the notification message
     */
    @Test
    public void setMessageTest() {
        assertEquals("You have been notified", notification.getMessage());

        notification.setMessage("User wants your book");

        assertEquals("User wants your book", notification.getMessage());
    }
}
