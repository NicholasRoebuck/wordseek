package com.example.wordseek.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wordseek.entities.Quotable;

import java.util.List;

@Dao
public interface QuotableDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Quotable quotable);

    @Update
    void update(Quotable quotable);

    @Delete
    void delete(Quotable quotable);

    @Query("SELECT * FROM quotable_table WHERE user_id=:userId ORDER BY word ASC")
    List<Quotable> getAssociatedQuotables(int userId);

    @Query("SELECT * FROM quotable_table WHERE quotableId=:quotableId")
    boolean isQuotable(int quotableId);

    @Query("SELECT DISTINCT * FROM quotable_table WHERE word = :word")
    List<Quotable> getAssociatedQuotables(String word);

    @Query("SELECT DISTINCT word FROM quotable_table WHERE user_id=:userId")
    List<String> getAllDistinctWords(int userId);

    @Query("SELECT * FROM quotable_table ORDER BY quotableId ASC")
    List<Quotable> getAllQuotables();

}
