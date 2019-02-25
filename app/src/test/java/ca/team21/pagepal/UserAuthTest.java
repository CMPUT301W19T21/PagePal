package ca.team21.pagepal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserAuthTest {
    private User user = new User();
    private String username = "Wayne Gretzky";
    private String password = "hunter2";

    @Test
    public void loginTest() {
        user.setUsername(username);
        UserAuthenticationInfo authInfo = new UserAuthenticationInfo(user.getUsername(), password);

        assertTrue(authInfo.login(user.getUsername(), password));
    }

    @Test
    public void logoutTest() {
        UserAuthenticationInfo authInfo = new UserAuthenticationInfo(user.getUsername(), password);

        assertTrue(authInfo.logout(user));
    }
}
