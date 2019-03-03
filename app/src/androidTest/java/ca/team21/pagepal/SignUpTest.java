package ca.team21.pagepal;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.WildcardType;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class SignUpTest {
    private String name = "sign up test user";
    private String email = "sign@up.test";
    private String password = "hunter2";

    private String invalidEmail = "tryemail.com";
    private String invalidPassword = "pass";
    private String misMatchPassword = "hunter3";

    @Rule
    public ActivityTestRule<SignUpActivity> activityRule =
            new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void testInvalidEmail() {
        typeInput(name, invalidEmail, password, password);

        onView(withId(R.id.email_input))
                .check(matches(hasErrorText("This email address is invalid")));
    }

    @Test
    public void testEmptyEmail() {
        typeInput(name, "", password, password);

        onView(withId(R.id.email_input))
                .check(matches(hasErrorText("This field is required")));
    }

    @Test
    public void testEmptyPassword() {
        typeInput(name, email, "", "");

        onView(withId(R.id.password_input))
                .check(matches(hasErrorText("This field is required")));
    }

    @Test
    public void testShortPassword() {
        typeInput(name, email, invalidPassword, invalidPassword);

        onView(withId(R.id.password_input))
                .check(matches(hasErrorText("This password is too short")));
    }

    @Test
    public void testDifferentPasswords() {
        typeInput(name, email, password, misMatchPassword);

        onView(withId(R.id.confirm_password_input))
                .check(matches(hasErrorText("Passwords don't match!")));
    }

    private void typeInput(String name, String email, String password, String confirmPassword) {
        onView(withId(R.id.name_input)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.email_input)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password_input)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.confirm_password_input)).perform(typeText(confirmPassword), closeSoftKeyboard());

        onView(withId(R.id.sign_up_button)).perform(click());
    }

}
