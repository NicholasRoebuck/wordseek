package com.example.wordseek.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.wordseek.dao.QuotableDAO;
import com.example.wordseek.dao.UserDAO;
import com.example.wordseek.dao.WordDAO;
import com.example.wordseek.entities.Quotable;
import com.example.wordseek.entities.User;
import com.example.wordseek.entities.Word;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {
    private final UserDAO mUserDAO;
    private final WordDAO mWordDAO;
    private final QuotableDAO mQuotableDAO;

    private List<Word> mAllWords;
    private List<Quotable> mAllQuotables;

    private static int NUMBER_OF_THREADS = 4;

    static final ExecutorService dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public Repository(Application application){
        WordSeekDatabase db = WordSeekDatabase.getDatabase(application);
        mUserDAO = db.UserDAO();
        mWordDAO = db.WordDAO();
        mQuotableDAO = db.QuotableDAO();
    }

    public void insert(Word word){
        dbExecutor.execute(() ->{
            mWordDAO.insert(word);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllDistinctWords(int userId) {
        Future<List<String>> task = dbExecutor.submit(() -> mQuotableDAO.getAllDistinctWords(userId));
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Quotable> getAllQuotables() {
        dbExecutor.execute(() -> {
            mAllQuotables = mQuotableDAO.getAllQuotables();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllQuotables;
    }

//    public void update (Word word) {
//        dbExecutor.execute(() ->{
//            mWordDAO.update(word);
//        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public void delete(Word word) {
//        dbExecutor.execute(() ->{
//            mWordDAO.delete(word);
//        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void insert(User user){
        dbExecutor.execute(() ->{
            mUserDAO.insert(user);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update (User user) {
        dbExecutor.execute(() ->{
            mUserDAO.update(user);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(User user) {
        dbExecutor.execute(() ->{
            mUserDAO.delete(user);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }public void insert(Quotable quotable){
        dbExecutor.execute(() ->{
            mQuotableDAO.insert(quotable);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update (Quotable quotable) {
        dbExecutor.execute(() ->{
            mQuotableDAO.update(quotable);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Quotable quotable) {
        dbExecutor.execute(() ->{
            mQuotableDAO.delete(quotable);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isQuotable(int quotableId){
        Future<Boolean> task =  dbExecutor.submit(()-> mQuotableDAO.isQuotable(quotableId));
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public User userLogin(String userName, String password) {
        Future<User> task = dbExecutor.submit(() -> mUserDAO.userLogin(userName, password));
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public User checkUser(String userName) {
        Future<User> task = dbExecutor.submit(()->mUserDAO.checkUser(userName));
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsData(){
     Future<Boolean> task =  dbExecutor.submit(()-> mWordDAO.containsData());
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Word randomWord(){
        Future<Word> task =  dbExecutor.submit(()-> mWordDAO.randomWord());
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Quotable> getAssociatedQuotables(int userId) {
        dbExecutor.execute(() ->{
            mAllQuotables = mQuotableDAO.getAssociatedQuotables(userId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return (List<Quotable>) mAllQuotables;
    }

    public List<Quotable> getAssociatedQuotables(String word) {
        dbExecutor.execute(() ->{
            mAllQuotables = mQuotableDAO.getAssociatedQuotables(word);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return (List<Quotable>) mAllQuotables;
    }

//    public void getAssociatedWords(int userId) {
//        dbExecutor.execute(() ->{
//            mAllWords = mWordDAO.getAssociatedWords(userId);
//        });
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
