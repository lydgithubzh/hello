package com.example.test3.ui.home;


import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.test3.History;
import com.example.test3.HistoryDao;
import com.example.test3.HistoryDatabases;
import com.example.test3.ui.history.historyViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeViewModel extends AndroidViewModel {



    HistoryDao historyDao;
    SharedPreferences pref;

    //可变的liveData 用于观察用户是否登陆
    MutableLiveData<Boolean> isLogin;



    public HomeViewModel(@NonNull Application application) {
        super(application);
        pref = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        HistoryDatabases historyDatabases = HistoryDatabases.getDatabases(application);
        historyDao = historyDatabases.getHistoryDao();


    }

    //初始化liveData
    public MutableLiveData<Boolean> getIsLogin() {
        if (isLogin==null){
            isLogin=new MutableLiveData<>();
            isLogin.setValue(pref.getBoolean("isLogin",false));
        }
        return isLogin;
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

    void deleteAll() {
        new deleteHistory(historyDao).execute();
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



}