package com.example.test3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test3.databinding.ActivityRegistBinding;

public class regist extends AppCompatActivity {

    private static final String TAG = "regist";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ActivityRegistBinding bindding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

        bindding = ActivityRegistBinding.inflate(LayoutInflater.from(this));
        setContentView(bindding.getRoot());

        //初始化 PreferenceManager
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        AssetManager am = getAssets();
        Typeface tf = Typeface.createFromAsset(am, "fonts/kt.ttf");
        bindding.appNameRG.setTypeface(tf);


        bindding.userRegist.setOnClickListener(v -> {

            //判断输入是否合格
            String userName, psd, psd_re, userId;
            userName = bindding.usernameRG.getText().toString();
            psd = bindding.userpsdRG.getText().toString();
            psd_re = bindding.userpsdRGRE.getText().toString();
            userId = bindding.userIdRGRE.getText().toString();
            if ( !psd.equals(psd_re) || psd==null) {
                Toast.makeText(this, "" + psd + "\t" + psd_re + "\n" + "两次密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("userName", userName);
                editor.putString("userId", userId);
                editor.putString("userPsd",psd);

                editor.apply();
                AlertDialog.Builder ag=new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("系统提示")
                        .setMessage("注册成功,是否立即登录?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent =new Intent();
                                intent.putExtra("userName",userName);
                                intent.putExtra("psd",psd);
                                setResult(1001,intent);
                                finish();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               finish();
                            }
                        });
                ag.show();
            }

        });


    }
}