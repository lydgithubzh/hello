package com.example.test3.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.test3.History;
import com.example.test3.R;
import com.example.test3.databinding.FragmentHomeBinding;
import com.example.test3.kebiao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    FragmentHomeBinding binding;
    SharedPreferences pref;
    List<String> kc_list;
    List<kc_pre> kc_pres;

    int qiandao_time_hour;
    int qiandao_time_minute;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());


        homeViewModel.getIsLogin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean islogin) {
                if (islogin) {
                    Log.d(TAG, "onChanged: " + islogin);
                    binding.homeUserImage.setImageResource(R.mipmap.user);
                    binding.homeUserName.setText(pref.getString("userName", "哦哦"));
                } else {
                    binding.homeUserImage.setImageResource(R.mipmap.main);
                    binding.homeUserName.setText("哦哦");

                }
            }
        });
        
        //初始化课程信息
        kc_list = init();

        //得到当前时间
        Calendar calendar = Calendar.getInstance();
        //当前日期
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        //Log.d(TAG, "onActivityCreated: Day .............."+day+"........."+getHour(hour)+"........"+hour);
        //定位课程位置
        int index = (day - 1) * 4 + getHour(hour);

        //初始化下节课程信息
        kc_pres = getKc_pres(index);

        //如果课程信息为空,则不设置展示课程
        if (kc_pres.size() != 0) {
            binding.qiandao.setText(kc_pres.get(0).getKc());
        } else {
            binding.qiandao.setText("暂无");
        }

        //同上
        if (kc_pres != null && kc_pres.size() > 1) {
            binding.nextKc.setText(kc_pres.get(1).getKc());
        } else {
            binding.nextKc.setText("暂无");
        }


        binding.qiandao.setOnClickListener(v -> {

            if (!pref.getBoolean("isLogin", false)) {
                Toast.makeText(getActivity(), "请您前往\"我的\"登录再进行签到!", Toast.LENGTH_SHORT).show();
            } else {
                //得到课程签到时间
                getQiandaoTime();

                //得到当前时间和分钟
                int hour_ = calendar.get(Calendar.HOUR_OF_DAY);
                int minter = calendar.get(Calendar.MINUTE);

                //判断当前时间是否可以签到
                //有5分钟左右的人性时间
                if (hour_ == qiandao_time_hour && (Math.abs(qiandao_time_minute - minter) <= 5)) {
                    Toast.makeText(getActivity(), "签到成功!", Toast.LENGTH_SHORT).show();

                    //得到签到的时间和分钟
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day_ = calendar.get(Calendar.DAY_OF_MONTH);
                    //初始化签到时间信息
                    String time = month + "." + day_ + " " + hour_ + ":" + minter;
                    History history = new History(kc_pres.get(0).getKc(), time);
//                    History history1 = new History("hello", time);
//                    History history2 = new History("web", time);
//                    History history3 = new History("java", time);
                    //将签到信息保存到数据库
                    homeViewModel.insertHistory(history);

                    //更改下一课程展示
                    binding.qiandao.setText(kc_pres.get(1).getKc());
                    //更改下一课程的数据
                    kc_pres = getKc_pres((day - 1) * 4 + kc_pres.get(0).getTime() + 1);
                    binding.nextKc.setText(kc_pres.get(0).getKc());

                } else {
                    Toast.makeText(getActivity(), "签到成功!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.kebiao.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), kebiao.class);
            startActivity(intent);
        });


    }


    private List<String> init() {
        List<String> list_kc = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            String s = "kc_" + i;
            String kc = pref.getString(s, "暂无");
            list_kc.add(kc);

        }
        return list_kc;
    }

    private int getHour(int hour) {
        int re = 0;
        switch (hour) {
            case 24:
            case 23:
            case 22:
            case 21:
            case 20:
            case 19:
            case 18:
            case 17:
            case 16: {
                re = 4;
                break;
            }
            case 15:
            case 14: {
                re = 3;
                break;
            }
            case 13:
            case 12:
            case 11:
            case 10: {
                re = 2;
                break;
            }
            default:
                re = 1;
        }
        return re;
    }

    private List<kc_pre> getKc_pres(int index) {

        List<kc_pre> pre_list = new ArrayList<>();
        for (int i = index, j = 0; j < 2; i++) {
            //Log.d(TAG, "getKc_pres: ................."+index);
            if (i == 20) {
                i = 0;
            }
            if (!kc_list.get(i).equals("暂无")) {
                kc_pre pre = new kc_pre(kc_list.get(i), (i + 1) % 4);
                pre_list.add(pre);
                j++;
            }
            if (i == index - 1) {
                Log.d(TAG, "onActivityCreated: 暂无课程");
                break;
            }
        }

        return pre_list;
    }

    private void getQiandaoTime() {
        switch (kc_pres.get(0).getTime()) {
            case 1: {
                qiandao_time_hour = 8;
                qiandao_time_minute = 15;
                break;
            }
            case 2: {
                qiandao_time_hour = 10;
                qiandao_time_minute = 15;
                break;
            }
            case 3: {
                qiandao_time_hour = 14;
                qiandao_time_minute = 30;
                break;
            }
            case 4: {
                qiandao_time_hour = 16;
                qiandao_time_minute = 15;
                break;
            }

        }
    }

    static class kc_pre {
        String kc;
        int time;

        public kc_pre(String kc, int time) {
            this.kc = kc;
            this.time = time;
        }

        public String getKc() {
            return kc;
        }

        public void setKc(String kc) {
            this.kc = kc;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

}