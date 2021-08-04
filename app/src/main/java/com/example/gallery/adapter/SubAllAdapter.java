package com.example.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gallery.R;
import com.example.gallery.modal.MyImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubAllAdapter extends RecyclerView.Adapter<SubAllAdapter.SubAllViewHolder>{
    private List<MyImage> list;
    private Context context;

    public SubAllAdapter(List<MyImage> list, Context context) {
        this.list = list;
        this.context = context;
    }

    void setData(List<MyImage> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_all, parent, false);
        return new SubAllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubAllViewHolder holder, int position) {
        MyImage image = list.get(position);
        if(image.getUri() != null){
            holder.imv.setImageURI(image.getUri());
        }
        else{
            holder.imv.setImageResource(R.drawable.image_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubAllViewHolder extends RecyclerView.ViewHolder{
        ImageView imv;
        public SubAllViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imvAll);
        }
    }
}
