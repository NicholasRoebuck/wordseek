package com.example.wordseek.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotActivated;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.annotation.SuppressLint;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.wordseek.R;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileActivityTest {
    static final String ACCOUNT_NAME = "John Smith";
    static final String USERNAME = "JSmith99";
    static final String PASSWORD = "password123";
    static final String RE_PASSWORD= "password123";

    @BeforeClass
    public static void setUpOnce() throws Exception {
        ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.mainRegisterBtn)).perform(click());
        onView(withId(R.id.register)).check(matches(isDisplayed()));

        onView(withId(R.id.accountNameEdtTxt))
                .perform(clearText(), typeText(ACCOUNT_NAME), closeSoftKeyboard());
        onView(withId(R.id.userNameEdtTxt))
                .perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordEdtTxt))
                .perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.rePasswordEdtTxt))
                .perform(clearText(), typeText(RE_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(click());
    }


    @Test
    public void loginAndViewProfileActivityUsernameAndPasswordAndAccountOwner(){
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.userNameLoginEdtTxt))
                .perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEdtTxt))
                .perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.profile)).check(matches(isDisplayed()));

        onView(withId(R.id.profileAccountName)).check(matches(isDisplayed()))
                .check(matches(withText("Account Owner: ")));
        onView(withId(R.id.profileUsername)).check(matches(isDisplayed()))
                .check(matches(withText("Username: ")));
        onView(withId(R.id.textView2)).check(matches(isDisplayed()))
                .check(matches(withText("Password: ")));

        onView(withId(R.id.accountName)).check(matches(isDisplayed()))
                .check(matches(withText(ACCOUNT_NAME)));
        onView(withId(R.id.username)).check(matches(isDisplayed()))
                .check(matches(withText(USERNAME)));
        onView(withId(R.id.password)).check(matches(isDisplayed()))
                .check(matches(withText(hashPass(PASSWORD))));
    }

    private static String hashPass(String pass) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(pass.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void loginAndViewProfileActivitySelectNewWordButton(){
        ActivityScenario.launch(LoginActivity.class);

        onView(withId(R.id.userNameLoginEdtTxt))
                .perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEdtTxt))
                .perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.profile)).check(matches(isDisplayed()));

        onView(withId(R.id.newWordBtn)).perform(click());

        onView(withId(R.id.wordActivity)).check(matches(isDisplayed()));

    }

}