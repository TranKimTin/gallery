package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gallery.adapter.AlbumAdapter;
import com.example.gallery.adapter.AllAdapter;
import com.example.gallery.adapter.SubAlbumAdapter;
import com.example.gallery.modal.Album;
import com.example.gallery.modal.AllImage;
import com.example.gallery.modal.MyImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.gallery.MainActivity.*;

public class SubAlbumActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNameAlbum;
    private RecyclerView rcvSubAlbum;
    private LinearLayout layoutAction;
    private SubAlbumAdapter adapter;
    private Button btnCopy, btnMove, btnRemove;
    private List<MyImage> listSelected;
    private AlertDialog dialog;
    private ThreadPoolExecutor executor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_album);

        initExecutor();

        layoutAction = findViewById(R.id.layoutActionSubAlbum);
        tvNameAlbum = findViewById(R.id.tvNameSubAlbum);
        rcvSubAlbum = findViewById(R.id.rcvSubAlbum);
        rcvSubAlbum.setHasFixedSize(true);
        rcvSubAlbum.setLayoutManager(new GridLayoutManager(this, 4));

        btnCopy = findViewById(R.id.btnCopySubAlbum);
        btnMove = findViewById(R.id.btnMoveSubAlbum);
        btnRemove = findViewById(R.id.btnRemoveSubAlbum);

        btnCopy.setOnClickListener(this);
        btnMove.setOnClickListener(this);
        btnRemove.setOnClickListener(this);


        adapter = new SubAlbumAdapter(this, new ArrayList<>());
        rcvSubAlbum.setAdapter(adapter);
        onCheck = false;
        refresh();
    }

    private void refresh() {
        getData(this);
        onCheck = false;
        String album = getIntent().getStringExtra("album_name");
        List<MyImage> list = hAlbum.get(album);
        if(list == null) list = new ArrayList<>();
        adapter.setList(list);
        notifyDataSetChanged();
    }

    public void updateLayout() {
        if (onCheck) {
            tvNameAlbum.setVisibility(View.GONE);
            layoutAction.setVisibility(View.VISIBLE);
        } else {
            tvNameAlbum.setVisibility(View.VISIBLE);
            layoutAction.setVisibility(View.GONE);
        }
    }

    public void notifyDataSetChanged() {
        updateLayout();
        AlbumFragment.getInstance().notifyDataSetChanged();
        adapter.fillChecked(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (onCheck) {
            onCheck = false;
            notifyDataSetChanged();
        } else {
            finish();
        }
    }

    private void initExecutor() {
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        int queueCapacity = 1000;

        executor = new ThreadPoolExecutor(corePoolSize, // Số corePoolSize
                maximumPoolSize, // số maximumPoolSize
                500, // thời gian một thread được sống nếu không làm gì
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity)); // Blocking queue để cho request đợi
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCopySubAlbum:
                listSelected = getListSelected();
                if (listSelected.size() == 0) {
                    Toast.makeText(this, "Chưa chọn media nào", Toast.LENGTH_SHORT).show();
                    return;
                }
                displayAlertDialog("copy");
                break;
            case R.id.btnMoveSubAlbum:
                listSelected = getListSelected();
                if (listSelected.size() == 0) {
                    Toast.makeText(this, "Chưa chọn media nào", Toast.LENGTH_SHORT).show();
                    return;
                }
                displayAlertDialog("move");
                break;
            case R.id.btnRemoveSubAlbum:
                listSelected = getListSelected();
                if (listSelected.size() == 0) {
                    Toast.makeText(this, "Chưa chọn media nào", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(this)
                        .setTitle("Xóa " + listSelected.size() + " media")
                        .setMessage("Bạn có chắc chắn chưu?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                remove(listSelected);
                                Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                onCheck = false;
                                updateLayout();
                                refresh();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
        }
    }

    public void displayAlertDialog(String type) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.fragment_album, null);
        RecyclerView rcvAlbum = alertLayout.findViewById(R.id.rcvAlbum);
        ImageView imvAdd = alertLayout.findViewById(R.id.imvAddAlbum);
        imvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SubAlbumActivity.this);
                builder.setTitle("Tên album");
                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/" + m_Text;
                        addAdbum(path, type, listSelected);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setCancelable(true);
                builder.show();
            }
        });

        rcvAlbum.setLayoutManager(new GridLayoutManager(SubAlbumActivity.this, 3));
        List<Album> list = new ArrayList<>();
        for (String name : hAlbum.keySet()) {
            List<MyImage> l = hAlbum.get(name);
            list.add(new Album(name, l.size(), l.get(0).getUri(), l.get(0).getUrl()));
        }
        AlbumAdapter adapter = new AlbumAdapter(SubAlbumActivity.this, list);
        adapter.setmOnclickListener(new AlbumAdapter.OnclickListener() {
            @Override
            public void mOnclick(Album album) {
                addAdbum(album.getUrl(), type, listSelected);
            }
        });
        rcvAlbum.setAdapter(adapter);

        AlertDialog.Builder alert = new AlertDialog.Builder(SubAlbumActivity.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", null);
        dialog = alert.create();
        dialog.show();
    }

    private void addAdbum(String url, String type, List<MyImage> listSelected) {
        initExecutor();
        for (MyImage image : listSelected) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        File folderAlbum = new File(url);
                        if (!folderAlbum.exists()) folderAlbum.mkdir();

                        File src = new File(image.getUrl());
                        File dest = new File(url + "/" + image.getName());

                        String path = copyFile(src, dest);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.Images.Media.DATA, path);
                        getContentResolver().insert(
                                image.getName().matches(".*mp4")
                                        ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                        : MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
        await();
        if (type.equals("move")) remove(listSelected);
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        refresh();
    }

    private void await() {
        try {
            while (!executor.awaitTermination(50, TimeUnit.MILLISECONDS)) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void remove(List<MyImage> listSelected) {
        for (MyImage image : listSelected) {
            removeFile(this, image);
        }
    }
}