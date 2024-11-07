package com.example.wordseek.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.mockito.Mockito.when;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.wordseek.R;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

@LargeTest
public class QuotableUpdateActivityTest {

    @Mock
    private Repository mockRepository;

    @Rule
    public ActivityScenarioRule<QuotableUpdateActivity> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), QuotableActivity.class){
                {
                    Word fakeWord = new Word("Absence");
                    String fakeDefinition = "Some definition.......";
                    User user = new User("JSmith99", "password123");

                    putExtra("position", 3);
                    putExtra("username",user.getUserName());
                    putExtra("word", fakeWord);
                    putExtra("definition", fakeDefinition);
                }
            });

    @Before
    public void setUp() {
        Intents.init();
        MockitoAnnotations.openMocks(this);
        List<String> wordList = Arrays.asList("Words", "More", "Abolish", "Absence");
        when(mockRepository.getAllDistinctWords(anyInt())).thenReturn(wordList);
        when(mockRepository.getAssociatedQuotables(anyString())).thenReturn(null);
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
        Mockito.reset(mockRepository);
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

    }

}