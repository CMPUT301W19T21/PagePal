package ca.team21.pagepal;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.team21.pagepal.views.LoginActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private String email = "test@email.com";
    private String password = "hunter2";

    @Rule
    public ActivityTestRule<LoginActivity> activityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void testEnterEmail() {
        onView(withId(R.id.email)).perform(click(), typeText(email), closeSoftKeyboard());

        onView(withId(R.id.email)).check(matches(withText(email)));
    }

    @Test
    public void testEnterPassword() {
        onView(withId(R.id.password)).perform(click(), typeText(password), closeSoftKeyboard());

        onView(withId(R.id.password)).check(matches(withText(password)));
    }
}
