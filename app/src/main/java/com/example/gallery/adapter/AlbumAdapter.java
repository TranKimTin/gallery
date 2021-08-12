package com.example.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallery.R;
import com.example.gallery.SubAlbumActivity;
import com.example.gallery.modal.Album;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context context;
    private List<Album> list;
    private OnclickListener mOnclickListener;

    public AlbumAdapter(Context context, List<Album> list) {
        this.context = context;
        this.list = list;
    }

    public void setmOnclickListener(OnclickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_album, parent, false);
        AlbumViewHolder viewHolder = new AlbumViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = list.get(position);
        holder.tvName.setText(album.getName());
        holder.tvNumber.setText(album.getCount() + "");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && album.getUri() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int size = 200;
                        Bitmap thumbnail = context.getContentResolver().loadThumbnail(album.getUri(), new Size(size, size), null);
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.imv.setImageBitmap(thumbnail);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnclickListener.mOnclick(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView imv;
        private TextView tvName;
        private TextView tvNumber;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imvAlbum);
            tvName = itemView.findViewById(R.id.tvNameAlbum);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }

    public interface OnclickListener{
        void mOnclick(Album album);
    }
}
