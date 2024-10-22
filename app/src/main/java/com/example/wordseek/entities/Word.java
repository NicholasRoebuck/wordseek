package com.example.wordseek.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.wordseek.database.WordSeekDatabase;

@Entity(tableName = "word_table")
public class Word implements Parcelable {
    @PrimaryKey
    Integer wordId;

    @ColumnInfo(name = "word")
    String word;

    public Word(String word) {
        this.word = word;
    }

    @Ignore
    public Word(Integer wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }

    protected Word(Parcel in) {
        if (in.readByte() == 0) {
            wordId = null;
        } else {
            wordId = in.readInt();
        }
        word = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (wordId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(wordId);
        }
        dest.writeString(word);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
