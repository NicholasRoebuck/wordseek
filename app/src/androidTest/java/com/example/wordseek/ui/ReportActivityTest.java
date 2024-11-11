package com.example.wordseek.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;

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

    @Test
    public void ReportsTest() throws Exception {
        mockRepo = mock(Repository.class);
        List<Quotable> quotables = new ArrayList<>();
        quotables.add(new Quotable("Some word","Some quote", 2, "11/10/2024"));
        quotables.add(new Quotable("Some word","Some quote", 3, "10/10/2024"));
        quotables.add(new Quotable("Another word","Another quote", 2, "9/10/2024"));
        quotables.add(new Quotable("Some word","Some quote", 1, "19/10/2024"));

        when(mockRepo.getAllQuotables()).thenReturn(quotables);
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.reportAdapter.addAll(quotables);
            activity.reportRecyclerView.setAdapter(activity.reportAdapter);
        });
    }
}