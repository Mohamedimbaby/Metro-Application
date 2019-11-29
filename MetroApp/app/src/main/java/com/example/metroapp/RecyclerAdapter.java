package com.example.metroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AdapterOfView> {
 ArrayList<String> list1;
 ArrayList<String> list2;
Context context;

    public RecyclerAdapter(ArrayList<String> list1, ArrayList<String> list2, Context context) {
        this.list1 = list1;
        this.list2 = list2;
        this.context = context;
    }



    @NonNull
    @Override
    public AdapterOfView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        RecyclerAdapter.AdapterOfView viewAdapter = new RecyclerAdapter.AdapterOfView(view);
        return viewAdapter;

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOfView holder, int position) {
holder.textView1.setText(list1.get(position));
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    class AdapterOfView extends RecyclerView.ViewHolder
    {
TextView textView1;
AutoCompleteTextView editText1;
        public AdapterOfView(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.stationName);
            editText1=itemView.findViewById(R.id.currentStationAutoComplete);

        }
    }
}
