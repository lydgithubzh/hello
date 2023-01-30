package com.example.test3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = History.class,version = 1,exportSchema = false)
public abstract class HistoryDatabases extends RoomDatabase {
    private static HistoryDatabases INSTANCE;
    public synchronized static HistoryDatabases getDatabases(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(), HistoryDatabases.class, "history_database.db").build();
        }
        return INSTANCE;
    }

    public abstract HistoryDao getHistoryDao();
}
