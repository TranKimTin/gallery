package com.example.gallery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gallery.MainActivity;
import com.example.gallery.R;
import com.example.gallery.modal.AllImage;
import com.example.gallery.modal.MyImage;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.gallery.MainActivity.TAG;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.AllViewHolder>{
    private List<AllImage> list;
    private Context context;

    public void setList(List<AllImage> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public AllAdapter(Context context, List<AllImage> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all, parent, false);
        return new AllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder holder, int position) {
        AllImage mList = list.get(position);
        holder.tvDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(mList.getDate()));
//        holder.rcvSubAll.setLayoutManager(new LinearLayoutManager(context));
        SubAllAdapter adapter = new SubAllAdapter(context, mList.getList());
        holder.rcvSubAll.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AllViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rcvSubAll;
        private TextView tvDate;

        public AllViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            rcvSubAll = itemView.findViewById(R.id.rcvSubAll);
            rcvSubAll.setNestedScrollingEnabled(false);
            rcvSubAll.setLayoutManager(new GridLayoutManager(context, 4));
            rcvSubAll.setHasFixedSize(true);
        }
    }
}
