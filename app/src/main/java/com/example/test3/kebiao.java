package com.example.test3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test3.databinding.ActivityKebiaoBinding;

import java.util.ArrayList;
import java.util.List;

public class kebiao extends AppCompatActivity {

    private static final String TAG = "kebiao";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ActivityKebiaoBinding binding;
    String xq, time;
    int xq_number, time_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );

        //viewBind的绑定
        binding = ActivityKebiaoBinding.inflate(LayoutInflater.from(this));
        //视图设置为viewBind的视图
        setContentView(binding.getRoot());
        //获取到默认的PreferenceManager
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        //获取到课表
        List<String> list_kc = init();
        //初始化页面课表视图
        init_kc(list_kc);

        //spinner的初始化数据
        List<String> list_xq = new ArrayList<>();
        List<String> list_time = new ArrayList<>();
        list_xq.add("星期一");
        list_xq.add("星期二");
        list_xq.add("星期三");
        list_xq.add("星期四");
        list_xq.add("星期五");
        list_time.add("第一节");
        list_time.add("第二节");
        list_time.add("第三节");
        list_time.add("第四节");

        //初始化适配器
        ArrayAdapter<String> adapter_xq = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list_xq);
        ArrayAdapter<String> adapter_time = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list_time);

        //给spinner设置适配器
        binding.spinnerXq.setAdapter(adapter_xq);
        binding.spinnerTime.setAdapter(adapter_time);

        //设置spinner选择监听
        binding.spinnerXq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xq = list_xq.get(position);
                xq_number = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = list_time.get(position);
                time_number = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //将设置好的课程添加到课表
        binding.addKc.setOnClickListener(v -> {
            if (!pref.getBoolean("isLogin", false)){
                Toast.makeText(this, "请先登录!", Toast.LENGTH_SHORT).show();
            }
            String kcName = binding.inputKc.getText().toString();
            if (xq.isEmpty() || time.isEmpty() || kcName.isEmpty()) {
                Toast.makeText(this, "选项不可为空", Toast.LENGTH_SHORT).show();
            } else {
                int kc_ID = xq_number * 4 + time_number + 1;
                editor.putString("kc_" + kc_ID, kcName);
                editor.apply();
                Toast.makeText(this, "已修改,请重新进入刷新!", Toast.LENGTH_SHORT).show();
                //添加成功后将参数清空
                binding.inputKc.setText("");
                binding.spinnerXq.setSelection(0);
                binding.spinnerTime.setSelection(0);
            }
        });

    }

    private void init_kc(List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            String kc = list.get(i);
            switch (i) {
                case 0: {
                    binding.kc1.setText(kc);
                    break;
                }
                case 1: {
                    binding.kc2.setText(kc);
                    break;
                }
                case 2: {
                    binding.kc3.setText(kc);
                    break;
                }
                case 3: {
                    binding.kc4.setText(kc);
                    break;
                }
                case 4: {
                    binding.kc5.setText(kc);
                    break;
                }
                case 5: {
                    binding.kc6.setText(kc);
                    break;
                }
                case 6: {
                    binding.kc7.setText(kc);
                    break;
                }
                case 7: {
                    binding.kc8.setText(kc);
                    break;
                }
                case 8: {
                    binding.kc9.setText(kc);
                    break;
                }
                case 9: {
                    binding.kc10.setText(kc);
                    break;
                }
                case 10: {
                    binding.kc11.setText(kc);
                    break;
                }
                case 11: {
                    binding.kc12.setText(kc);
                    break;
                }
                case 12: {
                    binding.kc13.setText(kc);
                    break;
                }
                case 13: {
                    binding.kc14.setText(kc);
                    break;
                }
                case 14: {
                    binding.kc15.setText(kc);
                    break;
                }
                case 15: {
                    binding.kc16.setText(kc);
                    break;
                }
                case 16: {
                    binding.kc17.setText(kc);
                    break;
                }
                case 17: {
                    binding.kc18.setText(kc);
                    break;
                }
                case 18: {
                    binding.kc19.setText(kc);
                    break;
                }
                case 19: {
                    binding.kc20.setText(kc);
                    break;
                }
            }
        }
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
}