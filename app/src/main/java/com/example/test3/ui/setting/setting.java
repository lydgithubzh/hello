package com.example.test3.ui.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.test3.About;
import com.example.test3.R;
import com.example.test3.databinding.FragmentSettingBinding;
import com.example.test3.login;

public class setting extends Fragment {

    private static final String TAG = "setting";

    private settingViewModel settingViewModel;
    FragmentSettingBinding binding;
    String[] fontStyleArr = {"婴幼儿模式", "青少年模式", "老年人模式"};
    AlertDialog dialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //初始化viewModel
        settingViewModel =
                new ViewModelProvider(this).get(settingViewModel.class);
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = pref.edit();

        //利用viewModel的观察者观察用户是否登录
        //如果登录则初始化用户信息数据
        //否则清空用户信息
        settingViewModel.getIsLogin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean islogin) {
                if (islogin) {
                    Log.d(TAG, "onChanged: " + islogin);
                    binding.userImage.setImageResource(R.mipmap.user);
                    binding.username.setText(pref.getString("userName", "哦哦"));
                    binding.userId.setText(pref.getString("userId", "123456"));
                    binding.settingLogin.setVisibility(View.INVISIBLE);
                } else {
                    binding.userImage.setImageResource(R.mipmap.main);
                    binding.username.setText("哦哦");
                    binding.userId.setText("");
                    binding.settingLogin.setVisibility(View.VISIBLE);

                }
            }
        });

        binding.settingLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), login.class);
            startActivityForResult(intent, 1);
        });

        binding.logout.setOnClickListener(v -> {
            settingViewModel.isLogin.setValue(false);
            editor.putBoolean("isLogin", false);
            editor.apply();
            Toast.makeText(getContext(), "退出成功", Toast.LENGTH_SHORT).show();
        });
        binding.about.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), About.class);
            startActivity(intent);
        });
        binding.use.setOnClickListener(v -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())//设置单选框列表
                    .setTitle("设置字体的大小")   //设置标题
                    .setIcon(R.mipmap.ic_launcher) //设置图标
                    .setSingleChoiceItems(fontStyleArr, 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                editor.putFloat("scale", 0.6f);
                                editor.apply();
                            } else if (i == 1) {
                                editor.putFloat("scale", 1.0f);
                                editor.apply();
                            } else {
                                editor.putFloat("scale", 2.0f);
                                editor.apply();
                            }
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//在对话框中设置“确定”按钮
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            androidx.appcompat.app.AlertDialog.Builder ag = new androidx.appcompat.app.AlertDialog.Builder(getContext())
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle("系统提示")
                                    .setMessage("是否重启立即生效??")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //重启
                                            final Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            Process.killProcess(Process.myPid());
                                        }
                                    })
                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            ag.show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {//在对话框中设置”取消按钮“
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
            dialog = builder.create();
            dialog.show();
        });

        binding.connect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //如果是登录页面传来的的Intent
        //则更改viewModel的isLogin为true
        //让观察者自动更改用户信息
        if (resultCode == 1001) {
            settingViewModel.isLogin.setValue(true);
            editor.putBoolean("isLogin", true);
            editor.apply();
            Toast.makeText(getContext(), "登录成功!", Toast.LENGTH_SHORT).show();
        }
    }
}