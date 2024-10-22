package com.example.wordseek.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wordseek.dao.QuotableDAO;
import com.example.wordseek.dao.UserDAO;
import com.example.wordseek.dao.WordDAO;
import com.example.wordseek.entities.Quotable;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

@Database(entities = {Quotable.class, Word.class, User.class}, version = 2, exportSchema = false)
public abstract class WordSeekDatabase extends RoomDatabase {

    public abstract UserDAO UserDAO();
    public abstract WordDAO WordDAO();
    public abstract QuotableDAO QuotableDAO();

    private static volatile WordSeekDatabase INSTANCE;

    static WordSeekDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (WordSeekDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordSeekDatabase.class, "WordSeek.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
