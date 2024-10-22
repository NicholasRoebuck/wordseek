package com.example.wordseek.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotable_table")
public class Quotable {
    @PrimaryKey(autoGenerate = true)
    Integer quotableId;

    @ColumnInfo(name = "word")
    String word;

    @ColumnInfo(name = "quotable")
    String quotable;

    @ColumnInfo(name = "user_id")
    Integer userId;

    @Ignore
    public Quotable(Integer quotableId, String word, String quotable, Integer userId) {
        this.quotableId = quotableId;
        this.word = word;
        this.quotable = quotable;
        this.userId = userId;
    }

    @Ignore
    public Quotable(String quotable) {
        this.quotable = quotable;
    }

    public Quotable(String word, String quotable, Integer userId) {
        this.word = word;
        this.quotable = quotable;
        this.userId = userId;
    }

    public Integer getQuotableId() {
        return quotableId;
    }

    public void setQuotableId(Integer quotableId) {
        this.quotableId = quotableId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getQuotable() {
        return quotable;
    }

    public void setQuotable(String quotable) {
        this.quotable = quotable;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = this.userId;
    }
}
