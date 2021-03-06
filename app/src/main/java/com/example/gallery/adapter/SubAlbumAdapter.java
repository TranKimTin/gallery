package com.example.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.gallery.AlbumFragment;
import com.example.gallery.AllFragment;
import com.example.gallery.R;
import com.example.gallery.SubAlbumActivity;
import com.example.gallery.modal.MyImage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.gallery.MainActivity.TAG;
import static com.example.gallery.MainActivity.onCheck;
import static com.example.gallery.MainActivity.open_file;

public class SubAlbumAdapter extends RecyclerView.Adapter<SubAlbumAdapter.SubAlbumViewHolder> {
    private List<MyImage> list;
    private Context context;
    private ThreadPoolExecutor executor;

    public SubAlbumAdapter(Context context, List<MyImage> list) {
        this.list = list;
        this.context = context;

        int corePoolSize = 3;
        int maximumPoolSize = 10;
        int queueCapacity = 1000;
        executor = new ThreadPoolExecutor(corePoolSize, // Số corePoolSize
                maximumPoolSize, // số maximumPoolSize
                500, // thời gian một thread được sống nếu không làm gì
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity)); // Blocking queue để cho request đợi
    }

    public void setList(List<MyImage> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SubAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_sub_album, parent, false);
        SubAlbumViewHolder viewHolder = new SubAlbumViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubAlbumViewHolder holder, int position) {
        MyImage image = list.get(position);
        Log.d(TAG, image.getName());
        holder.imv.setImageResource(R.drawable.image_gallery);
        if (image.getUrl() != null && image.getUrl().matches(".*mp4"))
            holder.imvPlay.setVisibility(View.VISIBLE);
        else holder.imvPlay.setVisibility(View.GONE);

        if (onCheck) holder.cbxChecked.setVisibility(View.VISIBLE);
        else holder.cbxChecked.setVisibility(View.GONE);

        holder.cbxChecked.setChecked(image.isChecked());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int size = 200;
                        Bitmap thumbnail = context.getContentResolver().loadThumbnail(image.getUri(), new Size(size, size), null);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.imv.setImageBitmap(thumbnail);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheck) {
                    image.setChecked(!image.isChecked());
                    notifyItemChanged(position);
                } else {
                    open_file(context, image.getUri());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                image.setChecked(!image.isChecked());
                if (onCheck) {
                    notifyItemChanged(position);
                } else {
                    notifyDataSetChanged();
                    onCheck = true;
                    ((SubAlbumActivity)context).updateLayout();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubAlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imv;
        ImageView imvPlay;
        CheckBox cbxChecked;

        public SubAlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imvSubAlbum);
            imvPlay = itemView.findViewById(R.id.imvPlayALbum);
            cbxChecked = itemView.findViewById(R.id.cbxCheckedSubAlbum);
        }
    }

    public void fillChecked(boolean checked) {
        for (int i = 0; i < list.size(); i++)
            list.get(i).setChecked(checked);
    }
}
