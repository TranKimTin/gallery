package com.example.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gallery.MainActivity;
import com.example.gallery.R;
import com.example.gallery.modal.MyImage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.gallery.MainActivity.*;

public class SubAllAdapter extends RecyclerView.Adapter<SubAllAdapter.SubAllViewHolder> {
    private List<MyImage> list;
    private Context context;
    private ThreadPoolExecutor executor;

    public SubAllAdapter(Context context, List<MyImage> list) {
        this.list = list;
        this.context = context;

        int corePoolSize = 4;
        int maximumPoolSize = 10;
        int queueCapacity = 1000;
        executor = new ThreadPoolExecutor(corePoolSize, // Số corePoolSize
                maximumPoolSize, // số maximumPoolSize
                500, // thời gian một thread được sống nếu không làm gì
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity)); // Blocking queue để cho request đợi
    }

    void setData(List<MyImage> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_sub_all, parent, false);
        SubAllViewHolder viewHolder = new SubAllViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubAllViewHolder holder, int position) {
        Log.d(TAG, "pos: " + position);
        MyImage image = list.get(position);
        holder.imv.setImageResource(R.drawable.image_gallery);
        if (image.getUri() != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int size = list.size() < 50 ? 200 : 150;
                        Bitmap thumbnail = context.getContentResolver().loadThumbnail(image.getUri(), new Size(size, size), null);
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
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubAllViewHolder extends RecyclerView.ViewHolder {
        ImageView imv;

        public SubAllViewHolder(@NonNull View itemView) {
            super(itemView);
            imv = itemView.findViewById(R.id.imvAll);
        }
    }
}
