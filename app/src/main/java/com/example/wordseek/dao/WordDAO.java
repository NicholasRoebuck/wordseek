package com.example.wordseek.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wordseek.entities.Word;

import java.util.List;

@Dao
public interface WordDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Query("SELECT *  FROM word_table")
    boolean containsData();

    @Query("SELECT * FROM word_table WHERE LENGTH(word) > 5 ORDER BY RANDOM() LIMIT 1")
    Word randomWord();

//    @Update
//    void update(Word word);
//
//    @Delete
//    void delete(Word word);
//
////    @Query("SELECT * FROM excursions ORDER BY excursion_id ASC")
////    List<Word> getAllWords();
//
//    @Query("SELECT * FROM word_table WHERE userId=:userId ORDER BY word ASC")
//    List<Word> getAssociatedWords(int userId);
//
////    @Query("SELECT *  FROM word_table WHERE wordId=:wordId")
////    List<Excursion> getExcursion(int excursionID);
}
