package ca.team21.pagepal;

import android.support.v4.app.FragmentTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.team21.pagepal.views.BookFragment;
import ca.team21.pagepal.views.EditBookActivity;
import ca.team21.pagepal.views.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MyBooksTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        FragmentTransaction fragmentTransaction =
                activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, BookFragment.newInstance()).commit();

        Intents.init();
    }

    /**
     * Check that the Add button launches the EditBookActivity.
     */
    @Test
    public void testAddBook() {

        onView(withId(R.id.add_book)).perform(click());

        intended(hasComponent(EditBookActivity.class.getName()));
    }

    @After
    public void finishUp() {
        Intents.release();
    }
}
