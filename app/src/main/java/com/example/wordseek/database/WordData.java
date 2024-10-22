package com.example.wordseek.database;

import android.content.Context;
import android.content.res.Resources;

import com.example.wordseek.R;
import com.example.wordseek.entities.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class WordData {
    public static void populateWordSeeKDatabase(Context context){
        WordSeekDatabase db = WordSeekDatabase.getDatabase(context);
        Resources resources = context.getResources();

        Executors.newSingleThreadExecutor().execute(()->{
            if  (db.WordDAO().containsData()){
                return;
            }
            try{
                InputStream inputStream = resources.openRawResource(R.raw.words);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                // handle what should happen if the db is already initialized

                while((line = reader.readLine()) != null){
                    if (!line.isEmpty()) {
                        db.WordDAO().insert(new Word(line.trim()));
                    }
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
