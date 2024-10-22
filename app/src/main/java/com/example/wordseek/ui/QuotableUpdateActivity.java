package com.example.wordseek.ui;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wordseek.R;
import com.example.wordseek.adapter.QuotableAdapter;
import com.example.wordseek.adapter.QuotableAdapterInterface;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuotableUpdateActivity extends AppCompatActivity implements QuotableAdapterInterface {
    private QuotableAdapter quotableAdapter;
    EditText quotesEdtTxt;
    TextView quotableWord, quotableUsername, quotableDefinition;
    Button quoteAddBtn, quoteUpdateBtn;
    List<Quotable> quotablesList;
    Repository repository;
    ExecutorService executorService;
    RecyclerView quotableRecyclerView;
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
        String usernameString;
        int userId;
        userId = sharedPreferences.getInt("userId", 0);
        usernameString = sharedPreferences.getString("username", "");
        executorService = Executors.newSingleThreadExecutor();
        repository = new Repository(getApplication());
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        List<String> fromWordList = repository.getAllDistinctWords(userId);
        String wordListPosition = fromWordList.get(position);
        quotablesList = repository.getAssociatedQuotables(wordListPosition);

        getDefinition(wordListPosition);

        quoteUpdateBtn = findViewById(R.id.quoteUpdateBtn);
        quotableWord = findViewById(R.id.quotableWord);
        quotableWord.setText(wordListPosition);
        quotableUsername = findViewById(R.id.quotableUsername);
        quotableUsername.setText(usernameString);
        quotableDefinition = findViewById(R.id.quotableDefinition);
        quotesEdtTxt = findViewById(R.id.quoteEdtTxt);
        quoteAddBtn = findViewById(R.id.quoteSubmitBtn);
        quotableRecyclerView = findViewById(R.id.quotableRecyclerView);

        quotableAdapter = new QuotableAdapter(this, quotablesList, this);
        quotableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quotableRecyclerView.setAdapter(quotableAdapter);

        hideSoftKeyboard(findViewById(R.id.quotable));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(QuotableUpdateActivity.this, ProfileActivity.class));
            }
        };
    }


    @Override
    public void quotableAdapterInterfaceOnClick(int position) {
        String quoteTxt = quotablesList.get(position).getQuotable();
        quotesEdtTxt.setText(quoteTxt);
        String updateQuotableTxt = quotesEdtTxt.getText().toString().trim();

        quoteUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateQuotableTxt.isEmpty()){
                    return;
                }
                Quotable quotable = quotablesList.get(position);
                if (quotable.getQuotable().equals(updateQuotableTxt)){
                    quotesEdtTxt.setText("");
                    return;
                }
                    quotable.setQuotable(updateQuotableTxt);
                    executorService.execute(()->{
                        repository.update(quotable);
                    });
                    quotableAdapter.notifyItemChanged(position);
                    quotesEdtTxt.setText("");
                }
        });

        quoteAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote = quotesEdtTxt.getText().toString();
                if (!quote.trim().isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("userId", 0);
                    Quotable quotable =new Quotable(quotablesList.get(position).getWord(), quote, userId);
                    quotableAdapter.addQuote(quotable);
                    executorService.execute(()->{
                        repository.insert(quotable);
                    });
                    quotesEdtTxt.setText("");
                }
            }
        });

    }
//
//    if (!quotablesList.contains(new Quotable(updateQuotableTxt))) {
//        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
//        int userId = sharedPreferences.getInt("userId", 0);
//        Quotable newQuote =new Quotable(quotableWord.getText().toString(), updateQuotableTxt, userId);
//        executorService.execute(()->{
//            repository.insert(newQuote);
//            runOnUiThread(()->{
//                quotableAdapter.addQuote(newQuote);
//                quotableAdapter.notifyItemInserted(quotablesList.size()-1);
//            });
//        });
//    }

    public void getDefinition(String randomWord){
        Log.e("WordActivity", "Error: " + randomWord);
        RequestQueue queue = Volley.newRequestQueue(QuotableUpdateActivity.this);
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+randomWord;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
                    JSONObject object = meaningsArray.getJSONObject(0);
                    JSONArray definitionsArray = object.getJSONArray("definitions");
                    JSONObject definitionsObject = definitionsArray.getJSONObject(0);
                    String definitionData = definitionsObject.optString("definition", "null");
                    quotableDefinition.setText(definitionData);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WordActivity", "Error: " + error.toString());
                Toast.makeText(QuotableUpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
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