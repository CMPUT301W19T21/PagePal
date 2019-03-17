package ca.team21.pagepal;

import org.junit.Test;

import ca.team21.pagepal.models.User;
import ca.team21.pagepal.models.UserAuthenticationInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests user authentication
 */
public class UserAuthTest {
    private User user = new User();
    private String username = "Wayne Gretzky";
    private String password = "hunter2";

    /**
     * Tests logging in
     */
    @Test
    public void loginTest() {
        user.setUsername(username);
        UserAuthenticationInfo authInfo = new UserAuthenticationInfo(user.getUsername(), password);

        assertTrue(authInfo.login(user.getUsername(), password));
    }

    /**
     * Tests logging out
     */
    @Test
    public void logoutTest() {
        UserAuthenticationInfo authInfo = new UserAuthenticationInfo(user.getUsername(), password);

        assertTrue(authInfo.logout(user));
    }
}
