package com.example.test3.ui.setting;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class settingViewModel extends AndroidViewModel {

    SharedPreferences pref;

    //可变的liveData 用于观察用户是否登陆
    MutableLiveData<Boolean> isLogin;

    public settingViewModel(@NonNull Application application) {
        super(application);
        pref = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    //初始化liveData
    public MutableLiveData<Boolean> getIsLogin() {
        if (isLogin==null){
            isLogin=new MutableLiveData<>();
            isLogin.setValue(pref.getBoolean("isLogin",false));
        }
        return isLogin;
    }



}