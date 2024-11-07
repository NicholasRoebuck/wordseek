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

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wordseek.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterActivityTest {
    static final String ACCOUNT_NAME = "John Phather";
    static final String USERNAME = "JohnPha9001";
    static final String PASSWORD = "password123";
    static final String RE_PASSWORD= "password123";


    @Before
    public void setUp(){
        ActivityScenario.launch(RegisterActivity.class);
    }

    @Test
    public void testRegisterSubmitButton(){
        onView(ViewMatchers.withId(R.id.submitBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterSubmitButtonHasCorrectText() {
        onView(withId(R.id.submitBtn)).check(matches(withText("SUBMIT")));
    }

    @Test
    public void testRegisterLoginButton(){
        onView(ViewMatchers.withId(R.id.registerLoginBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterLoginButtonHasCorrectText() {
        // Check that the button has the expected text
        onView(withId(R.id.registerLoginBtn)).check(matches(withText("LOGIN")));
    }

    @Test
    public void testRegisterLoginButtonIsEnabled() {
        // Check that the button has the expected text
        onView(withId(R.id.registerLoginBtn)).check(matches(isEnabled()));
    }

    @Test
    public void testRegisterSubmitButtonIsEnabled() {
        // Check that the button has the expected text
        onView(withId(R.id.submitBtn)).check(matches(isEnabled()));
    }

    @Test
    public void testRegisterSubmitButtonClick(){
        onView(withId(R.id.submitBtn)).perform(click());
    }

    @Test
    public void testRegisterLoginButtonClickNavigatesToLoginPage(){
        onView(withId(R.id.registerLoginBtn)).perform(click());
        onView(withId(R.id.main)).check(matches(isDisplayed()));
        onView(withId(R.id.wordseekWelcomeBanner)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterLoginButtonClick(){
        onView(withId(R.id.registerLoginBtn)).perform(click());
    }


    @Test
    public void testRegisterSubmitButtonClickDoesNotSubmitEmptyUserFields(){
        onView(withId(R.id.submitBtn)).perform(click());
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        onView(withId(R.id.registerBannerTxt)).check(matches(withText("Register")));
    }

    @Test
    public void testRegisterSubmitButtonWithFilledUserFields(){
        onView(withId(R.id.accountNameEdtTxt)).perform(clearText(), typeText(ACCOUNT_NAME), closeSoftKeyboard());
        onView(withId(R.id.userNameEdtTxt)).perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordEdtTxt)).perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.rePasswordEdtTxt)).perform(clearText(), typeText(RE_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(click());
        onView(withId(R.id.main)).check(matches(isDisplayed()));
        onView(withId(R.id.wordseekWelcomeBanner)).check(matches(isDisplayed()));
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.wordseek", appContext.getPackageName());
    }
}