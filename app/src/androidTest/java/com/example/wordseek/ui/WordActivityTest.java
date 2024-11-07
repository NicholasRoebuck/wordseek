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

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.wordseek.R;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Objects;

@LargeTest
public class WordActivityTest {
    static final String USERNAME = "JohnPha9001";
    static final String PASSWORD = "password123";
    static String WORD = "Something";

    @Rule
    public ActivityScenarioRule<WordActivity> activityScenarioRule =
            new ActivityScenarioRule<>(WordActivity.class);

    @Before
    public void setUp() throws Exception {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.mainLoginBtn)).perform(click());
        onView(withId(R.id.login)).check(matches(isDisplayed()));
        onView(withId(R.id.userNameLoginEdtTxt))
                .perform(clearText(), typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEdtTxt))
                .perform(clearText(), typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.profile)).check(matches(isDisplayed()));
        onView(withId(R.id.newWordBtn)).perform(click());
        onView(withId(R.id.wordActivity)).check(matches(isDisplayed()));
        WORD = onView(withId(R.id.wordActivityWord)).toString();
    }

    @Test
    public void viewWordActivityUsername(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.wordActivityUsername)).check(matches(withText(USERNAME)));
    }

    @Test
    public void viewWordActivityWord(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.wordActivityWord)).check(matches(isDisplayed()));
    }

    @Test
    public void viewWordActivityWordDefinition(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.wordActivityWordDefinition)).check(matches(isDisplayed()));
    }

    @Test
    public void viewWordActivityWordPronunciation(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.pronunciation)).check(matches(isDisplayed()));
    }

    @Test
    public void viewWordActivityWordPronunciationFunction(){
        ActivityScenario<WordActivity> scenario = ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.pronunciation)).perform(click());
        scenario.onActivity(activity -> {
            WordActivity spy = Mockito.spy(activity);
            Mockito.verify(spy).mediaPlayer.isPlaying();
        });
    }

    @Test
    public void viewWordActivityWordPhonetics(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.phonetics)).check(matches(isDisplayed()));
    }

    @Test
    public void viewWordActivityWordPartOfSpeech(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.partOfSpeech)).check(matches(isDisplayed()));
    }

    @Test
    public void viewWordActivityNewWord(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.fetchNewWordBtn)).perform(click());
        onView(withId(R.id.wordActivityWord)).check(matches(isDisplayed()));
        assert !Objects.equals(WORD, onView(withId(R.id.wordActivityWord)).toString());
    }

    @Test
    public void viewWordActivityCreateQuotableView(){
        ActivityScenario.launch(WordActivity.class);
        onView(withId(R.id.createWordQuotableBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.createWordQuotableBtn)).perform(click());
        onView(withId(R.id.quotable)).check(matches(isDisplayed()));
    }


}