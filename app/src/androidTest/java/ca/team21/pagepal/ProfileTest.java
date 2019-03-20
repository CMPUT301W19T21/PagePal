package ca.team21.pagepal;

import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.team21.pagepal.models.User;
import ca.team21.pagepal.views.MainActivity;
import ca.team21.pagepal.views.ProfileFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ProfileTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private User user;

    @Before
    public void setUp() {
        user = new User("test", "test@email.com");

        FragmentTransaction fragmentTransaction =
                activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, ProfileFragment.newInstance(user)).commit();
    }

    @Test
    public void testUserName() {
        onView(withId(R.id.username)).check(matches(withText(user.getUsername())));
    }

    /**
     * TODO add test for when edit button is clicked.
     */
    @Test
    public void testEditUser() {

    }
}
