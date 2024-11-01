package com.example.wordseek.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wordseek.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    static final String ACCOUNT_NAME = "John Smith";
    static final String USERNAME = "JSmith99";
    static final String PASSWORD = "password123";
    static final String RE_PASSWORD= "password123";

    @Before
    public void setUp() {
        ActivityScenario.launch(LoginActivity.class);
        Log.d("LoginActivityTest", "Database initialized and test data inserted.");
    }


    @Test
    public void testLoginActivity(){
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginActivityLoginButtonWithEmptyFields(){
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginActivityLoginButtonHasCorrectText() {
        onView(withId(R.id.loginBtn)).check(matches(withText("LOGIN")));
    }

    @Test
    public void testLoginActivityLoginButtonIsEnabled() {
        onView(withId(R.id.loginBtn)).check(matches(isEnabled()));
    }

    @Test
    public void testLoginActivityLoginButtonWithFilledUserFields(){

        ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.mainRegisterBtn)).perform(click());
        onView(withId(R.id.register)).check(matches(isDisplayed()));

        onView(withId(R.id.accountNameEdtTxt)).perform(clearText(), typeText(ACCOUNT_NAME), closeSoftKeyboard());
        onView(withId(R.id.userNameEdtTxt)).perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordEdtTxt)).perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.rePasswordEdtTxt)).perform(clearText(), typeText(RE_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(click());

        onView(withId(R.id.main)).check(matches(isDisplayed()));
        onView(withId(R.id.mainLoginBtn)).perform(click());
        onView(withId(R.id.login)).check(matches(isDisplayed()));

        onView(withId(R.id.userNameLoginEdtTxt)).perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEdtTxt)).perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.profile)).check(matches(isDisplayed()));
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.wordseek", appContext.getPackageName());
    }

}