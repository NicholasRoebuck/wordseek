package com.example.wordseek.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.wordseek.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

   @Before
   public void setUp(){
       ActivityScenario.launch(MainActivity.class);
   }

   @Test
   public void testRegisterButton(){
       onView(ViewMatchers.withId(R.id.mainRegisterBtn)).check(matches(isDisplayed()));
   }

    @Test
    public void testRegisterButtonHasCorrectText() {
        onView(withId(R.id.mainRegisterBtn)).check(matches(withText("REGISTER")));
    }

    @Test
    public void testLoginButton(){
        onView(ViewMatchers.withId(R.id.mainLoginBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginButtonHasCorrectText() {
        // Check that the button has the expected text
        onView(withId(R.id.mainLoginBtn)).check(matches(withText("LOGIN")));
    }

    @Test
    public void testLoginButtonIsEnabled() {
        // Check that the button has the expected text
        onView(withId(R.id.mainLoginBtn)).check(matches(isEnabled()));
    }

    @Test
    public void testRegisterButtonIsEnabled() {
        // Check that the button has the expected text
        onView(withId(R.id.mainRegisterBtn)).check(matches(isEnabled()));
    }

   @Test
   public void testRegisterButtonClick(){
       onView(withId(R.id.mainRegisterBtn)).perform(click());
   }

    @Test
    public void testRegisterButtonClickNavigatesToRegisterPage(){
        onView(withId(R.id.mainRegisterBtn)).perform(click());
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        onView(withId(R.id.registerBannerTxt)).check(matches(withText("Register")));
    }

    @Test
    public void testLoginButtonClick(){
        onView(withId(R.id.mainLoginBtn)).perform(click());
    }


    @Test
    public void testLoginButtonClickNavigatesToLoginPage(){
        onView(withId(R.id.mainLoginBtn)).perform(click());
        onView(withId(R.id.login)).check(matches(isDisplayed()));
        onView(withId(R.id.loginBannerTxt)).check(matches(withText("Login Page")));
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.wordseek", appContext.getPackageName());
    }


}