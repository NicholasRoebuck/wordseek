package com.example.wordseek.ui;

import android.app.Activity;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    EditText rePassword, password, userName, accountName;
    Button submit, login;
    Repository repository;
    ExecutorService executorService;
    ActionBar actionBar;
    OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        executorService = Executors.newFixedThreadPool(2);
        repository = new Repository(getApplication());
        accountName = findViewById(R.id.accountNameEdtTxt);
        password = findViewById(R.id.passwordEdtTxt);
        userName = findViewById(R.id.userNameEdtTxt);
        rePassword = findViewById(R.id.rePasswordEdtTxt);
        submit = findViewById(R.id.submitBtn);
        login = findViewById(R.id.registerLoginBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accountNameString = accountName.getText().toString().trim();
                String userNameString = userName.getText().toString().trim();
                String rePasswordString = rePassword.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if (accountNameString.isEmpty()) {
                    closeKeyboard();
                    Toast.makeText(RegisterActivity.this, "Account name must be greater than 5 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userNameString.isEmpty() || userNameString.length() < 5) {
                    closeKeyboard();
                    Toast.makeText(RegisterActivity.this, "User name must be greater than 5 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordString.isEmpty() || passwordString.length() < 8) {
                    closeKeyboard();
                    Toast.makeText(RegisterActivity.this, "Password must be more than 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rePasswordString.isEmpty() || rePasswordString.length() < 8) {
                    closeKeyboard();
                    Toast.makeText(RegisterActivity.this, "Re-entered password must be more than 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkPasswords(passwordString, rePasswordString)){
                        if (!checkUsernames(userNameString)){
                            executorService.submit(() ->{
                                repository.insert(new User(accountNameString, userNameString, passwordString));
                            });
                            closeKeyboard();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }else {
                            closeKeyboard();
                            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    closeKeyboard();
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        hideSoftKeyboard(findViewById(R.id.register));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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

    public boolean checkPasswords(String pass1, String pass2){
        return pass1.equals(pass2);
    }

    public boolean checkUsernames(String userName) {
        User user = repository.checkUser(userName);
        return user != null && user.getUserName().equals(userName);
    }

    @Override
    protected void onDestroy() {
        onBackPressedCallback.remove();
        super.onDestroy();
    }
}