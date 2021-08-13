package com.example.gallery;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gallery.adapter.AlbumAdapter;
import com.example.gallery.modal.Album;
import com.example.gallery.modal.MyImage;

import java.util.ArrayList;
import java.util.List;

import static com.example.gallery.MainActivity.TAG;
import static com.example.gallery.MainActivity.hAlbum;

public class AlbumFragment extends Fragment {
    private static AlbumFragment instance;
    private RecyclerView rcvAlbum;
    private List<Album> list;
    private ImageView imvAdd;

    public AlbumFragment() {
    }

    public static AlbumFragment getInstance() {
        if (instance == null) instance = new AlbumFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvAlbum = view.findViewById(R.id.rcvAlbum);
        imvAdd = view.findViewById(R.id.imvAddAlbum);
        imvAdd.setVisibility(View.GONE);

        rcvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 3));
        list = new ArrayList<>();
        for (String name : hAlbum.keySet()) {
            List<MyImage> l = hAlbum.get(name);
            list.add(new Album(name, l.size(), l.get(0).getUri(), l.get(0).getUrl()));
        }
        AlbumAdapter adapter = new AlbumAdapter(getContext(), list);
        adapter.setmOnclickListener(new AlbumAdapter.OnclickListener() {
            @Override
            public void mOnclick(Album album) {
                Intent intent = new Intent(getContext(), SubAlbumActivity.class);
                intent.putExtra("album_name", album.getName());
                getContext().startActivity(intent);
            }
        });
        rcvAlbum.setAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        list.clear();
        for (String name : hAlbum.keySet()) {
            List<MyImage> l = hAlbum.get(name);
            list.add(new Album(name, l.size(), l.get(0).getUri(), l.get(0).getUrl()));
        }
        rcvAlbum.getAdapter().notifyDataSetChanged();
    }
}