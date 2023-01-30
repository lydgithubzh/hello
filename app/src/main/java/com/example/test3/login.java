package com.example.test3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test3.databinding.ActivityLoginBinding;

public class login extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ActivityLoginBinding bindding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

        //初始化ViewBinding
        bindding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(bindding.getRoot());

        //初始化 PreferenceManager
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        //设置字体样式
        AssetManager am = getAssets();
        Typeface tf = Typeface.createFromAsset(am, "fonts/kt.ttf");
        bindding.appName.setTypeface(tf);

        bindding.userLogin.setOnClickListener(v -> {
            check();
        });

        bindding.goRegist.setOnClickListener(v -> {
            Intent intent =new Intent(this,regist.class);
            startActivityForResult(intent,1000);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1001){
            bindding.username.setText(data.getStringExtra("userName"));
            bindding.userpsd.setText(data.getStringExtra("psd"));
            check();
        }

    }

    void check(){
        String userName, userPsd;
        userName = bindding.username.getText().toString();
        userPsd = bindding.userpsd.getText().toString();
        if (userName.equals(pref.getString("userName", "")) && userPsd.equals(pref.getString("userPsd", ""))) {
            Intent intent = new Intent();
            setResult(1001,intent);
            finish();
        } else {
            Toast.makeText(this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}














