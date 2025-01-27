package com.example.wordseek.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordseek.R;
import com.example.wordseek.adapter.ReportAdapter;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;

import java.util.Collections;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    OnBackPressedCallback onBackPressedCallback;
    List<Quotable> quotables;
    RecyclerView reportRecyclerView;
    ReportAdapter reportAdapter;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.report), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        reportRecyclerView = findViewById(R.id.reportRecyclerView);
        repository = new Repository(getApplication());
        quotables = repository.getAllQuotables();

        reportAdapter = new ReportAdapter(this, quotables);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportRecyclerView.setAdapter(reportAdapter);
        reportAdapter.notifyDataSetChanged();
        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(ReportActivity.this, ProfileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        };
    }

    @Override
    protected void onDestroy() {
        onBackPressedCallback.remove();
        super.onDestroy();
    }
}