package com.example.test3.ui.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.History;
import com.example.test3.R;
import com.example.test3.databinding.FragmentHistoryBinding;
import com.example.test3.myAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class history extends Fragment {
    private static final String TAG = "history";

    private historyViewModel historyViewModel;
    FragmentHistoryBinding binding;
    myAdapter adapter;
    LiveData<List<History>> liveList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(historyViewModel.class);
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new myAdapter();
        List<History> listTest = new ArrayList<>();
        //给适配器适配空数据源
        adapter.setAllWords(listTest);
        binding.recycler.setAdapter(adapter);

        //得到所有签到信息
        liveList = historyViewModel.getAllHistory();
        //观察签到信息是否改变
        //如果改变,则更改适配器数据源
        //通知适配器改变数据
        liveList.observe(getViewLifecycleOwner(), new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                adapter.setAllWords(liveList.getValue());
                adapter.notifyDataSetChanged();
            }
        });

        binding.delete.setOnClickListener(view -> {

            AlertDialog.Builder ag=new AlertDialog.Builder(getContext())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("系统提示")
                    .setMessage("是否删除所有数据??")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除所有数据
                            historyViewModel.deleteAll();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            ag.show();


        });

        //监听搜索框
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //将所有符合条件的记录添加到新的集合里面
                List<History> list = new ArrayList<>();
                for (History his : liveList.getValue()) {
                    if(his.getKc().contains(s)){
                        list.add(his);
                        //Log.d(TAG, "onQueryTextChange: ........"+his.getKc());
                    }
                }
                //通知数据改变
                adapter.setAllWords(list);
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        //左滑删除
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START| ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                History history1 = historyViewModel.getAllHistory().getValue().get(viewHolder.getAdapterPosition());
                historyViewModel.delete(history1);
                Snackbar.make(binding.getRoot().getViewById(R.id.hislayout),"删除了一记录!",Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                historyViewModel.insertHistory(history1);
                            } 
                        })
                        .show();
            }
        }).attachToRecyclerView(binding.recycler);


    }


}