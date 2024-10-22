package com.example.wordseek.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wordseek.entities.User;

import java.util.concurrent.Future;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table WHERE user_name=:userName AND password=:password")
    User userLogin(String userName, String password);

    @Query("SELECT * FROM user_table WHERE user_name=:userName")
    User checkUser(String userName);
}
