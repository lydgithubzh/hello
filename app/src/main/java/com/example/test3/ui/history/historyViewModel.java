package com.example.test3.ui.history;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.test3.History;
import com.example.test3.HistoryDao;
import com.example.test3.HistoryDatabases;

import java.util.List;

;

public class historyViewModel extends AndroidViewModel {

    LiveData<List<History>> allHistory;

    HistoryDao historyDao;

    public historyViewModel(@NonNull Application application) {
        super(application);

        //初始化数据库
        HistoryDatabases historyDatabases = HistoryDatabases.getDatabases(application.getApplicationContext());
        historyDao = historyDatabases.getHistoryDao();
        //获取到所有签到记录
        allHistory = historyDao.getAll();


    }


    public LiveData<List<History>> getAllHistory() {
        return allHistory;
    }


    void deleteAll() {
        new deleteHistory(historyDao).execute();
    }

    void delete(History history) {
        new delete(historyDao).execute(history);
    }

    void insertHistory(History... histories) {
        new insertHistory(historyDao).execute(histories);
    }

    static class insertHistory extends AsyncTask<History, Void, Void> {
        HistoryDao historyDao;

        insertHistory(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }


        @Override
        protected Void doInBackground(History... histories) {
            historyDao.insert(histories);
            return null;
        }
    }


    static class deleteHistory extends AsyncTask<Void, Void, Void> {
        HistoryDao historyDao;

        deleteHistory(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            historyDao.deleteAll();
            return null;
        }
    }

    static class delete extends AsyncTask<History, Void, Void> {
        HistoryDao historyDao;

        delete(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }


        @Override
        protected Void doInBackground(History... histories) {
            historyDao.delete(histories);
            return null;
        }
    }


}