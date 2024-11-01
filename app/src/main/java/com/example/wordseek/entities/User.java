package com.example.wordseek.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.jar.Attributes;

@Entity(tableName = "user_table")
public class User implements Parcelable {
    @PrimaryKey
    Integer id;

    @ColumnInfo(name = "user_name")
    String userName;

    @ColumnInfo(name = "password")
    String password;

    @ColumnInfo(name = "account_name")
    String accountName;


    @Ignore
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Ignore
    public User(Integer id, String userName, String password, String accountName) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.accountName = accountName;
    }

    public User(String accountName, String userName, String password) {
        this.accountName = accountName;
        this.userName = userName;
        this.password = password;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        userName = in.readString();
        password = in.readString();
        accountName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(accountName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
