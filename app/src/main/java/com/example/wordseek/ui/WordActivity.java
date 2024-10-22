package com.example.wordseek.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wordseek.R;
import com.example.wordseek.database.Repository;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WordActivity extends AppCompatActivity {
    TextView userName, word, definition, partOfSpeech, phonetics, pronunciation;
    Button createQuotable, newWord;
    Repository repository;
    MediaPlayer mediaPlayer;
    Word currentWord;
    OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_word);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref_Name", MODE_PRIVATE);
        String usernameString;
        usernameString = sharedPreferences.getString("username", "");
        repository = new Repository(getApplication());
        mediaPlayer = new MediaPlayer();

       currentWord = repository.randomWord();

        userName = findViewById(R.id.wordActivityUsername);
        userName.setText(usernameString);
        word = findViewById(R.id.wordActivityWord);
        word.setText(currentWord.getWord());
        definition = findViewById(R.id.definition);
        partOfSpeech = findViewById(R.id.partOfSpeech);
        phonetics = findViewById(R.id.phonetics);
        pronunciation = findViewById(R.id.pronunciation);

        createQuotable = findViewById(R.id.createWordQuotableBtn);
        newWord = findViewById(R.id.fetchNewWordBtn);

        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getWord(getNextWord().getWord());
            }
        });

        createQuotable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WordActivity.this, QuotableActivity.class)
                        .putExtra("word", currentWord)
                        .putExtra("definition", definition.getText().toString()));
            }
        });

        getWord(currentWord.getWord());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onBackPressedCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(WordActivity.this, ProfileActivity.class));
            }
        };
    }

    public void getWord(String randomWord){
        Log.e("WordActivity", "Error: " + randomWord);
        RequestQueue queue = Volley.newRequestQueue(WordActivity.this);
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+randomWord;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WordActivity", "Error: " + error.toString());
                Toast.makeText(WordActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    public void parseData(JSONArray response){
        try {
            JSONObject jsonObject = response.getJSONObject(0);
            String wordData = jsonObject.optString("word", "Invalid, pick a new word.");
            word.setText(wordData + ":  ");

            JSONArray phoneticsData = jsonObject.getJSONArray("phonetics");
            if (phoneticsData.length()>0){
                JSONObject phoneticsDataObject = phoneticsData.getJSONObject(0);
                String phoneticText = phoneticsDataObject.optString("text", "null");
                if (phoneticText.equals("null")){
                    JSONObject phoneticsDataObject1 = phoneticsData.getJSONObject(1);
                    phoneticText = phoneticsDataObject1.optString("text", "null");
                }
                phonetics.setText(phoneticText);
                Log.d("Phonetics", "parseData: " + phoneticText);
                getAudio(phoneticsData);
            } else{
                phonetics.setText("null");
            }

            JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
            JSONObject object = meaningsArray.getJSONObject(0);
            String partOfSpeechData = object.optString("partOfSpeech", "null");
            partOfSpeech.setText(partOfSpeechData);
            Log.d("Part of speech", "parseData: " + partOfSpeechData);


            JSONArray definitionsArray = object.getJSONArray("definitions");
            JSONObject definitionsObject = definitionsArray.getJSONObject(0);
            String definitionData = definitionsObject.optString("definition", "null");
            definition.setText(definitionData);
            Log.d("Definition", "parseData: " + definitionData);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Word getNextWord(){
        currentWord = repository.randomWord();
        return currentWord;
    }



    public void getAudio(JSONArray jsonObject) {
        try {
            String audioUrl = ""; //jsonObjectData.optString("audio", "");
            for (int i = 0; i< jsonObject.length(); i++){
                JSONObject jsonObjectData = jsonObject.getJSONObject(i);
                audioUrl = jsonObjectData.optString("audio", "");
            }

            Log.d("Audio URL", "Audio URL: " + audioUrl);

            String finalAudioUrl = audioUrl;
            pronunciation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                    }
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());
                    try {
                        mediaPlayer.reset();
                        Log.d("Audio URL", "Audio URL: " + finalAudioUrl);

                        if (finalAudioUrl.isEmpty()) {
                            mediaPlayer = MediaPlayer.create(WordActivity.this, R.raw.ding);
                        } else {
                            mediaPlayer.setDataSource(finalAudioUrl);
                            mediaPlayer.prepare();
                        }
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.start();

                        mediaPlayer.setOnErrorListener((mp, i, o) -> {
                            Log.e("MediaPlayer", "Error occurred: " + i + ", " + o);
                            return true;
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }finally {
            mediaPlayer.reset();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        onBackPressedCallback.remove();
    }
}