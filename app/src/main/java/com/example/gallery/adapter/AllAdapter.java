package com.example.gallery.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gallery.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.AllViewHolder>{
    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AllViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rcvAll;
        private TextView tvDate;

        public AllViewHolder(@NonNull View itemView) {
            super(itemView);
            rcvAll = itemView.findViewById(R.id.rcvAll);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
