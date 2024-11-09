package com.example.wordseek.ui;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
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

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuotableUpdateActivity extends AppCompatActivity implements QuotableAdapterInterface {
    private QuotableAdapter quotableAdapter;
    EditText quotesEdtTxt;
    TextView quotableWord, quotableUsername, quotableDefinition;
    Button quoteAddBtn;
    List<Quotable> quotablesList;
    Repository repository;
    ExecutorService executorService;
    RecyclerView quotableRecyclerView;
    OnBackPressedCallback onBackPressedCallback;
    String wordListPosition;

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
        int userId = sharedPreferences.getInt("userId", 0);
        usernameString = sharedPreferences.getString("username", "");
        executorService = Executors.newFixedThreadPool(3);
        repository = new Repository(getApplication());
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        List<String> fromWordList = repository.getAllDistinctWords(userId);
        wordListPosition = fromWordList.get(position);
        quotablesList = repository.getAssociatedQuotables(wordListPosition);

        getDefinition(wordListPosition);

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

        createQuote(wordListPosition);
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

    public void createQuote(String word){
        quoteAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote = quotesEdtTxt.getText().toString();
                if (!quote.trim().isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
                    int userId = sharedPreferences.getInt("userId", 0);
                    Log.i("UpdateQuotableAct", "onClick: " + quotablesList.size());
                    Quotable quotable =new Quotable(word, quote, userId, String.valueOf(LocalDate.now()));
                    quotesEdtTxt.setText("");
                    executorService.execute(()->{
                        repository.insert(quotable);
                    });
                    quotableAdapter.addQuote(quotable);
                    quotableAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void quotableAdapterInterfaceOnClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        final EditText editText = new EditText(this);

        builder.setMessage(quotableWord.getText().toString().toUpperCase() + ":  " + quotableDefinition.getText().toString())
                .setView(editText)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Quotable quotable = quotablesList.get(position);
                        quotable.setQuotable(editText.getText().toString());
                        executorService.execute(()->{
                            Log.i("UpdateQuote", "onClick: " + quotable.getQuotable());
                            repository.update(quotable);
                            Log.i("UpdateQuote", "onClick: " + quotable.getQuotable());
                        });
                        quotableAdapter.notifyItemChanged(position);
                        quotableAdapter.notifyDataSetChanged();
                    }
                }).create().show();
    }

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

    @Override
    public void quotableAdapterInterfaceDeleteQuoteOnClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle("Delete Quotable?")
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Quotable quotable = quotablesList.get(position);
                        executorService.execute(() -> {
                            repository.delete(quotable);
                        });
                        quotableAdapter.delete(position);
                        quotableAdapter.notifyDataSetChanged();
                    }
                }).create().show();
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