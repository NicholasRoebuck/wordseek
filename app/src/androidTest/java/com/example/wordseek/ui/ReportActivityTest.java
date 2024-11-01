package com.example.wordseek.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReportActivityTest {

    private Repository mockRepo;

    @Rule
    public ActivityScenarioRule<ReportActivity> activityScenarioRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), ReportActivity.class));

    @Before
    public void setUp() throws Exception {
        mockRepo = mock(Repository.class);
        List<Quotable> quotables = new ArrayList<>();
        quotables.add(new Quotable("Some quote"));
        quotables.add(new Quotable("Another quote"));
        quotables.add(new Quotable("More quote"));
        quotables.add(new Quotable("More of the more quote"));

        when(mockRepo.getAllQuotables()).thenReturn(quotables);
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.reportAdapter.addAll(quotables);
            activity.reportList.setAdapter(activity.reportAdapter);
        });
    }
}