package com.example.metroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewAdapter> {
    ArrayList<String> stationNames;
    private Context context;

    public RecyclerViewAdapter( Context context, ArrayList<String> stationNames) {
        this.stationNames = stationNames;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
                 ViewAdapter viewAdapter = new ViewAdapter(view);


        return viewAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter holder, int position) {
        holder.stationName.setText(stationNames.get(position));
    }

    @Override
    public int getItemCount() {
        return stationNames.size();
    }






    public class ViewAdapter extends RecyclerView.ViewHolder
    {
        TextView stationName ;
        LinearLayout parentLayout;
        public ViewAdapter(@NonNull View itemView) {
            super(itemView);
            stationName=itemView.findViewById(R.id.stationName);
            parentLayout=itemView.findViewById(R.id.parent_layout);

        }
    }
}
