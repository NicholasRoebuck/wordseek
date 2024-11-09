package com.example.wordseek.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordseek.R;
import com.example.wordseek.adapter.QuotableAdapter;
import com.example.wordseek.adapter.QuotableAdapterInterface;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuotableActivity extends AppCompatActivity implements QuotableAdapterInterface {
    private QuotableAdapter quotableAdapter;
    EditText quotesEdtTxt;
    TextView quotableWord, quotableUsername, quotableDefinition;
    Button quoteSubmitBtn;
    List<Quotable> quotablesList;
    Repository repository;
    ExecutorService executorService;
    RecyclerView quotableRecyclerView;
    Word word;
    OnBackPressedCallback onBackPressedCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quotable);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quotable), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
        String usernameString = sharedPreferences.getString("username", "");
        executorService = Executors.newFixedThreadPool(3);
        repository = new Repository(getApplication());
        Intent intent = getIntent();
        word = intent.getParcelableExtra("word");
        String definition = intent.getStringExtra("definition");
        quotablesList = repository.getAssociatedQuotables(word.getWord());


        quotableWord = findViewById(R.id.quotableWord);
        quotableWord.setText(word.getWord());
        quotableUsername = findViewById(R.id.quotableUsername);
        quotableUsername.setText(usernameString);
        quotableDefinition = findViewById(R.id.quotableDefinition);
        quotableDefinition.setText(definition);
        quotesEdtTxt = findViewById(R.id.quoteEdtTxt);
        quoteSubmitBtn = findViewById(R.id.quoteSubmitBtn);
        quotableRecyclerView = findViewById(R.id.quotableRecyclerView);

        quotableAdapter = new QuotableAdapter(this, quotablesList, this);
        quotableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quotableRecyclerView.setAdapter(quotableAdapter);

        createQuote();
        hideSoftKeyboard(findViewById(R.id.quotable));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(QuotableActivity.this, ProfileActivity.class));
            }
        };
    }


    @Override
    public void quotableAdapterInterfaceOnClick(int position) {
    }

    @Override
    public void quotableAdapterInterfaceDeleteQuoteOnClick(int position) {

    }


    public void createQuote(){
        quoteSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote = quotesEdtTxt.getText().toString();
                if (!quote.trim().isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("userId", 0);
                    Quotable quotable =new Quotable(word.getWord(), quote, userId, String.valueOf(LocalDate.now()));
                    quotableAdapter.addQuote(quotable);
                    executorService.execute(()->{
                        repository.insert(quotable);
                    });
                    quotesEdtTxt.setText("");
                    quotableAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
    public void hideSoftKeyboard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    closeKeyboard();
                    return false;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onBackPressedCallback.remove();
    }
}