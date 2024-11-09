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

    @ColumnInfo(name = "date")
    String date;

    @Ignore
    public Quotable(Integer quotableId, String word, String quotable, Integer userId, String date) {
        this.quotableId = quotableId;
        this.word = word;
        this.quotable = quotable;
        this.userId = userId;
        this.date = date;
    }

    @Ignore
    public Quotable(String quotable) {
        this.quotable = quotable;
    }

    public Quotable(String word, String quotable, Integer userId, String date) {
        this.word = word;
        this.quotable = quotable;
        this.userId = userId;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Quotable  [  " +
                " Quotable  == '" + quotable + '\'' +
                ",  User ID  == " + userId +
                ",  Word  == '" + word + '\'' +
                ",  Date  == '" + date + '\'' +
                "  ]";
    }
}
