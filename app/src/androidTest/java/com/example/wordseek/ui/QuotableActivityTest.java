package com.example.wordseek.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.content.Intent;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import com.example.wordseek.R;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuotableActivityTest {

    @Rule
    public ActivityScenarioRule<QuotableActivity> activityScenarioRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), QuotableActivity.class) {
                {
                    Word fakeWord = new Word("abolish");
                    String fakeDefinition = "Some definition.......";
                    User user = new User("JSmith99", "password123");


                    putExtra("username",user.getUserName());
                    putExtra("word", fakeWord);
                    putExtra("definition", fakeDefinition);
                }
            });

    @After
    public void tearDown() throws Exception{
        Intents.release();
    }

    @Before
    public void setUp()throws Exception{
        Intents.init();

    }

    @Test
    public void viewQuotableActivityWord() throws Exception{
        onView(withId(R.id.quotableWord)).check(matches(isDisplayed()));
    }

    @Test
    public void viewQuotableActivityUserName() throws Exception{
        onView(withId(R.id.quotableUsername)).check(matches(isDisplayed()));
    }

    @Test
    public void viewQuotableActivityWordDefinition()throws Exception{
        onView(withId(R.id.quotableRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void createQuotableActivityQuotable()throws Exception{

        onView(withId(R.id.quoteEdtTxt))
                .perform(clearText(),
                        typeText("Some new quote to be added with the word abolish"),
                        closeSoftKeyboard());
        onView(withId(R.id.quoteSubmitBtn)).perform(click());
        onView(withId(R.id.quotableRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.quotableRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }

}

