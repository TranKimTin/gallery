package com.example.gallery;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gallery.adapter.AlbumAdapter;
import com.example.gallery.adapter.AllAdapter;
import com.example.gallery.modal.Album;
import com.example.gallery.modal.AllImage;
import com.example.gallery.modal.MyImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.gallery.MainActivity.*;

public class AllFragment extends Fragment implements View.OnClickListener {
    private static AllFragment instance;
    private RecyclerView rcvAll;
    private List<AllImage> list;
    private AllAdapter adapter;
    private TextView tvTitle;
    private LinearLayout layoutAction;
    private Button btnCopy, btnMove;
    private List<MyImage> listSelected;
    private AlertDialog dialog;

    public AllFragment() {

    }

    public static AllFragment getInstance() {
        if (instance == null) instance = new AllFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tvTitleAll);
        layoutAction = view.findViewById(R.id.layoutAction);
        btnCopy = view.findViewById(R.id.btnCopy);
        btnMove = view.findViewById(R.id.btnMove);
        rcvAll = view.findViewById(R.id.rcvAll);
        rcvAll.setNestedScrollingEnabled(false);
//        rcvAll.setHasFixedSize(true);

        btnCopy.setOnClickListener(this);
        btnMove.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new AllAdapter(getContext(), list);
        rcvAll.setAdapter(adapter);
        rcvAll.setLayoutManager(new LinearLayoutManager(getContext()));

        refresh();
    }

    private void refresh() {
        list.clear();
        getData(getContext());

        List<MyImage> item = new ArrayList<>();
        for (int i = 0; i < listAll.size(); i++) {
            if (i > 0 && listAll.get(i).getDate() != listAll.get(i - 1).getDate()) {
                list.add(new AllImage(new Date(listAll.get(i - 1).getDate() * 86400000), item));
                item = new ArrayList<>();
            }
            item.add(listAll.get(i));
        }
        if (item.size() > 0)
            list.add(new AllImage(new Date(item.get(0).getDate() * 86400000), item));
        adapter = new AllAdapter(getContext(), list);
        adapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        updateLayout();
        adapter.clearChecked();
        rcvAll.getAdapter().notifyDataSetChanged();
    }

    public void updateLayout() {
        if (onCheck) {
            tvTitle.setVisibility(View.GONE);
            layoutAction.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            layoutAction.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCopy:
                listSelected = getListSelected();
                if (listSelected.size() == 0) {
                    Toast.makeText(getContext(), "Chưa chọn media nào", Toast.LENGTH_SHORT).show();
                    return;
                }
                displayAlertDialog("copy");
                break;
            case R.id.btnMove:
                listSelected = getListSelected();
                if (listSelected.size() == 0) {
                    Toast.makeText(getContext(), "Chưa chọn media nào", Toast.LENGTH_SHORT).show();
                    return;
                }
                displayAlertDialog("move");
                break;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Tên album");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/" + m_Text;
                        addAdbum(path, type);
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
        rcvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 3));
        List<Album> list = new ArrayList<>();
        for (String name : hAlbum.keySet()) {
            List<MyImage> l = hAlbum.get(name);
            list.add(new Album(name, l.size(), l.get(0).getUri(), l.get(0).getUrl()));
        }
        AlbumAdapter adapter = new AlbumAdapter(getContext(), list);
        adapter.setmOnclickListener(new AlbumAdapter.OnclickListener() {
            @Override
            public void mOnclick(Album album) {
                addAdbum(album.getUrl(), type);
            }
        });
        rcvAlbum.setAdapter(adapter);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", null);
        dialog = alert.create();
        dialog.show();
    }

    private void addAdbum(String url, String type) {
        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
        for (MyImage image : listSelected) {
            try {
                File folderAlbum = new File(url);
                if (!folderAlbum.exists()) folderAlbum.mkdir();

                File src = new File(image.getUrl());
                File dest = new File(url + "/" + image.getName());

                String path = copyFile(src, dest);
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DATA, path);
                getContext().getContentResolver().insert(
                        image.getName().matches(".*mp4")
                                ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                : MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                );
                if (type.equals("move")) src.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        refresh();
    }

}