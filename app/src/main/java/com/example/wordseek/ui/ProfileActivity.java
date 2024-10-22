package com.example.wordseek.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordseek.R;
import com.example.wordseek.adapter.WordAdapter;
import com.example.wordseek.adapter.WordAdapterInterface;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.Quotable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProfileActivity extends AppCompatActivity implements WordAdapterInterface {
    private WordAdapter wordAdapter;
    TextView accountName, username, password;
    RecyclerView wordRecyclerView;
    Button newWord;
    List<String> wordList;
    Repository repository;
    ExecutorService executorService;
    OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
        String usernameString, accountNameString, passwordString;
        int userId;
        usernameString = sharedPreferences.getString("username", "");
        accountNameString = sharedPreferences.getString("accountName", "");
        passwordString = sharedPreferences.getString("password", "");
        userId = sharedPreferences.getInt("userId", 0);
        executorService = Executors.newFixedThreadPool(4);
        repository = new Repository(getApplication());

        wordRecyclerView = findViewById(R.id.wordRecView);
        wordList = repository.getAllDistinctWords(userId);

        newWord = findViewById(R.id.newWordBtn);
        accountName = findViewById(R.id.accountName);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        accountName.setText(accountNameString);
        username.setText(usernameString);
        MaskFilter blurMask = new BlurMaskFilter(40f, BlurMaskFilter.Blur.NORMAL);
        SpannableString spannableString = new SpannableString(passwordString);
        spannableString.setSpan(new MaskFilterSpan(blurMask), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        password.setText(spannableString);


        wordAdapter = new WordAdapter(this, wordList, this);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordRecyclerView.setAdapter(wordAdapter);

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlert();
            }
        });

        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, WordActivity.class));
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchBar = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) searchBar.getActionView();
        searchView.setQueryHint("Search your WordSeek Words");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                wordAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void createAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle("Change password?")
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

                    }
                }).create().show();
    }

    public ArrayList<String> parseData(JSONArray response){
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject jsonObject = response.getJSONObject(0);

            JSONArray phoneticsData = jsonObject.getJSONArray("phonetics");
            JSONObject phoneticsDataObject = phoneticsData.getJSONObject(0);
            String phoneticText = phoneticsDataObject.getString("text");
            String audio = phoneticsDataObject.getString("audio");

            JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
            JSONObject object = meaningsArray.getJSONObject(0);
            String partOfSpeechData = object.getString("partOfSpeech");

            JSONArray definitionsArray = object.getJSONArray("definitions");
            JSONObject definitionsObject = definitionsArray.getJSONObject(0);
            String definitionData = definitionsObject.getString("definition");

            list.add(phoneticText);
            list.add(audio);
            list.add(partOfSpeechData);
            list.add(definitionData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void wordAdapterInterfaceOnClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle("View quotables?")
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
                       startActivity(new Intent(ProfileActivity.this, QuotableUpdateActivity.class)
                               .putExtra("position", position));
                    }
                }).create().show();
    }

    @Override
    public void wordAdapterInterfaceDeleteWord(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder.setTitle("Delete word?")
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
                        List<Quotable> removedQuotables = repository.getAssociatedQuotables(wordList.get(position));
                        for (Quotable quotable : removedQuotables) {
                            executorService.execute(() -> {
                                repository.delete(quotable);
                            });
                        }
                        wordList.remove(position);
                        wordAdapter.notifyItemRemoved(position);
                    }
                }).create().show();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        wordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        onBackPressedCallback.remove();
        super.onDestroy();
    }}