package com.example.test3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.viewHolder> {

    private List<History> allRecord;

    public myAdapter(){

    }

    public void setAllWords(List<History> allWords) {
        this.allRecord = allWords;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new viewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.id.setText(String.valueOf(position + 1));
        holder.textView_kc.setText(allRecord.get(position).getKc());
        holder.textView_time.setText(allRecord.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return allRecord.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView id, textView_kc, textView_time;

        public viewHolder(@NonNull View view) {
            super(view);
            id = view.findViewById(R.id.item_id);
            textView_kc = view.findViewById(R.id.item_kc);
            textView_time = view.findViewById(R.id.item_time);
        }
    }
}
