package com.example.wordseek.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wordseek.R;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {
    EditText userName, password;
    Button login;
    Repository repository;
    ExecutorService executorService;
    ActionBar actionBar;
    OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        repository = new Repository(getApplication());
        executorService = Executors.newSingleThreadExecutor();
        login = findViewById(R.id.loginBtn);
        userName = findViewById(R.id.userNameLoginEdtTxt);
        password = findViewById(R.id.passwordLoginEdtTxt);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameString = userName.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if (userNameString.isEmpty()){
                    Toast.makeText(LoginActivity.this, "User name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordString.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = repository.userLogin(userNameString, passwordString);

                if (user==null ){
                    Toast.makeText(LoginActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                } else{
                    SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", user.getUserName());
                    editor.putString("password", user.getPassword());
                    editor.putString("accountName", user.getAccountName());
                    editor.putInt("userId", user.getId());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    userName.setText("");
                    password.setText("");
                }
            }
        });


        hideSoftKeyboard(findViewById(R.id.login));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        };
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