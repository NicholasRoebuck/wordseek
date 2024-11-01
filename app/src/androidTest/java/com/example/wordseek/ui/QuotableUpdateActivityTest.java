package com.example.wordseek.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.mockito.Mockito.when;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.wordseek.R;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuotableUpdateActivityTest {

    @Mock
    private Repository mockRepository;

    @Rule
    public ActivityScenarioRule<QuotableUpdateActivity> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), QuotableActivity.class){
                {
                    Word fakeWord = new Word("abolish");
                    String fakeDefinition = "Some definition.......";
                    User user = new User("JSmith99", "password123");

                    putExtra("position", 2);
                    putExtra("username",user.getUserName());
                    putExtra("word", fakeWord);
                    putExtra("definition", fakeDefinition);
                }
            });

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<String> wordList = Arrays.asList("Words", "More", "Abolish");
        when(mockRepository.getAllDistinctWords(anyInt())).thenReturn(wordList);
        when(mockRepository.getAssociatedQuotables(anyString())).thenReturn(any());
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void testQuotableUpdateActivityWordVisible() {
        Espresso.onView(withId(R.id.quotableWord)).check(matches(isDisplayed()));
    }
    @Test
    public void testQuotableUpdateActivityDefinitionVisible() {
        Espresso.onView(withId(R.id.quotableDefinition)).check(matches(isDisplayed()));
    }
    @Test
    public void testQuotableUpdateActivityAddQuote() {
        onView(withId(R.id.quoteEdtTxt))
                .perform(clearText(),
                        typeText("Some new quote to be added with the word abolish"),
                        closeSoftKeyboard());
        onView(withId(R.id.quoteSubmitBtn)).perform(click());
        onView(withId(R.id.quotableRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


    @Test
    public void testQuotableUpdateActivityDeleteQuote() {
        onView(withId(R.id.quoteEdtTxt))
                .perform(clearText(),
                        typeText("Some new quote to be added with the word abolish"),
                        closeSoftKeyboard());
        onView(withId(R.id.quoteSubmitBtn)).perform(click());
        onView(withId(R.id.quoteEdtTxt))
                .perform(clearText(),
                        typeText("New quote to be added and deleted"),
                        closeSoftKeyboard());
        onView(withId(R.id.quoteSubmitBtn)).perform(click());

        onView(withId(R.id.quotableRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(1));

//        onView(withText("Delete Quotable?")).check(matches(isDisplayed()));
//        onView(withText("Yes")).perform(click());

        // fknsdfobasodgbaobs!!!! make a custom matcher
        onView(allOf(withId(R.id.deleteQuote),
                isDescendantOfA(allOf(withId(R.id.quotableRecyclerView),
                        hasDescendant(withText("New quote to be added and deleted"))))))
                .check(matches(isDisplayed())).perform(ViewActions.click());
    }
}