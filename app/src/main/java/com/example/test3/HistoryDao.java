package com.example.test3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(History... histories);

    @Query("DELETE FROM History")
    void deleteAll();

    @Query("SELECT * FROM History ORDER BY ID DESC")
    LiveData<List<History>> getAll();

    @Delete
    void delete(History... histories);



}
