package ca.team21.pagepal;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.team21.pagepal.views.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void finishUp() {
        Intents.release();
    }

    /**
     * Test that the search bar is present.
     * TODO change to test that input can be added to the search bar.
     */
    @Test
    public void testSearchBar() {
        onView(withId(R.id.search_label)).check(matches(isDisplayed()));
    }

    /**
     * Test that BorrowingFragment can be viewed.
     * TODO change the check view when the borrowing list is implemented properly.
     */
    @Test
    public void testViewBorrowing() {
        onView(withId(R.id.borrowed_books)).perform(click());

        onView(withId(R.id.borrowing_text)).check(matches(isDisplayed()));
    }

    /**
     * Test that my books can be viewed.
     */
    @Test
    public void testViewMyBooks() {
        onView(withId(R.id.owned_books)).perform(click());

        onView(withId(R.id.my_books_title)).check(matches(isDisplayed()));
    }

    /**
     * Test that my notifications can be viewed.
     * TODO change the check view when notifications is implemented properly.
     */
    @Test
    public void testViewNotifications() {
        onView(withId(R.id.navigation_notifications)).perform(click());

        onView(withId(R.id.notifications_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testViewProfile() {
        onView(withId(R.id.navigation_profile)).perform(click());

        onView(withId(R.id.username)).check(matches(isDisplayed()));
    }
}
